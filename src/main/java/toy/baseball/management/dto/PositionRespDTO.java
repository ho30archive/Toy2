package toy.baseball.management.dto;

import lombok.Getter;

@Getter
public class PositionRespDTO {

    private String teamName;
    private String playerName;
    private String playerPosition;

    public PositionRespDTO(String teamName, String playerName, String playerPosition) {
        this.teamName = teamName;
        this.playerName = playerName;
        this.playerPosition = playerPosition;
    }

    @Override
    public String toString() {
        return "PositionRespDTO{" +
                "teamName='" + teamName + '\'' +
                ", playerName='" + playerName + '\'' +
                ", playerPosition='" + playerPosition + '\'' +
                '}';
    }
}
