package toy.baseball.management.model;

import java.sql.Timestamp;

public class OutPlayer {
    private int id;
    private int playerId; //fk
    private String reason;
    private Timestamp createdAt;

    public OutPlayer(int id, int playerId, String reason, Timestamp createdAt) {
        this.id = id;
        this.playerId = playerId;
        this.reason = reason;
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "OutPlayer{" +
                "id=" + id +
                ", playerId=" + playerId +
                ", reason='" + reason + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
