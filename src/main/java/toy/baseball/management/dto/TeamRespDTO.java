package toy.baseball.management.dto;


import lombok.Getter;

import java.sql.Timestamp;

@Getter
public class TeamRespDTO {
    private int teamId;
    private String teamName;
    private Timestamp teamCreatedAt;

    private int stadiumId;
    private String stadiumName;
    private Timestamp stadiumCreatedAt;

    public TeamRespDTO(int teamId, String teamName, Timestamp teamCreatedAt, int stadiumId, String stadiumName, Timestamp stadiumCreatedAt) {
        this.teamId = teamId;
        this.teamName = teamName;
        this.teamCreatedAt = teamCreatedAt;
        this.stadiumId = stadiumId;
        this.stadiumName = stadiumName;
        this.stadiumCreatedAt = stadiumCreatedAt;
    }

    @Override
    public String toString() {
        return "TeamRespDTO{" +
                "teamId=" + teamId +
                ", teamName='" + teamName + '\'' +
                ", teamCreatedAt=" + teamCreatedAt +
                ", stadiumId=" + stadiumId +
                ", stadiumName='" + stadiumName + '\'' +
                ", stadiumCreatedAt=" + stadiumCreatedAt +
                '}';
    }
}
