package toy.baseball.management.dao;

import toy.baseball.management.dto.OutPlayerRespDTO;
import toy.baseball.management.exception.StadiumException;
import toy.baseball.management.model.OutPlayer;
import toy.baseball.management.model.Player;
import toy.baseball.management.model.Stadium;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OutPlayerDao {

    private Connection connection;

    private static OutPlayerDao instance;

    public OutPlayerDao(Connection connection) {
        this.connection = connection;
    }

    public static OutPlayerDao getInstance(Connection connection) {
        if (instance == null) {
            instance = new OutPlayerDao(connection);
        }
        return instance;
    }

    public List<OutPlayer> findAllOutPlayer() {
        // 0. collection
        List<OutPlayer> OutPlayerList = new ArrayList<>();
        // 1. sql
        String query = "select * from out_player_tb";

        try{
            PreparedStatement statement = connection.prepareStatement(query);

            // 3. send
            ResultSet rs = statement.executeQuery();

            // 4. cursor while
            while (rs.next()) {
                // 5. mapping(parsing) (db result -> model)
                OutPlayer outPlayer = new OutPlayer(
                        rs.getInt("id"),
                        rs.getInt("player_id"),
                        rs.getString("reason"),
                        rs.getTimestamp("created_at")
                );

                // 5. collect
                OutPlayerList.add(outPlayer);
            }
            return OutPlayerList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public OutPlayerRespDTO registerOutPlayer(int playerId, String reason) {
        LocalDateTime currentDateTime = LocalDateTime.now(); // Get the current date and time

        // 1. sql
        String insertQuery = "insert into out_player_tb values (?, ?, ?, ?)";
        String updateQuery = "update player_tb set team_id = null where id = ?";

        // 2. buffer
        try {
            connection.setAutoCommit(false); // 트랜젝션 시작

            PreparedStatement statement = connection.prepareStatement(insertQuery);
            PreparedStatement statement2 = connection.prepareStatement(updateQuery);

            statement.setInt(1, 0);
            statement.setInt(2, playerId);
            statement.setString(3, reason);
            statement.setTimestamp(4, Timestamp.valueOf(currentDateTime));

            statement2.setInt(1, playerId);

            int i = statement.executeUpdate();
            int j = statement2.executeUpdate();
            if (i == 1 && j == 1) {
                connection.commit();
                return findByPlayerId(playerId);
            }
            else return null;

        } catch (SQLIntegrityConstraintViolationException e) {
            throw new StadiumException();
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException exception) {
                    throw new RuntimeException();
                }
            }
        }
        return null;
    }

    public OutPlayerRespDTO findByPlayerId(int playerId) {
        // 1. sql
        String query = "select p.id, p.name, p.position, o.reason, o.created_at " +
                "from out_player_tb o " +
                "join player_tb p on o.player_id = p.id " +
                "where o.player_id = ?";

        // 2. buffer
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, playerId);

            // 3. send
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                OutPlayerRespDTO outPlayer = new OutPlayerRespDTO(
                        rs.getInt("p.id"),
                        rs.getString("p.name"),
                        rs.getString("p.position"),
                        rs.getString("o.reason"),
                        rs.getTimestamp("o.created_at")
                );
                return outPlayer;
            }

            // 4. mapping(parsing) (db result -> model)

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public void updateOutPlayerReason(int playerId, String reason) {
        String updateQuery = "update out_player_tb set reason = ? where player_id = ?";
        String selectQuery = "select player_tb.name, out_player_tb.reason " +
                "from out_player_tb " +
                "join player_tb on out_player_tb.player_id = player_tb.id " +
                "where out_player_tb.player_id = ?";

        try {
            String beforeReason = null;
            String playerName = null;
            PreparedStatement updatePstmt = connection.prepareStatement(updateQuery);
            PreparedStatement selectPstmt = connection.prepareStatement(selectQuery);

            updatePstmt.setString(1, reason);
            updatePstmt.setInt(2, playerId);
            selectPstmt.setInt(1, playerId);

            ResultSet rs = selectPstmt.executeQuery();
            if (rs.next()) {
                playerName = rs.getString("name");
                beforeReason = rs.getString("reason");
            }

            updatePstmt.executeUpdate();
            System.out.println(playerName + " 선수 퇴출 이유 수정완료! " + beforeReason + " -> " + reason);
        } catch (Exception e) {
            System.out.println("수정 실패!= " + e.getMessage());
        }

    }

    public void deleteOutPlayer(int playerId) {
        String deleteQuery = "delete from out_player_tb where player_id = ?";
        String selectQuery = "select name from player_tb where id = ?";

        // 2. buffer
        try {
            String name = null;
            PreparedStatement deletePstmt = connection.prepareStatement(deleteQuery);
            PreparedStatement selectPstmt = connection.prepareStatement(selectQuery);

            deletePstmt.setInt(1, playerId);
            selectPstmt.setInt(1, playerId);

            ResultSet rs = selectPstmt.executeQuery();
            if (rs.next()) {
                name = rs.getString("name");
            }

            deletePstmt.executeUpdate();
            System.out.println(name + " 선수 퇴출 목록에서 삭제 완료!");

        } catch (Exception e) {
            System.out.println("삭제 실패!= " + e.getMessage());
        }
    }




}
