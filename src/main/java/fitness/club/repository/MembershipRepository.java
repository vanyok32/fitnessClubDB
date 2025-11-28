package fitness.club.repository;

import fitness.club.entity.Membership;
import fitness.club.exeptions.RepositoryException;
import fitness.club.util.ConnectionManager;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class MembershipRepository extends BaseRepositoryWithoutId<Membership, Integer> {
    private final String ADD_SQL = """
            INSERT INTO fitness_club.membership (client_id, start_date, end_date, is_active) 
            VALUES (?, ?, ?, ?)""";
    private final String DELETE_SQL = """
            DELETE FROM fitness_club.membership WHERE client_id = ?""";
    private final String UPDATE_SQL = """
            UPDATE fitness_club.membership SET start_date = ?, end_date = ?,
            is_active = ? WHERE client_id = ?""";
    private final String FIND_BY_CLIENT_ID_SQL = """
            SELECT client_id, start_date, end_date, is_active
            FROM fitness_club.membership WHERE client_id = ?""";
    private final String FIND_ALL_SQL = """
            SELECT client_id, start_date, end_date, is_active
            FROM fitness_club.membership""";

    public Membership activate(Membership membership) {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(UPDATE_SQL)) {
            membership.setActive(true);
            membership.setStartDate(Date.valueOf(LocalDate.now()));
            membership.setEndDate(Date.valueOf(LocalDate.now().plusMonths(1)));
            setStatementParametersForUpdate(statement, membership);
            int rows = statement.executeUpdate();
            if (rows == 0) {
                throw new RuntimeException("Entity not found for update: " + membership);
            }
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
        return membership;
    }

    public MembershipRepository() {super(Membership.class);}

    @Override
    protected String getSelectAllSQL() {return FIND_ALL_SQL;}

    @Override
    protected String getSelectByIdSQL() {return FIND_BY_CLIENT_ID_SQL;}

    @Override
    protected String getInsertSQL() {return ADD_SQL;}

    @Override
    protected String getUpdateSQL() {return UPDATE_SQL;}

    @Override
    protected String getDeleteByIdSQL() {return DELETE_SQL;}

    @Override
    protected String getPkFieldName() {return "client_id";}
}
