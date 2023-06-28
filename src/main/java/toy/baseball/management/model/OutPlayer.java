package toy.baseball.management.model;

import java.sql.Timestamp;

public class OutPlayer {
    private int id;
    private int playerId; //fk
    private String reason;
    private Timestamp createdAt;
}
