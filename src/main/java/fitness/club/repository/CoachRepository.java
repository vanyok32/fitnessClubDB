package fitness.club.repository;

import fitness.club.entity.Client;
import fitness.club.entity.Coach;
import fitness.club.exeptions.RepositoryException;
import fitness.club.util.ConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CoachRepository extends BaseRepositoryImpl<Coach, Integer> {

    private final Logger logger = LoggerFactory.getLogger(CoachRepository.class);

    private final String FIND_BY_ID_SQL = """
            select id, club_id, name, email, password from fitness_club.coach where id = ?""";
    private final String DELETE_SQL = """
            delete from fitness_club.coach where id = ?""";
    private final String UPDATE_SQL = """
            update fitness_club.coach set club_id=?, name=?, email = ?, password = ? where id = ?""";
    private final String FIND_ALL_SQL = """
            select id, club_id, name,emial from fitness_club.coach""";
    private final String ADD_SQL = """
            insert into fitness_club.coach (club_id, name, email, password)
                        values (?, ?,?,?);""";
    private final String FIND_COACHES_BY_SPEC_ID_CLUB_ID = """
            select id, club_id, name, email, password from fitness_club.coach c
            join fitness_club.trainer_specialization ts on c.id = ts.coach_id
            where c.club_id = ? AND ts.spec_id = ?""";
    private final String FIND_COACHES_BY_CLUB= """
            select id, club_id, name, email, password from fitness_club.coach c
            join fitness_club.trainer_specialization ts on c.id = ts.coach_id
            where c.club_id = ?""";
    private final String FIND_BY_EMAIL= """
            select id, club_id, name, email,password from fitness_club.coach where email = ?""";


    public CoachRepository() {super(Coach.class);}

    public List<Coach> findCoachesBySpecIdClubId(Integer specId, Integer clubId) {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(FIND_COACHES_BY_SPEC_ID_CLUB_ID)) {
            statement.setObject(1, clubId);
            statement.setObject(2, specId);
            logger.debug(statement.toString());
            var rs = statement.executeQuery();
            var list = new ArrayList<Coach>();
            while (rs.next()) {
                list.add(mapResultSetToEntity(rs));
            }
            return list;
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    public List<Coach> findCoachesByClubId(Integer clubId) {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(FIND_COACHES_BY_CLUB)) {
            statement.setObject(1, clubId);
            logger.debug(statement.toString());
            var rs = statement.executeQuery();
            var list = new ArrayList<Coach>();
            while (rs.next()) {
                list.add(mapResultSetToEntity(rs));
            }
            return list;
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
    }
    public Optional<Coach> findByEmail(String email) {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(FIND_BY_EMAIL);) {
            statement.setObject(1, email);
            var rs = statement.executeQuery();
            logger.debug(statement.toString());
            if (rs.next()) {
                return Optional.of(mapResultSetToEntity(rs));
            }
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    protected String getSelectAllSQL() {return FIND_ALL_SQL;}

    @Override
    protected String getSelectByIdSQL() {return FIND_BY_ID_SQL;}

    @Override
    protected String getInsertSQL() {return ADD_SQL;}

    @Override
    protected String getUpdateSQL() {return UPDATE_SQL;}

    @Override
    protected String getDeleteByIdSQL() {return DELETE_SQL;}
}
