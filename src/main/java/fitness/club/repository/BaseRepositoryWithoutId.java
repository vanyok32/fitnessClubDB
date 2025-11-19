package fitness.club.repository;

import fitness.club.exeptions.RepositoryException;
import fitness.club.repository.BaseRepository;
import fitness.club.util.Column;
import fitness.club.util.ConnectionManager;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class BaseRepositoryWithoutId<T, Integer> implements BaseRepository<T, Integer> {

    protected final Class<T> entityClass;

    public BaseRepositoryWithoutId(Class<T> entityClass) {
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
             var statement = connection.prepareStatement(sql)) {
            setStatementParameters(statement, entity);
            statement.executeUpdate();
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
            setStatementParametersForUpdate(statement, entity);
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
                    field.setAccessible(true); // ← Убедимся, что поле доступно
                    Object value = rs.getObject(column.name());
                    field.set(entity, value); // ← Ошибка может быть здесь
                }
            }
            return entity;
        } catch (Exception e) {
            throw new RuntimeException("Error mapping ResultSet to entity", e);
        }
    }

    protected void setStatementParameters(PreparedStatement statement, T entity) throws SQLException {
        Field[] fields = entityClass.getDeclaredFields();
        int index = 1;
        for (Field field : fields) {
            Column column = field.getAnnotation(Column.class);
            if (column != null) {
                field.setAccessible(true); // ← Убедимся, что поле доступно
                try {
                    Object value = field.get(entity);
                    statement.setObject(index++, value);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Cannot access field: " + field.getName(), e);
                }
            }
        }
    }

    protected void setStatementParametersForUpdate(PreparedStatement statement, T entity) throws SQLException {
        Field[] fields = entityClass.getDeclaredFields();
        int index = 1;
        for (Field field : fields) {
            field.setAccessible(true); // ← Убедимся, что поле доступно
            Column column = field.getAnnotation(Column.class);
            if (column != null) {
                try {
                    Object value = field.get(entity);
                    if (!column.name().equals(getPkFieldName())) { // skip PK
                        statement.setObject(index++, value);
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Cannot access field: " + field.getName(), e);
                }
            }
        }
        // Add PK as last parameter
        Field pkField = getPkField();
        pkField.setAccessible(true); // ← Убедимся, что PK-поле доступно
        try {
            statement.setObject(index, pkField.get(entity));
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Cannot access PK field: " + pkField.getName(), e);
        }
    }

    protected abstract String getPkFieldName();

    protected Field getPkField() {
        for (Field field : entityClass.getDeclaredFields()) {
            field.setAccessible(true);
            Column col = field.getAnnotation(Column.class);
            if (col != null && col.name().equals(getPkFieldName())) {
                return field;
            }
        }
        throw new RuntimeException("No field annotated as PK found in " + entityClass);
    }
}