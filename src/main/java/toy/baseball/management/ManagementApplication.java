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
import toy.baseball.management.service.OutPlayerService;
import toy.baseball.management.service.PlayerService;
import toy.baseball.management.service.StadiumService;
import toy.baseball.management.service.TeamService;
import toy.baseball.management.util.CheckValues;

import java.net.ConnectException;
import java.net.SocketException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

import static toy.baseball.management.enums.Positions.*;


public class ManagementApplication {
	private static final StadiumService stadiumService = StadiumService.getInstance();
	private static final TeamService teamService = TeamService.getInstance();
	private static final PlayerService playerService = PlayerService.getInstance();
	private static final OutPlayerService outPlayerService = OutPlayerService.getInstance();

	private static final CheckValues checkvalues = CheckValues.getInstance();

	public static void main(String[] args) {
		String manual =
				"1. 야구장목록 \n" +
						"2. 야구장등록?name=잠실야구장\n" +
						"3. 야구장이름수정?stadiumId=1&name\n" +
						"4. 야구장삭제?stadiumId=1\n" +
						"5. 팀목록\n" +
						"6. 팀과야구장목록\n" +
						"7. 팀등록?stadiumId=1&name=고척돔\n" +
						"8. 팀이름수정?teamId=1&name=고척돔\n" +
						"9. 팀야구장수정?teamId=1&stadiumId=1\n" +
						"10. 팀삭제?teamId=1\n" +
						"11. 선수목록\n" +
						"12. 팀선수목록?teamId=1\n" +
						"13. 선수등록?teamId=1&name=이대호&position=1루수\n" +
						"14. 선수이름수정?playerId=1&name=이대호\n" +
						"15. 선수팀수정?playerId=1&teamId=1\n" +
						"16. 선수포지션수정?playerId=1&position=1루수\n" +
						"17. 선수삭제?playerId=1\n" +
						"18. 퇴출목록\n" +
						"19. 퇴출등록?playerId=1&reason=도박\n" +
						"20. 퇴출사유수정?playerId=1&reason=폭력\n" +
						"21. 포지션별목록";

		Scanner sc = new Scanner(System.in);
		String str;
		StringTokenizer st;
		String order;
		String value;

		String name;
		String position;
		String reason;
		Integer stadiumId;
		Integer teamId;
		Integer playerId;

		while(true){
			System.out.println("\n어떤 기능을 요청하시겠습니까?");
			System.out.println("[매뉴얼] 입력시 사용 예시 확인 가능");
			str = sc.nextLine();
			System.out.println();

			st = new StringTokenizer(str, "?");
			order = st.nextToken();
			value = null;
			if (st.hasMoreTokens()){
				value = st.nextToken();
			}

			switch (order) {
				case "매뉴얼":
					System.out.println(manual);
					break;

				case "야구장목록":
					stadiumService.getStadiumList();
					break;

				case "야구장등록":
					name = checkvalues.checkName(value);
					if (name != null) {
						stadiumService.registerStadium(name);
					} else System.out.println("양식을 확인하여 다시 입력해주세요.");
					break;

				case "야구장이름수정":
					st = new StringTokenizer(value, "&");
					stadiumId = checkvalues.checkStadiumId(st.nextToken());
					name = checkvalues.checkName(st.nextToken());

					if (stadiumId != null && name != null) {
						stadiumService.updateStadiumName(stadiumId, name);
					} else System.out.println("양식을 확인하여 다시 입력해주세요.");
					break;

				case "야구장삭제":
					stadiumId = checkvalues.checkStadiumId(value);
					if (stadiumId != null) {
						stadiumService.deleteStadiumById(stadiumId);
					} else System.out.println("양식을 확인하여 다시 입력해주세요.");
					break;

				case "팀목록":
					teamService.getTeamList();
					break;

				case "팀과야구장목록":
					teamService.getTeamStadiumList();
					break;

				case "팀등록":
					st = new StringTokenizer(value, "&");
					stadiumId = checkvalues.checkStadiumId(st.nextToken());
					name = checkvalues.checkName(st.nextToken());

					if (stadiumId != null && name != null) {
						teamService.registerTeam(stadiumId, name);
					} else System.out.println("양식을 확인하여 다시 입력해주세요.");
					break;

				case "팀이름수정":
					st = new StringTokenizer(value, "&");
					teamId = checkvalues.checkTeamId(st.nextToken());
					name = checkvalues.checkName(st.nextToken());

					if (teamId != null && name != null) {
						teamService.updateTeamName(teamId, name);
					} else System.out.println("양식을 확인하여 다시 입력해주세요.");
					break;

				case "팀야구장수정":
					st = new StringTokenizer(value, "&");
					teamId = checkvalues.checkTeamId(st.nextToken());
					stadiumId = checkvalues.checkStadiumId(st.nextToken());

					if (teamId != null && stadiumId != null) {
						teamService.updateTeamStadiumId(teamId, stadiumId);
					} else System.out.println("양식을 확인하여 다시 입력해주세요.");
					break;

				case "팀삭제":
					teamId = checkvalues.checkTeamId(value);

					if (teamId != null) {
						teamService.deleteTeamById(teamId);
					} else System.out.println("양식을 확인하여 다시 입력해주세요.");
					break;

				case "선수목록":
					playerService.getPlayerList();
					break;

				case "팀선수목록":
					teamId = checkvalues.checkTeamId(value);

					if (teamId != null) {
						playerService.getPlayerListByTeamId(teamId);
					} else System.out.println("양식을 확인하여 다시 입력해주세요.");
					break;

				case "선수등록":
					st = new StringTokenizer(value, "&");
					teamId = checkvalues.checkTeamId(st.nextToken());
					name = checkvalues.checkName(st.nextToken());
					position = checkvalues.checkPosition(st.nextToken());

					if (teamId != null && name != null && position != null) {
						playerService.registerPlayer(teamId, name, position);
					} else System.out.println("양식을 확인하여 다시 입력해주세요.");
					break;

				case "선수이름수정":
					st = new StringTokenizer(value, "&");
					playerId = checkvalues.checkPlayerId(st.nextToken());
					name = checkvalues.checkName(st.nextToken());

					if (playerId != null && name != null) {
						playerService.updatePlayerName(playerId, name);
					} else System.out.println("양식을 확인하여 다시 입력해주세요.");
					break;

				case "선수팀수정":
					st = new StringTokenizer(value, "&");
					playerId = checkvalues.checkPlayerId(st.nextToken());
					teamId = checkvalues.checkTeamId(st.nextToken());

					if (playerId != null && teamId != null) {
						playerService.updatePlayerTeamId(playerId, teamId);
					} else System.out.println("양식을 확인하여 다시 입력해주세요.");
					break;

				case "선수포지션수정":
					st = new StringTokenizer(value, "&");
					playerId = checkvalues.checkPlayerId(st.nextToken());
					position = checkvalues.checkPosition(st.nextToken());

					if (playerId != null && position != null) {
						playerService.updatePlayerPosition(playerId, position);
					} else System.out.println("양식을 확인하여 다시 입력해주세요.");
					break;

				case "선수삭제":
					playerId = checkvalues.checkPlayerId(value);

					if (playerId != null) {
						playerService.deletedPlayerById(playerId);
					} else System.out.println("양식을 확인하여 다시 입력해주세요.");
					break;

				case "퇴출목록":
					outPlayerService.getOutPlayerList();
					break;

				case "퇴출등록":
					st = new StringTokenizer(value, "&");
					playerId = checkvalues.checkPlayerId(st.nextToken());
					reason = checkvalues.checkReason(st.nextToken());

					if (playerId != null && reason != null) {
						outPlayerService.registerOutPlayer(playerId, reason);
					} else System.out.println("양식을 확인하여 다시 입력해주세요.");
					break;

				case "퇴출사유수정":
					st = new StringTokenizer(value, "&");
					playerId = checkvalues.checkPlayerId(st.nextToken());
					reason = checkvalues.checkReason(st.nextToken());

					if (playerId != null && reason != null) {
						outPlayerService.updateOutPlayerReason(playerId, reason);
					} else System.out.println("양식을 확인하여 다시 입력해주세요.");
					break;


				case "포지션별목록":
					playerService.getPositionsPlayerByTeamList();
					break;

				default:
					System.out.println("양식을 확인하여 다시 입력해주세요.");
			}
		}
	}
}