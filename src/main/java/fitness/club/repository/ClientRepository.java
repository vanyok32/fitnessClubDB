package fitness.club.repository;

import fitness.club.entity.Client;
import fitness.club.exeptions.RepositoryException;
import fitness.club.util.ConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClientRepository extends BaseRepositoryImpl<Client, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(ClientRepository.class);

    private final String ADD_SQL = """
            insert into fitness_club.client (name, club_id, email) 
            values (?, ?, ?);
            """;

    private final String GET_BY_ID_SQL = """
            select id, name, email, club_id from
            fitness_club.client where id = ?;""";

    private final String DELETE_SQL = """
            delete from fitness_club.client
            where id = ?""";

    private final String UPDATE_SQL = """
            update fitness_club.client set name=?, email=?,
             club_id=? where id = ?""";

    private final String FIND_ALL_SQL = """
            select * from fitness_club.client""";

    private final String FIND_BY_CLUB_ID = """
            select * from fitness_club.client where club_id = ?""";

    public List<Client> findClientsByClubId(Integer clubId) {
        try (var connection = ConnectionManager.get();
            var statement = connection.prepareStatement(FIND_BY_CLUB_ID);
            var rs = statement.executeQuery()) {
            logger.debug(statement.toString());
            var list = new ArrayList<Client>();
            while (rs.next()) {
                list.add(mapResultSetToEntity(rs));
            }
            return list;
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    public ClientRepository() {super(Client.class);}

    @Override
    protected String getSelectAllSQL() {return FIND_ALL_SQL;}

    @Override
    protected String getSelectByIdSQL() {return GET_BY_ID_SQL;}

    @Override
    protected String getInsertSQL() {return ADD_SQL;}

    @Override
    protected String getUpdateSQL() {return UPDATE_SQL;}

    @Override
    protected String getDeleteByIdSQL() {return DELETE_SQL;}
}
