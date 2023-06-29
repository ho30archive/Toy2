package toy.baseball.management.model;

import java.sql.Timestamp;

public class Team {
    private int id;
    private int stadiumId; // fk
    private String name;
    private Timestamp createdAt;

    public Team(int id, int stadiumId, String name, Timestamp createdAt) {
        this.id = id;
        this.stadiumId = stadiumId;
        this.name = name;
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Team{" +
                "id=" + id +
                ", stadiumId=" + stadiumId +
                ", name='" + name + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}


