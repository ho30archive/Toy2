package toy.baseball.management.service;

import ch.qos.logback.core.joran.spi.NoAutoStartUtil;
import toy.baseball.management.dao.StadiumDao;
import toy.baseball.management.db.DBConnection;
import toy.baseball.management.exception.StadiumException;
import toy.baseball.management.model.Stadium;
import toy.baseball.management.util.CustomPrint;

import javax.swing.text.html.Option;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Optional;


public class StadiumService {
    static final Connection connection = DBConnection.getInstance();
    static final StadiumDao stadiumDao = StadiumDao.getInstance(connection);
    private static final StadiumService instance = new StadiumService();

    CustomPrint customPrint = new CustomPrint();

    private StadiumService() {
    }
    public static StadiumService getInstance() {return instance;}


    public void getStadiumList(){
        try {
            List<Stadium> stadiumList = stadiumDao.findAllStadium();
            if (stadiumList.size() != 0) {
                System.out.println("야구장 조회 완료!");
                System.out.println("| ---- id ------------------------ name ---------------------- createdAt --------------- |");
                for (Stadium stadium : stadiumList) {
                    customPrint.printStadium(stadium);
                }
            } else {
                System.out.println("등록된 야구장이 없습니다.");
            }
        } catch (RuntimeException e) {
            System.out.println("DB연결을 확인해주세요." + e.getMessage());
        }

    }

    public void registerStadium(String name) {
        try {
            Stadium stadium = stadiumDao.registerStadium(name);
            if (stadium == null) {
                throw new NullPointerException();
            }
            System.out.println("야구장 " + name + " 등록 완료!");
            System.out.println("| ---- id ------------------------ name ---------------------- createdAt --------------- |");
            customPrint.printStadium(stadium);
        } catch (NullPointerException e) {
            System.out.println("등록 실패! = " + e.getMessage());
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("등록 실패! " + name + "이(가) 이미 존재합니다.");
        } catch (RuntimeException e) {
            System.out.println("DB연결을 확인해주세요." + e.getMessage());
        }
    }

    public void deleteStadiumById(int staiumId) {
        try {
            Boolean check = stadiumDao.deleteById(staiumId);
            if (check) {
                System.out.println(staiumId + "번 야구장 삭제 성공");
            } else {
                System.out.println("삭제 실패");
            }
        } catch (NullPointerException e) {
            System.out.println("삭제 실패! " + staiumId + "번 아이디의 야구장은 존재 하지 않습니다.");
            System.out.println("야구장 아이디를 확인해 주세요.");
        } catch (StadiumException e) {
            System.out.println("삭제 실패! 무결성 제약조건에 위배됩니다. 관련 데이터를 먼저 삭제해 주세요.");
        } catch (RuntimeException e) {
            System.out.println("DB연결을 확인해주세요." + e.getMessage());
        }
    }

    public void updateStadiumName(int stadiumId, String name) {
        try {
            Stadium stadium = stadiumDao.updateStadiumName(stadiumId, name);
            if (stadium == null) {
                throw new RuntimeException();
            } else {
                System.out.println(name + "으로 이름 수정 완료!");
                System.out.println("| ---- id ------------------------ name ---------------------- createdAt --------------- |");
                customPrint.printStadium(stadium);
            }
        } catch (StadiumException e) {
            System.out.println("수정 실패! 중복되는 야구 이름 입니다.");
        } catch (RuntimeException e) {
            System.out.println("수정 실패! 아이디를 확인해주세요.");
        }


    }




}
