package fitness.club.repository;

import fitness.club.entity.Coach;
import fitness.club.entity.CoachSpecialization;
import fitness.club.exeptions.RepositoryException;
import fitness.club.util.ConnectionManager;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CoachSpecializationRepository extends BaseRepositoryWithoutId<CoachSpecialization, CoachSpecialization>  {

    private static final String SELECT_ALL = """
            SELECT coach_id, spec_id FROM fitness_club.trainer_specialization""";
    private static final String SELECT_BY_ID = """
            SELECT coach_id, spec_id FROM fitness_club.trainer_specialization 
            WHERE coach_id = ? AND spec_id = ?""";
    private static final String INSERT = """
            INSERT INTO fitness_club.trainer_specialization (coach_id, spec_id) 
            VALUES (?, ?)""";
    private static final String DELETE_BY_ID = """
            DELETE FROM fitness_club.trainer_specialization WHERE coach_id = ?
             AND spec_id = ?""";


    public CoachSpecializationRepository() {super(CoachSpecialization.class);}
    @Override
    protected String getSelectAllSQL() {return SELECT_ALL;}
    @Override
    protected String getSelectByIdSQL() {return SELECT_BY_ID;}
    @Override
    protected String getInsertSQL() {return INSERT;}
    @Override
    protected String getUpdateSQL() {throw new RepositoryException("Invalid request in CoachSpecializationRepository");}
    @Override
    protected String getDeleteByIdSQL() {return DELETE_BY_ID;}
    @Override
    protected String getPkFieldName() {throw new RepositoryException("Invalid request in CoachSpecializationRepository");}

    @Override
    public Optional<CoachSpecialization> findById(CoachSpecialization id) {
        String sql = getSelectByIdSQL();
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(sql)) {
            statement.setObject(1, id.getCoachId());
            statement.setObject(2, id.getSpecId());
            var rs = statement.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToEntity(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error executing query: " + sql, e);
        }
        return Optional.empty();
    }

    @Override
    public void delete(CoachSpecialization id) {
        String sql = getDeleteByIdSQL();
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(sql)) {
            statement.setObject(1, id.getCoachId());
            statement.setObject(2, id.getSpecId());
            int rows = statement.executeUpdate();
            if (rows == 0) {
                throw new RuntimeException("Entity not found for deletion: " + id);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error executing query: " + sql, e);
        }
    }

    @Override
    protected void setStatementParameters(PreparedStatement statement, CoachSpecialization entity) throws SQLException {
        statement.setObject(1, entity.getCoachId());
        statement.setObject(2, entity.getSpecId());
    }

    @Override
    protected void setStatementParametersForUpdate(PreparedStatement statement, CoachSpecialization entity) throws SQLException {
        // Update не поддерживается
    }

    @Override
    protected Field getPkField() {
        throw new UnsupportedOperationException("Composite key");
    }
}
