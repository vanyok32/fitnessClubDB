package fitness.club.repository;

import fitness.club.entity.Membership;

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
