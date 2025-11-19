package fitness.club.repository;

import fitness.club.entity.Schedule;
import fitness.club.exeptions.RepositoryException;
import fitness.club.util.ConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ScheduleRepository extends BaseRepositoryImpl<Schedule, Integer> {
    Logger logger = LoggerFactory.getLogger(ScheduleRepository.class);
    private final String FIND_BY_ID_SQL = """
            SELECT id, client_id, coach_id, workout_id, date from
            fitness_club.schedule WHERE id = ?""";

    private final String FIND_BY_COACH_ID = """
            SELECT id, client_id, coach_id, workout_id, date from
            fitness_club.schedule WHERE coach_id = ?""";
    private final String FIND_BY_CLIENT_ID = """
            SELECT id, client_id, coach_id, workout_id, date from
            fitness_club.schedule WHERE client_id = ?""";

    private final String DELETE_SQL = """
            delete from fitness_club.schedule where id = ?""";

    private final String UPDATE_SQL = """
            update fitness_club.schedule set client_id = ?, coach_id = ?,
             workout_id = ?, date = ? where id = ?""";

    private final String ADD_SQL = """
            insert into fitness_club.schedule (client_id, coach_id, workout_id, date)
            values(?, ?, ?, ?);""";
    private final String FIND_ALL_SQL = """
            select * from fitness_club.schedule""";


    public List<Schedule> findByCoachOrClientId(int id, boolean coach) {
        try (var connection = ConnectionManager.get();
             var statement = connection
                     .prepareStatement(coach ? FIND_BY_COACH_ID : FIND_BY_CLIENT_ID)) {
            statement.setObject(1, id);
            var rs = statement.executeQuery();
            logger.debug(statement.toString());
            var list = new ArrayList<Schedule>();
            while (rs.next()) {
                list.add(mapResultSetToEntity(rs));
            }
            return list;
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    public ScheduleRepository() {super(Schedule.class);}

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
