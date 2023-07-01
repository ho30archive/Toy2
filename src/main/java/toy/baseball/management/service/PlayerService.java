package toy.baseball.management.service;

import toy.baseball.management.dao.PlayerDao;
import toy.baseball.management.db.DBConnection;
import toy.baseball.management.enums.Positions;
import toy.baseball.management.exception.StadiumException;
import toy.baseball.management.model.Player;
import toy.baseball.management.model.Stadium;
import toy.baseball.management.util.CustomPrint;

import java.sql.Connection;
import java.util.List;

public class PlayerService {

    static final Connection connection = DBConnection.getInstance();
    static final PlayerDao playerDao = PlayerDao.getInstance(connection);
    private static final PlayerService instance = new PlayerService();

    CustomPrint customPrint = new CustomPrint();

    private PlayerService() {

    }

    public static PlayerService getInstance() {
        return instance;
    }

    public void getPlayerList() {
        try {
            List<Player> playerList = playerDao.findAllPlayer();
            if (playerList.size() != 0) {
                System.out.println("플레이어 조회 완료!");
                System.out.println("| ---- id ------------------------ teamId -------------------- name ---------------------- position ------------------ createdAt --------------- |");
                for (Player player : playerList) {
                    customPrint.printPlayer(player);
                }
            } else {
                System.out.println("등록된 팀이 없습니다.");
            }
        } catch (RuntimeException e) {
            System.out.println("DB연결을 확인해주세요." + e.getMessage());
        }
    }

    public void getPlayerListByTeamId(int teamId) {
        try {
            List<Player> playerList = playerDao.findByTeamId(teamId);
            if (playerList.size() != 0) {
                System.out.println(teamId + "번 팀 플레이어 조회 완료!");
                System.out.println("| ---- id ------------------------ name ---------------------- position ------------------ createdAt --------------- |");
                for (Player player : playerList) {
                    customPrint.printPlayerByTeamId(player);
                }
            } else {
                System.out.println("등록된 팀이 없습니다.");
            }
        } catch (RuntimeException e) {
            System.out.println("DB연결을 확인해주세요." + e.getMessage());
        }
    }

    public void registerPlayer(int teamId, String name, String position) {
        try {
            Boolean isContained = false;
            Positions[] enums = Positions.values();
            for (Positions p : enums) {
                if (p.getPositionName().equals(position)) {
                    isContained = true;
                    break;
                }
            }
            if (isContained) {
                Player player = playerDao.registerPlayer(teamId, name, position);
                if (player == null) {
                    throw new NullPointerException();
                }
                System.out.println("선수 " + name + " 등록완료!");
                System.out.println("| ---- id ------------------------ teamId -------------------- name ---------------------- position ------------------ createdAt --------------- |");
                customPrint.printPlayer(player);
            } else throw new IllegalStateException();


        } catch (IllegalStateException e) {
            System.out.println("올바른 포지션을 입력해주세요.");
        } catch (NullPointerException e) {
            System.out.println("선수 등록 실패!");
        } catch (StadiumException e) {
            System.out.println("선수 등록 실패! 팀 번호가 잘못되었거나 포지션이 중복되었습니다.");
        } catch (RuntimeException e) {
            System.out.println("DB연결을 확인해주세요." + e.getMessage());
        }

    }
}
