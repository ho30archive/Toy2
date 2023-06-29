package toy.baseball.management.dao;

import ch.qos.logback.core.joran.spi.NoAutoStartUtil;
import toy.baseball.management.model.Stadium;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static java.time.LocalTime.now;

public class StadiumDao {
    private Connection connection;

    public StadiumDao(Connection connection) {
        this.connection = connection;
    }

    public List<Stadium> findAllStadium(){
        // 0. collection
        List<Stadium> stadiumList = new ArrayList<>();
        // 1. sql
        String query = "select * from stadium_tb";

        // 2. buffer
        try{
            PreparedStatement statement = connection.prepareStatement(query);

            // 3. send
            ResultSet rs = statement.executeQuery();

            // 4. cursor while
            while (rs.next()) {
                // 5. mapping(parsing) (db result -> model)
                Stadium stadium = new Stadium(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getTimestamp("created_at")
                );

                // 5. collect
                stadiumList.add(stadium);
            }
            return stadiumList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void registerStadium(String name) {
        LocalDateTime currentDateTime = LocalDateTime.now(); // Get the current date and time


        // 1. sql
        String query = "insert into stadium_tb values (?, ?, ?)";

        // 2. buffer
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, 0);
            statement.setString(2, name);
            statement.setTimestamp(3, Timestamp.valueOf(currentDateTime));

            statement.executeUpdate();
            System.out.println("스타디움 "+ name + " 등록 완료!");
        } catch (Exception e) {
            System.out.println("등록 실패!= " + e.getMessage());
        }
    }

    // stadium one
    public Stadium findByName(String stadiumName) {
        // 1. sql
        String query = "select * from stadium_tb where name = ?";

        // 2. buffer
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, stadiumName);

            // 3. send
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                Stadium stadium = new Stadium(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getTimestamp("created_at")
                );
                return stadium;
            }

            // 4. mapping(parsing) (db result -> model)

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public void deleteStadium(int id) {

        String deleteQuery = "delete from stadium_tb where id = ?";
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

}
