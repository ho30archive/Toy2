package toy.baseball.management;

import toy.baseball.management.dao.OutPlayerDao;
import toy.baseball.management.dao.PlayerDao;
import toy.baseball.management.dao.StadiumDao;
import toy.baseball.management.dao.TeamDao;
import toy.baseball.management.db.DBConnection;
import toy.baseball.management.enums.Positions;
import toy.baseball.management.model.OutPlayer;
import toy.baseball.management.model.Player;
import toy.baseball.management.model.Team;
import toy.baseball.management.service.StadiumService;
import toy.baseball.management.service.TeamService;

import java.net.ConnectException;
import java.net.SocketException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static toy.baseball.management.enums.Positions.*;


public class ManagementApplication {
	private static final StadiumService stadiumService = StadiumService.getInstance();
	private static final TeamService teamService = TeamService.getInstance();

	public static void main(String[] args) {

			stadiumService.getStadiumList();


//		stadiumService.registerStadium("맹구스타디움");
//		stadiumService.registerStadium("유리스타디움");
//		stadiumService.registerStadium("짱구스타디움");
//		stadiumService.registerStadium("고척");
//		stadiumService.deleteStadiumById(4);
//		stadiumService.updateStadiumName(5, "고척돔");
//
//		teamService.registerTeam(1, "맹구팀");
//		teamService.registerTeam(3, "짱구팀");

//		teamService.getTeamStadiumList();
//		teamService.deleteTeam(9);
//		teamService.registerTeam(2, "유리팀2");
//		teamService.deleteTeamById(24);
		teamService.getTeamList();
//		teamService.updateTeamName(22, "짱구팀");
//		teamService.registerTeam(3, "철수팀");
		teamService.updateTeamStadiumId(25, 10);
	}

}
