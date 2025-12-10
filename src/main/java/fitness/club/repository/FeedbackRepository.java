package fitness.club.repository;

import fitness.club.entity.Coach;
import fitness.club.entity.Feedback;
import fitness.club.exeptions.RepositoryException;
import fitness.club.util.ConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FeedbackRepository extends BaseRepositoryWithoutId<Feedback, Integer> {

    private final Logger logger = LoggerFactory.getLogger(FeedbackRepository.class);

    private final String ADD_SQL = """
            INSERT INTO fitness_club.feedback (schedule_id, rating, comment) VALUES(?, ?, ?)""";
    private final String SELECT_ALL_SQL = """
            SELECT schedule_id, rating, comment FROM fitness_club.feedback""";
    private final String SELECT_BY_ID_SQL = """
            select schedule_id, rating, comment FROM fitness_club.feedback
            where schedule_id = ?""";
    private final String DELETE_SQL = """
            DELETE FROM fitness_club.feedback WHERE schedule_id = ?""";
    private final String UPDATE_SQL = """
            UPDATE fitness_club.feedback SET rating = ?, comment = ?
             WHERE schedule_id = ?""";
    private final String FIND_BY_CLIENT_ID = """
            SELECT f.* FROM fitness_club.feedback f
            JOIN fitness_club.schedule s ON f.schedule_id = s.id
            WHERE s.client_id = ?""";
    private final String FIND_BY_COACH_ID = """
            SELECT f.* FROM fitness_club.feedback f
            JOIN fitness_club.schedule s ON f.schedule_id = s.id
            WHERE s.coach_id = ?""";


    public List<Feedback> findByClientOrCoachId(Integer id, boolean coach){
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(coach ? FIND_BY_COACH_ID : FIND_BY_CLIENT_ID)) {
            statement.setObject(1, id);
            logger.debug(statement.toString());
            var rs = statement.executeQuery();
            var list = new ArrayList<Feedback>();
            while (rs.next()) {
                list.add(mapResultSetToEntity(rs));
            }
            return list;
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    public FeedbackRepository() {
        super(Feedback.class);
    }
    @Override
    protected String getSelectAllSQL() {return SELECT_ALL_SQL;}

    @Override
    protected String getSelectByIdSQL() {return SELECT_BY_ID_SQL;}


    @Override
    protected String getInsertSQL() {return ADD_SQL;}

    @Override
    protected String getUpdateSQL() {return UPDATE_SQL;}

    @Override
    protected String getDeleteByIdSQL() {return DELETE_SQL;}

    @Override
    protected String getPkFieldName() {return "schedule_id";}
}
