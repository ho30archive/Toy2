package toy.baseball.management.dao;

import toy.baseball.management.dto.TeamRespDTO;
import toy.baseball.management.exception.StadiumException;
import toy.baseball.management.model.Stadium;
import toy.baseball.management.model.Team;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

public class TeamDao {
    private Connection connection;
    private static TeamDao instance;

    public TeamDao(Connection connection) {
        this.connection = connection;
    }

    public static TeamDao getInstance(Connection connection) {
        if (instance == null) {
            instance = new TeamDao(connection);
        }
        return instance;
    }

    public List<Team> findAllTeam() {
        // 0. collection
        List<Team> teamList = new ArrayList<>();

        // 1. sql
        String query = "select * from team_tb";

        // 2. buffer
        try{
            PreparedStatement statement = connection.prepareStatement(query);

            // 3. send
            ResultSet rs = statement.executeQuery();

            // 4. cursor while
            while (rs.next()) {
                // 5. mapping(parsing) (db result -> model)
                Team team = new Team(
                        rs.getInt("id"),
                        rs.getInt("stadium_id"),
                        rs.getString("name"),
                        rs.getTimestamp("created_at")
                );

                // 5. collect
                teamList.add(team);
            }
            return teamList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<TeamRespDTO> findTeamStadium() {
        // 0. collection
        List<TeamRespDTO> teamStadiumList = new ArrayList<>();

        // 1. sql
        String query = "select t.id, t.name, t.created_at, s.id, s.name, s.created_at " +
                "from team_tb t " +
                "join stadium_tb s " +
                "on s.id = t.stadium_id";

        // 2. buffer
        try{
            PreparedStatement statement = connection.prepareStatement(query);

            // 3. send
            ResultSet rs = statement.executeQuery();

            // 4. cursor while
            while (rs.next()) {
                // 5. mapping(parsing) (db result -> model)
                TeamRespDTO teamRespDTO = new TeamRespDTO(
                        rs.getInt("t.id"),
                        rs.getString("t.name"),
                        rs.getTimestamp("t.created_at"),
                        rs.getInt("s.id"),
                        rs.getString("s.name"),
                        rs.getTimestamp("s.created_at")
                );

                // 5. collect
                teamStadiumList.add(teamRespDTO);
            }
            return teamStadiumList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    public Team registerTeam(int stadiumId, String name) {
        LocalDateTime currentDateTime = LocalDateTime.now(); // Get the current date and time

        // 1. sql
        String query = "insert into team_tb values (?, ?, ?, ?)";

        // 2. buffer
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, 0);
            statement.setInt(2, stadiumId);
            statement.setString(3, name);
            statement.setTimestamp(4, Timestamp.valueOf(currentDateTime));

            statement.executeUpdate();
            return findByName(name);
//            System.out.println("팀 "+ name + " 등록 완료!");
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new StadiumException();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Boolean deleteTeam(int id) {
        String deleteQuery = "delete from team_tb where id = ?";
        String selectQuery = "select (name) from team_tb where id = ?";

        // 2. buffer
        try {
            String name = null;
            PreparedStatement deletePstmt = connection.prepareStatement(deleteQuery);
            PreparedStatement selectPstmt = connection.prepareStatement(selectQuery);

            deletePstmt.setInt(1, id);
            selectPstmt.setInt(1, id);

            ResultSet rs = selectPstmt.executeQuery();
            if (!rs.next()) {
                throw new NullPointerException();
            } else {
                int i = deletePstmt.executeUpdate();
                if (i == 1) {
                    return true;
                } else return false;
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new StadiumException();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Team updateTeamName(int id, String name) {
        String updateQuery = "update team_tb set name = ? where id = ?";

        try {
            PreparedStatement updatePstmt = connection.prepareStatement(updateQuery);

            updatePstmt.setString(1, name);
            updatePstmt.setInt(2, id);

            int i = updatePstmt.executeUpdate();
            if (i == 1) {
                return findById(id);
            }
            else return null;
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new StadiumException();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Team updateTeamStadiumId(int teamId, int stadiumId) {
        String updateQuery = "update team_tb set stadium_id = ? where id = ?";

        try {
            PreparedStatement updatePstmt = connection.prepareStatement(updateQuery);

            updatePstmt.setInt(1, stadiumId);
            updatePstmt.setInt(2, teamId);

            int i = updatePstmt.executeUpdate();
            if (i == 1) {
                return findById(teamId);
            }
            else return null;
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new StadiumException();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public Team findByName(String teamName) {
        // 1. sql
        String query = "select * from team_tb where name = ?";

        // 2. buffer
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, teamName);

            // 3. send
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                Team team = new Team(
                        rs.getInt("id"),
                        rs.getInt("stadium_id"),
                        rs.getString("name"),
                        rs.getTimestamp("created_at")
                );
                return team;
            }

            // 4. mapping(parsing) (db result -> model)

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public Team findById(int teamId) {
        // 1. sql
        String query = "select * from team_tb where id = ?";

        // 2. buffer
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, teamId);

            // 3. send
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                Team team = new Team(
                        rs.getInt("id"),
                        rs.getInt("stadium_id"),
                        rs.getString("name"),
                        rs.getTimestamp("created_at")
                );
                return team;
            }

            // 4. mapping(parsing) (db result -> model)

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }


}
