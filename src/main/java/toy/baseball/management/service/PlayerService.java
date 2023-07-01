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
                System.out.println("선수 리스트 조회 완료!");
                System.out.println("| ---- id ------------------------ teamId -------------------- name ---------------------- position ------------------ createdAt --------------- |");
                for (Player player : playerList) {
                    customPrint.printPlayer(player);
                }
            } else {
                System.out.println("등록된 선수가 없습니다.");
            }
        } catch (RuntimeException e) {
            System.out.println("DB연결을 확인해주세요." + e.getMessage());
        }
    }

    public void getPlayerListByTeamId(int teamId) {
        try {
            List<Player> playerList = playerDao.findByTeamId(teamId);
            if (playerList.size() != 0) {
                System.out.println(teamId + "번 팀 선수 리스트 조회 완료!");
                System.out.println("| ---- id ------------------------ name ---------------------- position ------------------ createdAt --------------- |");
                for (Player player : playerList) {
                    customPrint.printPlayerByTeamId(player);
                }
            } else {
                System.out.println("등록된 선수가 없습니다.");
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

    public void deletedPlayerById(int playerId) {
        try {
            Boolean check = playerDao.deletePlayer(playerId);
            if (check) {
                System.out.println(playerId + "번 선수 삭제 성공");
            } else {
                System.out.println("삭제 실패");
            }
        } catch (NullPointerException e) {
            System.out.println("선수 삭제 실패! " + playerId + "번의 선수는 존재 하지 않습니다.");
            System.out.println("선수 아이디를 확인해 주세요.");
        } catch (StadiumException e) {
            System.out.println("선수 삭제 실패! 무결성 제약조건에 위배됩니다. 관련 데이터를 먼저 삭제해 주세요.");
        } catch (RuntimeException e) {
            System.out.println("DB연결을 확인해주세요." + e.getMessage());
        }
    }


}
