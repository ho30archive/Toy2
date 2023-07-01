package toy.baseball.management.util;

import toy.baseball.management.dto.TeamRespDTO;
import toy.baseball.management.model.Stadium;
import toy.baseball.management.model.Team;

public class CustomPrint {

    private static CustomPrint instance;

    public static CustomPrint getInstance() {
        if (instance == null) {
            instance = new CustomPrint();
        }
        return instance;
    }

    public void printTeam(Team team) {
        String id = String.valueOf(team.getId());
        String stadiumId = String.valueOf(team.getStadiumId());
        String name = team.getName();
        String createdAt = String.valueOf(team.getCreatedAt());

        System.out.print("|");
        printStr(id);
        printStr(stadiumId);
        printStr(name);
        printStr(createdAt);
        System.out.println("     |");
        System.out.println("| ------------------------------------------------------------------------------------------------------------------ |");
    }
    public void printTeamStadium(TeamRespDTO teamRespDTO) {
        String teamid = String.valueOf(teamRespDTO.getTeamId());
        String teamName = teamRespDTO.getTeamName();
        String teamCreatedAt = String.valueOf(teamRespDTO.getTeamCreatedAt());
        String stadiumId = String.valueOf(teamRespDTO.getStadiumId());
        String stadiumName = teamRespDTO.getStadiumName();
        String stadiumCreatedAt = String.valueOf(teamRespDTO.getStadiumCreatedAt());

        System.out.print("|");
        printStr(teamid);
        printStr(teamName);
        printStr(teamCreatedAt);
        printStr(stadiumId);
        printStr(stadiumName);
        printStr(stadiumCreatedAt);

        System.out.println("     |");
        System.out.println("| -------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |");

    }

    public void printStadium(Stadium stadium) {
        String id = String.valueOf(stadium.getId());
        String name = stadium.getName();
        String createdAt = String.valueOf(stadium.getCreatedAt());

        System.out.print("|");
        printStr(id);
        printStr(name);
        printStr(createdAt);

        System.out.println("     |");
        System.out.println("| -------------------------------------------------------------------------------------- |");
    }

    public void printStr(String str) {
        int i = 21 - String.valueOf(str).length();
        System.out.print("\t   " + str);
        for (int j = 0; j < i; j++) {
            System.out.print(" ");
        }
    }
}
