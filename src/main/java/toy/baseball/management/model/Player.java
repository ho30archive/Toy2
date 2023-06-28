package toy.baseball.management.model;

import java.sql.Timestamp;

public class Player {

    private int id;
    private int teamId; //fk
    private String name;
    private String position;
    private Timestamp createdAt;
}
