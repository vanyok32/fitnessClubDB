package fitness.club.repository;

import fitness.club.entity.Workout;

public class WorkoutRepository extends BaseRepositoryImpl<Workout, Integer> {

    private static final String SELECT_ALL = "SELECT id, name, duration FROM fitness_club.workout";
    private static final String SELECT_BY_ID = "SELECT id, name, duration FROM fitness_club.workout WHERE id = ?";
    private static final String INSERT = "INSERT INTO fitness_club.workout (name, duration) VALUES (?, ?)";
    private static final String UPDATE = "UPDATE fitness_club.workout SET name = ?, duration = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM fitness_club.workout WHERE id = ?";

    public WorkoutRepository() {super(Workout.class);}
    @Override
    protected String getSelectAllSQL() {return SELECT_ALL;}
    @Override
    protected String getSelectByIdSQL() {return SELECT_BY_ID;}
    @Override
    protected String getInsertSQL() {return INSERT;}
    @Override
    protected String getUpdateSQL() {return UPDATE;}
    @Override
    protected String getDeleteByIdSQL() {return DELETE;}
}
