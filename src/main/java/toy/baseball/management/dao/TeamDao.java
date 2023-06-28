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


}
