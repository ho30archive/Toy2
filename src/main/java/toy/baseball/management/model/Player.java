package toy.baseball.management.model;

import lombok.Getter;
import toy.baseball.management.enums.Positions;

import java.sql.Timestamp;
@Getter
public class Player {

    private int id;
    private int teamId; //fk
    private String name;
    private String position;
    private Timestamp createdAt;


    public Player(int id, int teamId, String name, String position, Timestamp createdAt) {
        this.id = id;
        this.teamId = teamId;
        this.name = name;
        this.position = position;
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", teamId=" + teamId +
                ", name='" + name + '\'' +
                ", position='" + position + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
