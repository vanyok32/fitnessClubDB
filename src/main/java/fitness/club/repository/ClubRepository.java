package fitness.club.repository;

import fitness.club.entity.Club;

public class ClubRepository extends BaseRepositoryImpl<Club, Integer> {

    private final String ADD_SQL = """
    insert into fitness_club.club (address, name)
            values (?, ?);""";
    private final String DELETE_SQL = """
    Delete from fitness_club.club where id = ?;""";
    private final String UPDATE_SQL = """
    update fitness_club.club set address=?, name=? where id = ?""";
    private final String FIND_ALL_SQL = """
            select * from fitness_club.club""";
    private final String FIND_BY_ID_SQL = """
            select id, address, name from fitness_club.club where id = ?""";

    public ClubRepository() {super(Club.class);}

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
