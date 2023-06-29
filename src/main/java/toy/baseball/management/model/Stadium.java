package toy.baseball.management.model;

import java.sql.Timestamp;

public class Stadium {
    private int id;
    private String name;
    private Timestamp createdAt;

    public Stadium(int id, String name, Timestamp createdAt) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
    }

    public Stadium(String name, Timestamp createdAt) {
        this.name = name;
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Stadium{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
