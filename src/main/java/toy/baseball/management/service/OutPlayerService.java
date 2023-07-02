package toy.baseball.management.service;

import toy.baseball.management.dao.OutPlayerDao;
import toy.baseball.management.db.DBConnection;
import toy.baseball.management.dto.OutPlayerRespDTO;
import toy.baseball.management.exception.StadiumException;
import toy.baseball.management.util.CustomPrint;

import java.sql.Connection;

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


    public void registerOutPlayer(int outPlayerId, String reason) {
        try {
            OutPlayerRespDTO outPlayerRespDTO = outPlayerDao.registerOutPlayer(outPlayerId, reason);
            if (outPlayerRespDTO == null) {
                throw new NullPointerException();
            }
            System.out.println(outPlayerId + "번 선수 퇴출 완료! 사유 : " + reason);
            System.out.println("| ---- p.id ----------------------- p.name -------------------- p.position ---------------- o.reason ------------------ o.createdAt ------------ |");
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


}
