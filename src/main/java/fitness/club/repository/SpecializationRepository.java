package fitness.club.repository;

import fitness.club.entity.Specialization;
import fitness.club.exeptions.RepositoryException;
import fitness.club.util.ConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Optional;


public class SpecializationRepository extends BaseRepositoryImpl<Specialization,Integer> {
    Logger logger = LoggerFactory.getLogger(SpecializationRepository.class);
    private final String ADD_SQL = """
            insert into fitness_club.specialization (name) values (?)""";
    private final String DELETE_SQL = """
            delete from fitness_club.specialization where id = ?""";
    private final String FIND_BY_ID = """
            SELECT id, name from fitness_club.specialization where id = ?""";
    private final String FIND_BY_NAME = """
            select id, name from fitness_club.specialization where name = ?""";
    private final String FIND_ALL = """
            select * from fitness_club.specializations""";
    private final String UPDATE_SQL = """
            update fitness_club.specialization set name = ? where id = ?""";

    public Optional<Specialization> findByName(String name) {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(FIND_BY_NAME)) {
            statement.setObject(1, name);
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

    public SpecializationRepository() {super(Specialization.class);}

    @Override
    protected String getSelectAllSQL() {return FIND_ALL;}

    @Override
    protected String getSelectByIdSQL() {return FIND_BY_ID;}

    @Override
    protected String getInsertSQL() {return ADD_SQL;}

    @Override
    protected String getUpdateSQL() {return UPDATE_SQL;}

    @Override
    protected String getDeleteByIdSQL() {return DELETE_SQL;}
}
