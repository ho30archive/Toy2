package toy.baseball.management.dao;

import ch.qos.logback.core.joran.spi.NoAutoStartUtil;
import toy.baseball.management.exception.StadiumException;
import toy.baseball.management.model.Stadium;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.DuplicateFormatFlagsException;
import java.util.List;
import java.util.Optional;

import static java.time.LocalTime.now;

public class StadiumDao {
    private Connection connection;
    private static StadiumDao instance;
    private StadiumDao(Connection connection) {
        this.connection = connection;
    }

    public static StadiumDao getInstance(Connection connection) {
        if (instance == null) {
            instance = new StadiumDao(connection);
        }
        return instance;
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

    public Stadium registerStadium(String name) throws SQLIntegrityConstraintViolationException {
        LocalDateTime currentDateTime = LocalDateTime.now(); // Get the current date and time
        // 1. sql
        String query = "insert into stadium_tb values (?, ?, ?)";

        // 2. buffer
        try {
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, 0);
            statement.setString(2, name);
            statement.setTimestamp(3, Timestamp.valueOf(currentDateTime));

            int count = statement.executeUpdate();
            if (count > 0) return findByName(name);
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new SQLIntegrityConstraintViolationException();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
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

    public Stadium findById(int stadiumId) {
        // 1. sql
        String query = "select * from stadium_tb where id = ?";

        // 2. buffer
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, stadiumId);

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


    public void deleteById(int id) {

        String deleteQuery = "delete from stadium_tb where id = ?";
        String selectQuery = "select (name) from stadium_tb where id = ?";


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
            } else throw new NullPointerException();

            deletePstmt.executeUpdate();
            System.out.println("스타디움 " + name + " 삭제 완료!");

        } catch (Exception e) {
            throw new NullPointerException();
//            System.out.println("삭제 실패!= " + e.getMessage());
        }

    }

    public Stadium updateStadiumName(int id, String name) {
        String updateQuery = "update stadium_tb set name = ? where id = ?";

        try {
            PreparedStatement updatePstmt = connection.prepareStatement(updateQuery);

            updatePstmt.setString(1, name);
            updatePstmt.setInt(2, id);

            updatePstmt.executeUpdate();
//            System.out.println(id + "번 스타디움 이름 수정완료! " + beforeName + " -> " + name);
            return findById(id);
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new StadiumException();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
