package fitness.club.repository;

import fitness.club.entity.Feedback;

public class FeedbackRepository extends BaseRepositoryWithoutId<Feedback, Integer> {

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
