package toy.baseball.management.dao;

import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import toy.baseball.management.enums.Positions;
import toy.baseball.management.exception.StadiumException;
import toy.baseball.management.model.Player;
import toy.baseball.management.model.Stadium;
import toy.baseball.management.model.Team;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class PlayerDao {

    private Connection connection;

    private static PlayerDao instance;

    public PlayerDao(Connection connection) {
        this.connection = connection;
    }

    public static PlayerDao getInstance(Connection connection) {
        if (instance == null) {
            instance = new PlayerDao(connection);
        }
        return instance;
    }

    public List<Player> findAllPlayer() {
        // 0. collection
        List<Player> playerList = new ArrayList<>();
        // 1. sql
        String query = "select * from player_tb";

        // 2. buffer
        try{
            PreparedStatement statement = connection.prepareStatement(query);

            // 3. send
            ResultSet rs = statement.executeQuery();

            // 4. cursor while
            while (rs.next()) {
                // 5. mapping(parsing) (db result -> model)
                Player player = new Player(
                        rs.getInt("id"),
                        rs.getInt("team_id"),
                        rs.getString("name"),
                        rs.getString("position"),
                        rs.getTimestamp("created_at")
                );

                // 5. collect
                playerList.add(player);
            }
            return playerList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Player registerPlayer(int teamId, String name, String position) {
        LocalDateTime currentDateTime = LocalDateTime.now(); // Get the current date and time

        // 1. sql
        String query = "insert into player_tb values (?, ?, ?, ? , ?)";

        // 2. buffer
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, 0);
            statement.setInt(2, teamId);
            statement.setString(3, name);
            statement.setString(4, position);
            statement.setTimestamp(5, Timestamp.valueOf(currentDateTime));

            int i = statement.executeUpdate();
            if (i == 1) {
                return findByTeamIdPosition(teamId, position);
            }
            else return null;
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new StadiumException();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Player findByTeamIdPosition(int teamId, String position) {
        // 1. sql
        String query = "select * from player_tb where team_id = ? and position = ?";

        // 2. buffer
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, teamId);
            statement.setString(2, position);

            // 3. send
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                Player player = new Player(
                        rs.getInt("id"),
                        rs.getInt("team_id"),
                        rs.getString("name"),
                        rs.getString("position"),
                        rs.getTimestamp("created_at")
                );
                return player;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public List<Player> findByTeamId(int teamId) {
        List<Player> playerList = new ArrayList<>();
        // 1. sql
        String query = "select * from player_tb where team_id = ?";

        // 2. buffer
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, teamId);

            // 3. send
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Player player = new Player(
                        rs.getInt("id"),
                        rs.getInt("team_id"),
                        rs.getString("name"),
                        rs.getString("position"),
                        rs.getTimestamp("created_at")
                );
                playerList.add(player);
            }
            return playerList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

        public Boolean deletePlayer(int id) {

        String deleteQuery = "delete from player_tb where id = ?";
        String selectQuery = "select (name) from player_tb where id = ?";

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

    public Player updatePlayerName(int id, String name) {
        String updateQuery = "update player_tb set name = ? where id = ?";

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

    public Player findById(int playerId) {
        // 1. sql
        String query = "select * from player_tb where id = ?";

        // 2. buffer
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, playerId);

            // 3. send
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                Player player = new Player(
                        rs.getInt("id"),
                        rs.getInt("team_id"),
                        rs.getString("name"),
                        rs.getString("position"),
                        rs.getTimestamp("created_at")
                );
                return player;
            }

            // 4. mapping(parsing) (db result -> model)

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public Player updatePlayerTeamId(int playerId, int teamId) {
        String updateQuery = "update player_tb set team_id = ? where id = ?";

        try {
            PreparedStatement updatePstmt = connection.prepareStatement(updateQuery);

            updatePstmt.setInt(1, teamId);
            updatePstmt.setInt(2, playerId);

            int i = updatePstmt.executeUpdate();
            if (i == 1) {
                return findById(playerId);
            }
            else return null;

        } catch (SQLIntegrityConstraintViolationException e) {
            throw new StadiumException();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public Player updatePlayerPosition(int id, String position) {
        String updateQuery = "update player_tb set position = ? where id = ?";

        try {
            PreparedStatement updatePstmt = connection.prepareStatement(updateQuery);

            updatePstmt.setString(1, position);
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

}
