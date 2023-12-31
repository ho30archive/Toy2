package toy.baseball.management.service;

import toy.baseball.management.dao.PlayerDao;
import toy.baseball.management.dao.TeamDao;
import toy.baseball.management.db.DBConnection;
import toy.baseball.management.dto.PositionRespDTO;
import toy.baseball.management.enums.Positions;
import toy.baseball.management.exception.StadiumException;
import toy.baseball.management.model.Player;
import toy.baseball.management.model.Stadium;
import toy.baseball.management.model.Team;
import toy.baseball.management.util.CustomPrint;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class PlayerService {

    static final Connection connection = DBConnection.getInstance();
    static final PlayerDao playerDao = PlayerDao.getInstance(connection);
    static final TeamDao teamDao = TeamDao.getInstance(connection);
    private static final PlayerService instance = new PlayerService();

    private static final CustomPrint customPrint = CustomPrint.getInstance();

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


    public void updatePlayerName(int playerId, String name) {
        try {
            Player player = playerDao.updatePlayerName(playerId, name);
            if (player == null) {
                throw new RuntimeException();
            } else {
                System.out.println(name + "으로 이름 수정 완료!");
                System.out.println("| ---- id ------------------------ teamId -------------------- name ---------------------- position ------------------ createdAt --------------- |");
                customPrint.printPlayer(player);
            }
        } catch (StadiumException e) {
            System.out.println("선수 수정 실패!");
        } catch (RuntimeException e) {
            System.out.println("선수 수정 실패! 아이디를 확인해주세요.");
        }
    }

    public void updatePlayerTeamId(int playerId, int teamId) {
        try {
            Player player = playerDao.updatePlayerTeamId(playerId, teamId);
            if (player == null) {
                throw new RuntimeException();
            } else {
                System.out.println(teamId + "번으로 팀 번호 수정 완료!");
                System.out.println("| ---- id ------------------------ teamId -------------------- name ---------------------- position ------------------ createdAt --------------- |");
                customPrint.printPlayer(player);
            }
        } catch (StadiumException e) {
            System.out.println("선수 수정 실패! 팀 번호나 포지션 중복여부를 확인해주세요.");
        } catch (RuntimeException e) {
            System.out.println("선수 수정 실패! 아이디를 확인해주세요.");
        }
    }

    public void updatePlayerPosition(int playerId, String position) {
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
                Player player = playerDao.updatePlayerPosition(playerId, position);
                if (player == null) {
                    throw new RuntimeException();
                }
                System.out.println(position + " 으로 포지션 수정 완료!");
                System.out.println("| ---- id ------------------------ teamId -------------------- name ---------------------- position ------------------ createdAt --------------- |");
                customPrint.printPlayer(player);
            } else throw new IllegalStateException();


        } catch (IllegalStateException e) {
            System.out.println("올바른 포지션을 입력해주세요.");
        } catch (NullPointerException e) {
            System.out.println("선수 수정 실패!");
        } catch (StadiumException e) {
            System.out.println("선수 수정 실패! 포지션이 중복되었습니다.");
        } catch (RuntimeException e) {
            System.out.println("선수 수정 실패! 아이디를 확인해주세요.");
        }

    }


    // TODO: 2023/07/02 ㅠㅠ 
    public void getPositionsPlayerByTeamList() {
        try {
            List<PositionRespDTO> positionRespDTOList = playerDao.findAllPositionsPlayerByTeam();
            List<Team> teamList = teamDao.findAllTeam();
            if (positionRespDTOList.size() != 0 && teamList.size() != 0) {

                System.out.println("포지션별 팀 야구 선수 페이지 조회 완료!");

                // 상단
                int t = 16;
                System.out.print("|      position        ");
                for (int i = 0; i < teamList.size(); i++) {
                    String teamname = teamList.get(i).getName();
                    System.out.print(teamname);
                    for (int j = 0; j < t-teamname.length(); j++) {
                        System.out.print(" ");
                    }
                }
                System.out.println("|");

                // 중단
                Positions[] enums = Positions.values();
                for (int i = 0; i < enums.length; i++) {
                    System.out.print("|      " + enums[i]);

                }



                for (PositionRespDTO positionRespDTO : positionRespDTOList) {
                    System.out.println(positionRespDTO);
                }




            } else {
                System.out.println("조회된 정보가 없습니다.");
            }
        } catch (RuntimeException e) {
            System.out.println("DB연결을 확인해주세요." + e.getMessage());
        }


    }



}
