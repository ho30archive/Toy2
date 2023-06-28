package toy.baseball.management.model;

import java.sql.Timestamp;

public class Team {
    private int id;
    private int stadiumId; // fk
    private String name;
    private Timestamp createdAt;
}
