package fitness.club.repository;
import fitness.club.entity.BaseEntity;
import fitness.club.exeptions.RepositoryException;
import fitness.club.util.Column;
import fitness.club.util.ConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;


public abstract class BaseRepositoryImpl<T, Integer> implements BaseRepository<T, Integer> {
    private final Logger logger = LoggerFactory.getLogger(BaseRepositoryImpl.class);

    protected final Class<T> entityClass;

    public BaseRepositoryImpl(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected abstract String getSelectAllSQL();
    protected abstract String getSelectByIdSQL();
    protected abstract String getInsertSQL();
    protected abstract String getUpdateSQL();
    protected abstract String getDeleteByIdSQL();

    @Override
    public Optional<T> findById(Integer id) {
        String sql = getSelectByIdSQL();
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(sql)) {
            statement.setObject(1, id);
            logger.debug(statement.toString());
            var rs = statement.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToEntity(rs));
            }
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<T> findAll() {
        String sql = getSelectAllSQL();
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(sql);
             var rs = statement.executeQuery()) {
            logger.debug(statement.toString());
            var list = new ArrayList<T>();
            while (rs.next()) {
                list.add(mapResultSetToEntity(rs));
            }
            return list;
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    @Override
    public T add(T entity) {
        String sql = getInsertSQL();
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            setStatementParameters(statement, entity, true); // exclude ID
            statement.executeUpdate();
            logger.debug(statement.toString());
            var rs = statement.getGeneratedKeys();
            if (rs.next()) {
                Field idField = getIdField();
                idField.setAccessible(true);
                idField.set(entity, rs.getObject(1));
            }
        } catch (Exception e) {
            throw new RepositoryException(e.getMessage());
        }
        return entity;
    }

    @Override
    public T update(T entity) {
        String sql = getUpdateSQL();
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(sql)) {
            setStatementParameters(statement, entity, false); // include ID
            logger.debug(statement.toString());
            int rows = statement.executeUpdate();
            if (rows == 0) {
                throw new RuntimeException("Entity not found for update: " + entity);
            }
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
        return entity;
    }

    @Override
    public void delete(Integer id) {
        String sql = getDeleteByIdSQL();
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(sql)) {
            statement.setObject(1, id);
            logger.debug(statement.toString());
            int rows = statement.executeUpdate();
            if (rows == 0) {
                throw new RuntimeException("Entity not found for deletion: " + id);
            }
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    protected T mapResultSetToEntity(ResultSet rs) throws SQLException {
        try {
            T entity = entityClass.getDeclaredConstructor().newInstance();
            Field[] fields = entityClass.getDeclaredFields();
            for (Field field : fields) {
                Column column = field.getAnnotation(Column.class);
                if (column != null) {
                    field.setAccessible(true); // Убедимся, что поле доступно
                    Object value = rs.getObject(column.name());
                    field.set(entity, value);
                }
            }
            return entity;
        } catch (Exception e) {
            throw new RuntimeException("Error mapping ResultSet to entity", e);
        }
    }

    protected void setStatementParameters(PreparedStatement statement, T entity, boolean excludeId) throws SQLException {
        Field[] fields = entityClass.getDeclaredFields();
        int index = 1;
        for (Field field : fields) {
            Column column = field.getAnnotation(Column.class);
            if (column != null) {
                if (excludeId && column.name().equalsIgnoreCase("id")) {
                    continue; // skip ID on insert
                }
                field.setAccessible(true); // Убедимся, что поле доступно
                try {
                    Object value = field.get(entity);
                    statement.setObject(index++, value);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Cannot access field: " + field.getName(), e);
                }
            }
        }
        if (!excludeId) {
            Field idField = getIdField();
            idField.setAccessible(true); // Убедимся, что ID-поле доступно
            try {
                statement.setObject(index, idField.get(entity));
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Cannot access ID field: " + idField.getName(), e);
            }
        }
    }

    protected Field getIdField() {
        for (Field field : entityClass.getDeclaredFields()) {
            field.setAccessible(true);
            Column col = field.getAnnotation(Column.class);
            if (col != null && col.name().equalsIgnoreCase("id")) {
                return field;
            }
        }
        throw new RuntimeException("No field annotated as 'id' found in " + entityClass);
    }
}