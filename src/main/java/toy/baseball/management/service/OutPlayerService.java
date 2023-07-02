package toy.baseball.management.service;

import toy.baseball.management.dao.OutPlayerDao;
import toy.baseball.management.db.DBConnection;
import toy.baseball.management.dto.OutPlayerRespDTO;
import toy.baseball.management.exception.StadiumException;
import toy.baseball.management.util.CustomPrint;

import java.sql.Connection;
import java.util.List;

public class OutPlayerService {

    static final Connection connection = DBConnection.getInstance();
    static final OutPlayerDao outPlayerDao = OutPlayerDao.getInstance(connection);
    private static final OutPlayerService instance = new OutPlayerService();
    private static final CustomPrint customPrint = CustomPrint.getInstance();

    private OutPlayerService() {

    }

    public static OutPlayerService getInstance() {
        return instance;
    }

    public void getOutPlayerList() {
        try {
            List<OutPlayerRespDTO> outPlayerRespDTOList = outPlayerDao.findAllOutPlayer();
            if (outPlayerRespDTOList.size() != 0) {
                System.out.println("퇴출 선수 리스트 조회 완료!");
                System.out.println("| ---- p.id ---------------------- p.name -------------------- p.position ---------------- o.reason ------------------ o.createdAt ------------- |");
                for (OutPlayerRespDTO outPlayerRespDTO : outPlayerRespDTOList) {
                    customPrint.printOutPlayer(outPlayerRespDTO);
                }
            } else {
                System.out.println("퇴출된 선수가 없습니다.");
            }
        } catch (RuntimeException e) {
            System.out.println("DB연결을 확인해주세요." + e.getMessage());
        }
    }


    public void registerOutPlayer(int outPlayerId, String reason) {
        try {
            OutPlayerRespDTO outPlayerRespDTO = outPlayerDao.registerOutPlayer(outPlayerId, reason);
            if (outPlayerRespDTO == null) {
                throw new NullPointerException();
            }
            System.out.println(outPlayerId + "번 선수 퇴출 완료! 사유 : " + reason);
            System.out.println("| ---- p.id ---------------------- p.name -------------------- p.position ---------------- o.reason ------------------ o.createdAt ------------- |");
            customPrint.printOutPlayer(outPlayerRespDTO);
        }

        catch (NullPointerException e) {
            System.out.println("선수 등록 실패!");
        }
        catch (StadiumException e) {
            System.out.println("선수 퇴출 실패! 선수 번호가 잘못되었습니다.");
        }
        catch (RuntimeException e) {
            System.out.println("DB연결을 확인해주세요." + e.getMessage());
        }

    }

    public void updateOutPlayerReason(int playerId, String reason) {
        try {
            OutPlayerRespDTO outPlayerRespDTO = outPlayerDao.updateOutPlayerReason(playerId, reason);
            if (outPlayerRespDTO == null) {
                throw new RuntimeException();
            }
            System.out.println(playerId + "번 선수 퇴출 사유 수정 완료! 사유 : " + reason);
            System.out.println("| ---- p.id ---------------------- p.name -------------------- p.position ---------------- o.reason ------------------ o.createdAt ------------- |");
            customPrint.printOutPlayer(outPlayerRespDTO);
        } catch (StadiumException e) {
            System.out.println("사유 수정 실패! 포지션이 중복되었습니다.");
        } catch (RuntimeException e) {
            System.out.println("사유 수정 실패! 아이디를 확인해주세요.");
        }
    }


}
