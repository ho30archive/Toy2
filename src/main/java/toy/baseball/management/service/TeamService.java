package toy.baseball.management.service;

import toy.baseball.management.dao.StadiumDao;
import toy.baseball.management.dao.TeamDao;
import toy.baseball.management.db.DBConnection;
import toy.baseball.management.dto.TeamRespDTO;
import toy.baseball.management.exception.StadiumException;
import toy.baseball.management.model.Team;
import toy.baseball.management.util.CustomPrint;

import javax.swing.*;
import java.sql.Connection;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

public class TeamService {
    static final Connection connection = DBConnection.getInstance();
    static final TeamDao teamDao = TeamDao.getInstance(connection);
    private static final TeamService instance = new TeamService();
    private static final CustomPrint customPrint = CustomPrint.getInstance();


    private TeamService() {
    }

    public static TeamService getInstance() {
        return instance;
    }

    public void getTeamList() {
        try {
            List<Team> teamList = teamDao.findAllTeam();
            if (teamList.size() != 0) {
                System.out.println("팀 조회 완료!");
                System.out.println("| ---- id ------------------------ stadiumId ----------------- name ---------------------- createdAt --------------- |");
                for (Team team : teamList) {
                    customPrint.printTeam(team);
                }
            } else {
                System.out.println("등록된 팀이 없습니다.");
            }
        } catch (RuntimeException e) {
            System.out.println("DB연결을 확인해주세요." + e.getMessage());
        }
    }

    public void getTeamStadiumList() {
        try {
            List<TeamRespDTO> teamRespDTOList = teamDao.findTeamStadium();
            if (teamRespDTOList.size() != 0) {
                System.out.println("팀 & 야구장 조회 완료!");
                System.out.println("| ---- teamId -------------------- teamName ------------------ teamCreatedAt ------------- stadiumId ----------------- stadiumName --------------- stadiumCreatedAt -------- |");
                for (TeamRespDTO teamRespDTO : teamRespDTOList) {
                    customPrint.printTeamStadium(teamRespDTO);
                }
            } else {
                System.out.println("등록된 팀이나 야구장이 없습니다.");
            }
        } catch (RuntimeException e) {
            System.out.println("DB연결을 확인해주세요." + e.getMessage());
        }


    }

    public void registerTeam(int stadiumId, String name) {
        try {
            Team team = teamDao.registerTeam(stadiumId, name);
            if (team == null) {
                throw new NullPointerException();
            }
            System.out.println("팀 " + name + " 등록완료!");
            System.out.println("| ---- id ------------------------ stadiumId ----------------- name ---------------------- createdAt --------------- |");
            customPrint.printTeam(team);
        } catch (NullPointerException e) {
            System.out.println("등록 실패! = " + e.getMessage());
        } catch (StadiumException e) {
            System.out.println("등록 실패! " + name + "이(가) 이미 존재합니다.");
        } catch (RuntimeException e) {
            System.out.println("DB연결을 확인해주세요." + e.getMessage());
        }

    }

    public void deleteTeamById(int teamId) {
        try {
            Boolean check = teamDao.deleteTeam(teamId);
            if (check) {
                System.out.println(teamId + "번 팀 삭제 성공");
            } else {
                System.out.println("삭제 실패");
            }
        } catch (NullPointerException e) {
            System.out.println("삭제 실패! " + teamId + "번 아이디의 팀은 존재 하지 않습니다.");
            System.out.println("팀 아이디를 확인해 주세요.");
        } catch (StadiumException e) {
            System.out.println("삭제 실패! 무결성 제약조건에 위배됩니다. 관련 데이터를 먼저 삭제해 주세요.");
        } catch (RuntimeException e) {
            System.out.println("DB연결을 확인해주세요." + e.getMessage());
        }
    }

    public void updateTeamName(int teamId, String name) {
        try {
            Team team = teamDao.updateTeamName(teamId, name);
            if (team == null) {
                throw new RuntimeException();
            } else {
                System.out.println(name + "으로 이름 수정 완료!");
                System.out.println("| ---- id ------------------------ stadiumId ----------------- name ---------------------- createdAt --------------- |");
                customPrint.printTeam(team);
            }
        } catch (StadiumException e) {
            System.out.println("수정 실패! 중복되는 팀 이름 입니다.");
        } catch (RuntimeException e) {
            System.out.println("수정 실패! 아이디를 확인해주세요.");
        }
    }

    public void updateTeamStadiumId(int teamId, int stadiumId) {
        try {
            Team team = teamDao.updateTeamStadiumId(teamId, stadiumId);
            if (team == null) {
                throw new NullPointerException();
            } else {
                System.out.println(teamId + "번 팀 " + stadiumId + "번 야구장으로 변경 완료!");
                System.out.println("| ---- id ------------------------ stadiumId ----------------- name ---------------------- createdAt --------------- |");
                customPrint.printTeam(team);
            }
        } catch (StadiumException e) {
            System.out.println("수정 실패! 존재하지 않는 야구장 번호 입니다.");
        } catch (RuntimeException e) {
            System.out.println("수정 실패! 아이디를 확인해주세요.");
        }
    }
}
