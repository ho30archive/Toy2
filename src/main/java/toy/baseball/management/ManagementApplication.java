package toy.baseball.management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import toy.baseball.management.db.DBConnection;

import java.sql.Connection;

public class ManagementApplication {
	public static void main(String[] args) {
		Connection connection = DBConnection.getInstance();

	}

}
