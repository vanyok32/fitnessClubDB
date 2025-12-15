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
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Создаем набор доступных колонок для быстрой проверки
            java.util.Set<String> availableColumns = new java.util.HashSet<>();
            for (int i = 1; i <= columnCount; i++) {
                availableColumns.add(metaData.getColumnName(i).toLowerCase());
            }

            for (Field field : getAllFields()) {
                Column column = field.getAnnotation(Column.class);
                if (column != null) {
                    field.setAccessible(true);
                    String columnName = column.name().toLowerCase();

                    // Проверяем наличие колонки в ResultSet
                    if (availableColumns.contains(columnName)) {
                        Object value = rs.getObject(column.name());
                        // Normalize SQL types to Java types expected by entities (e.g., LocalDate)
                        if (value instanceof java.sql.Date && field.getType().equals(java.time.LocalDate.class)) {
                            value = ((java.sql.Date) value).toLocalDate();
                        }
                        field.set(entity, value);
                    }
                    // Если колонки нет в ResultSet, просто пропускаем её (оставляем значение по умолчанию)
                }
            }
            return entity;
        } catch (Exception e) {
            throw new RuntimeException("Error mapping ResultSet to entity", e);
        }
    }

    protected void setStatementParameters(PreparedStatement statement, T entity, boolean excludeId) throws SQLException {
        int index = 1;
        for (Field field : getAllFields()) {
            Column column = field.getAnnotation(Column.class);
            if (column != null) {
                if (column.name().equalsIgnoreCase("id")) {
                    continue; // id задаём отдельно, чтобы он всегда был последним
                }
                field.setAccessible(true);
                try {
                    Object value = field.get(entity);
                    // Convert Java time to SQL-friendly types
                    if (value instanceof java.time.LocalDate localDate) {
                        value = java.sql.Date.valueOf(localDate);
                    }
                    statement.setObject(index++, value);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Cannot access field: " + field.getName(), e);
                }
            }
        }
        if (!excludeId) {
            Field idField = getIdField();
            idField.setAccessible(true);
            try {
                Object idValue = idField.get(entity);
                statement.setObject(index, idValue);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Cannot access ID field: " + idField.getName(), e);
            }
        }
    }

    protected Field getIdField() {
        for (Field field : getAllFields()) {
            field.setAccessible(true);
            Column col = field.getAnnotation(Column.class);
            if (col != null && col.name().equalsIgnoreCase("id")) {
                return field;
            }
        }
        throw new RuntimeException("No field annotated as 'id' found in " + entityClass);
    }

    /**
     * Собираем все поля сущности, включая те, что объявлены в родителях (например, id в BaseEntity).
     */
    private Field[] getAllFields() {
        List<Field> fields = new ArrayList<>();
        Class<?> current = entityClass;
        while (current != null && current != Object.class) {
            fields.addAll(List.of(current.getDeclaredFields()));
            current = current.getSuperclass();
        }
        return fields.toArray(new Field[0]);
    }
}