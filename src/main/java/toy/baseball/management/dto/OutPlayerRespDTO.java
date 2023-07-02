package toy.baseball.management.dto;

import lombok.Getter;

import java.sql.Timestamp;

@Getter
public class OutPlayerRespDTO {
    private int playerId;
    private String playerName;
    private String playerPosition;

    private String outPlayerReason;
    private Timestamp outPlayercreatedAt;

    public OutPlayerRespDTO(int playerId, String playerName, String playerPosition, String outPlayerReason, Timestamp outPlayercreatedAt) {
        this.playerId = playerId;
        this.playerName = playerName;
        this.playerPosition = playerPosition;
        this.outPlayerReason = outPlayerReason;
        this.outPlayercreatedAt = outPlayercreatedAt;
    }

    @Override
    public String toString() {
        return "OutPlayerRespDTO{" +
                "playerId=" + playerId +
                ", playerName='" + playerName + '\'' +
                ", playerPosition='" + playerPosition + '\'' +
                ", outPlayerReason='" + outPlayerReason + '\'' +
                ", outPlayercreatedAt=" + outPlayercreatedAt +
                '}';
    }
}
