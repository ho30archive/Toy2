package toy.baseball.management.db;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    public static Connection getInstance(){
// MySQL 연결 정보-호윤
        String url = "jdbc:mysql://localhost:3334/toy2";
        String username = "root";
        String password = "root1234";

        // MySQL 연결 정보-성민
//        String url = "jdbc:mysql://localhost:3334/toy2";
//        String username = "root";
//        String password = "root1234";


// JDBC 드라이버 로드
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, username,
                    password);
            System.out.println("디버그 : DB연결 성공");
            return connection;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
