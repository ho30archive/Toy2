package toy.baseball.management.dao;

import toy.baseball.management.model.Stadium;
import toy.baseball.management.model.Team;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

public class TeamDao {
    private Connection connection;

    public TeamDao(Connection connection) {
        this.connection = connection;
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



    public void registerTeam(int stadiumId, String name) {
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
            System.out.println("팀 "+ name + " 등록 완료!");
        } catch (Exception e) {
            System.out.println("등록 실패!= " + e.getMessage());
        }

    }

    public void deleteTeam(int id) {
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
            if (rs.next()) {
                name = rs.getString("name");
            }

            deletePstmt.executeUpdate();
            System.out.println("팀 " + name + " 삭제 완료!");

        } catch (Exception e) {
            System.out.println("삭제 실패!= " + e.getMessage());
        }
    }

    public void updateTeamName(int id, String name) {
        String updateQuery = "update team_tb set name = ? where id = ?";
        String selectQuery = "select (name) from team_tb where id = ?";


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
            System.out.println("팀 이름 수정완료! " + beforeName + " -> " + name);
        } catch (Exception e) {
            System.out.println("수정 실패!= " + e.getMessage());
        }
    }

    public void updateTeamStadiumId(int teamId, int stadiumId) {
        String updateQuery = "update team_tb set stadium_id = ? where id = ?";
        String selectQuery = "SELECT team_tb.stadium_id, stadium_tb.name FROM team_tb JOIN stadium_tb ON team_tb.stadium_id = stadium_tb.id where team_tb.id = ?";
        String afterQuery = "SELECT stadium_tb.name FROM team_tb JOIN stadium_tb ON team_tb.stadium_id = stadium_tb.id where team_tb.id = ?";

        try {
            Integer beforeStadiumId = null;
            String beforeStadiumName = null;
            String afterStadiumName = null;
            PreparedStatement updatePstmt = connection.prepareStatement(updateQuery);
            PreparedStatement selectPstmt = connection.prepareStatement(selectQuery);
            PreparedStatement afterPstmt = connection.prepareStatement(afterQuery);

            updatePstmt.setInt(1, stadiumId);
            updatePstmt.setInt(2, teamId);
            selectPstmt.setInt(1, teamId);
            afterPstmt.setInt(1, teamId);

            ResultSet rs = selectPstmt.executeQuery();
            if (rs.next()) {
                beforeStadiumId = rs.getInt("stadium_id");
                beforeStadiumName = rs.getString("name");
            }

            updatePstmt.executeUpdate();

            ResultSet rs2 = afterPstmt.executeQuery();
            if (rs2.next()) {
                afterStadiumName = rs2.getString("name");
            }

            System.out.println("팀 스타디움 번호 수정완료! " + beforeStadiumId + "(" + beforeStadiumName + ") -> " + stadiumId +"(" + afterStadiumName + ")");
        } catch (Exception e) {
            System.out.println("수정 실패!= " + e.getMessage());
        }

    }


}
