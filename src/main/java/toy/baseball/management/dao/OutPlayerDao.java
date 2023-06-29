package toy.baseball.management.dao;

import toy.baseball.management.model.OutPlayer;
import toy.baseball.management.model.Stadium;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OutPlayerDao {

    private Connection connection;

    public OutPlayerDao(Connection connection) {
        this.connection = connection;
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

    public void registerOutPlayer(int playerId, String reason) {
        LocalDateTime currentDateTime = LocalDateTime.now(); // Get the current date and time

        // 1. sql
        String query = "insert into out_player_tb values (?, ?, ?, ?)";

        // 2. buffer
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, 0);
            statement.setInt(2, playerId);
            statement.setString(3, reason);
            statement.setTimestamp(4, Timestamp.valueOf(currentDateTime));

            statement.executeUpdate();
            System.out.println(playerId + "번 선수 퇴출. 사유 : " + reason + " 등록 완료!");
        } catch (Exception e) {
            System.out.println("등록 실패!= " + e.getMessage());
        }
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
