package fitness.club.repository;

import fitness.club.entity.Coach;
import fitness.club.exeptions.RepositoryException;
import fitness.club.util.ConnectionManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CoachRepository extends BaseRepositoryImpl<Coach, Integer> {
    private final String FIND_BY_ID_SQL = """
            select id, club_id, name, email from fitness_club.coach where id = ?""";
    private final String DELETE_SQL = """
            delete from fitness_club.coach where id = ?""";
    private final String UPDATE_SQL = """
            update fitness_club.coach set club_id=?, name=?, email = ? where id = ?""";
    private final String FIND_ALL_SQL = """
            select id, club_id, name, email from fitness_club.coach""";
    private final String ADD_SQL = """
            insert into fitness_club.coach (club_id, name, email)
                        values (?, ?,?);""";
    private final String FIND_COACHES_BY_SPEC_ID_CLUB_ID = """
            select distinct c.id, c.club_id, c.name, c.email from fitness_club.coach c
            join fitness_club.trainer_specialization ts on c.id = ts.coach_id
            where c.club_id = ? AND ts.spec_id = ?""";
    private final String FIND_COACHES_BY_CLUB= """
            select distinct id, club_id, name, email from fitness_club.coach
            where club_id = ?""";
    private final String FIND_COACHES_BY_SPEC_ID = """
            select distinct c.id, c.club_id, c.name, c.email from fitness_club.coach c
            join fitness_club.trainer_specialization ts on c.id = ts.coach_id
            where ts.spec_id = ?""";

    private final String WITH_FILTERS_SQL = """
            SELECT
                c.id,
                c.club_id,
                c.name,
                c.email,
                STRING_AGG(s.name, ', ' ORDER BY s.name) AS specializations -- Агрегация специализаций
            FROM fitness_club.coach c
            LEFT JOIN fitness_club.trainer_specialization ts ON c.id = ts.coach_id
            LEFT JOIN fitness_club.specialization s ON ts.spec_id = s.id
            WHERE ...
            GROUP BY c.id, c.club_id, c.name, c.email -- Группировка по основным полям
            ORDER BY
                CASE
                    WHEN ? = 'specialization' THEN STRING_AGG(s.name, ', ' ORDER BY s.name)
                    WHEN ? = 'club' THEN c.club_id::TEXT -- Приведение к строке для CASE
                    ELSE c.id::TEXT
                END;""";
    public CoachRepository() {super(Coach.class);}
    public List<Coach> findCoachesBySpecIdClubId(Integer specId, Integer clubId) {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(FIND_COACHES_BY_SPEC_ID_CLUB_ID)) {
            statement.setObject(1, clubId);
            statement.setObject(2, specId);
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

    public List<Coach> findCoachesBySpecId(Integer specId) {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(FIND_COACHES_BY_SPEC_ID)) {
            statement.setObject(1, specId);
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

    public List<Coach> findWithFilters(Integer clubId, Integer specId, String sortBy) {
        StringBuilder sql = new StringBuilder("""
        SELECT
            c.id, 
            c.name, 
            c.email, 
            c.club_id 
        FROM fitness_club.coach c
        WHERE 1=1
        """);

        List<Object> params = new ArrayList<>();

        // Фильтр по клубу
        if (clubId != null) {
            sql.append(" AND c.club_id = ?");
            params.add(clubId);
        }
        // Фильтр по специализации
        if (specId != null) {
            sql.append("""
             AND EXISTS (
                 SELECT 1 
                 FROM fitness_club.trainer_specialization ts 
                 WHERE ts.coach_id = c.id 
                   AND ts.spec_id = ?
             )
            """);
            params.add(specId);
        }

        // Сортировка
        sql.append(" ORDER BY ");
        if ("club".equalsIgnoreCase(sortBy)) {
            sql.append(" c.club_id NULLS LAST, c.name");
        } else if ("specialization".equalsIgnoreCase(sortBy)) {
            sql.append(" c.name");
        } else {
            sql.append(" c.id");
        }

        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                statement.setObject(i + 1, params.get(i));
            }

            var rs = statement.executeQuery();
            var list = new ArrayList<Coach>();
            while (rs.next()) {
                list.add(mapResultSetToEntity(rs));  // этот метод остаётся без изменений
            }
            return list;

        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
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
