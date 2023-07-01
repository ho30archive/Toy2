package toy.baseball.management.dao;

import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import toy.baseball.management.enums.Positions;
import toy.baseball.management.exception.StadiumException;
import toy.baseball.management.model.Player;
import toy.baseball.management.model.Stadium;

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

        public void deletePlayer(int id) {

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
            if (rs.next()) {
                name = rs.getString("name");
            }

            deletePstmt.executeUpdate();
            System.out.println("플레이어 " + name + " 삭제 완료!");

        } catch (Exception e) {
            System.out.println("삭제 실패!= " + e.getMessage());
        }

    }

    public void updatePlayerName(int id, String name) {
        String updateQuery = "update player_tb set name = ? where id = ?";
        String selectQuery = "select (name) from player_tb where id = ?";

        try {
            String beforeName = null;
            PreparedStatement updatePstmt = connection.prepareStatement(updateQuery);
            PreparedStatement selectPstmt = connection.prepareStatement(selectQuery);

            updatePstmt.setString(1, name);
            updatePstmt.setInt(2, id);
            selectPstmt.setInt(1, id);

            ResultSet rs = selectPstmt.executeQuery();
            if (rs.next()) {
                beforeName = rs.getString("name");
            }

            updatePstmt.executeUpdate();
            System.out.println("플레이어 이름 수정완료! " + beforeName + " -> " + name);
        } catch (Exception e) {
            System.out.println("수정 실패!= " + e.getMessage());
        }
    }

    public void updatePlayerTeamId(int playerId, int teamId) {
        String updateQuery = "update player_tb set team_id = ? where id = ?";
        String selectQuery = "SELECT player_tb.team_id, team_tb.name " +
                "FROM player_tb " +
                "JOIN team_tb ON player_tb.team_id = team_tb.id " +
                "where player_tb.id = ?";

        try {
            Integer beforeTeamId = null;
            String beforeSTeamName = null;
            Integer afterTeamId = null;
            String afterTeamName = null;

            PreparedStatement updatePstmt = connection.prepareStatement(updateQuery);
            PreparedStatement selectPstmt = connection.prepareStatement(selectQuery);
            PreparedStatement afterPstmt = connection.prepareStatement(selectQuery);

            updatePstmt.setInt(1, teamId);
            updatePstmt.setInt(2, playerId);
            selectPstmt.setInt(1, playerId);
            afterPstmt.setInt(1, playerId);

            ResultSet rs = selectPstmt.executeQuery();
            if (rs.next()) {
                beforeTeamId = rs.getInt("team_id");
                beforeSTeamName = rs.getString("name");
            }

            updatePstmt.executeUpdate();

            ResultSet rs2 = afterPstmt.executeQuery();
            if (rs2.next()) {
                afterTeamId = rs2.getInt("team_id");
                afterTeamName = rs2.getString("name");
            }

            System.out.println(playerId + "번 선수 팀 번호 수정완료! " + beforeTeamId + "(" + beforeSTeamName + ") -> " + afterTeamId +"(" + afterTeamName + ")");
        } catch (Exception e) {
            System.out.println("수정 실패!= " + e.getMessage());
        }

    }

    public void updatePlayerPosition(int id, Enum<Positions> position) {
        String updateQuery = "update player_tb set position = ? where id = ?";
        String selectQuery = "select position from player_tb where id = ?";

        try {
            String beforePosition = null;
            PreparedStatement updatePstmt = connection.prepareStatement(updateQuery);
            PreparedStatement selectPstmt = connection.prepareStatement(selectQuery);

            updatePstmt.setString(1, String.valueOf(position));
            updatePstmt.setInt(2, id);
            selectPstmt.setInt(1, id);

            ResultSet rs = selectPstmt.executeQuery();
            if (rs.next()) {
                beforePosition = rs.getString("position");
            }

            updatePstmt.executeUpdate();
            System.out.println(id + "번 선수 포지션 수정완료! " + beforePosition + " -> " + String.valueOf(position));
        } catch (Exception e) {
            System.out.println("수정 실패!= " + e.getMessage());
        }
    }

    // TODO: 2023/06/29 퇴출쿼리짜야됨 
    public void outPlayer() {
        
    }



}
