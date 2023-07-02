package toy.baseball.management.util;

import java.util.StringTokenizer;

public class CheckValues {
    private static CheckValues instance;  // 인스턴스 변수

    private CheckValues() {
        // private 생성자로 외부에서 인스턴스 생성을 방지
    }

    public static CheckValues getInstance() {
        if (instance == null) {
            synchronized (CheckValues.class) {
                if (instance == null) {
                    instance = new CheckValues();  // 인스턴스 생성
                }
            }
        }
        return instance;
    }

    // 싱글톤 클래스의 나머지 코드


    public String checkName(String str) {
        StringTokenizer st = new StringTokenizer(str, "=");
        String front = st.nextToken();
        String back = st.nextToken();

        if (front.equals("name"))
            return back;
        else return null;
    }

    public Integer checkStadiumId(String str) {
        StringTokenizer st = new StringTokenizer(str, "=");
        String front = st.nextToken();
        String back = st.nextToken();

        if (front.equals("stadiumId")) {
            return Integer.parseInt(back);
        }else return null;
    }

    public Integer checkTeamId(String str) {
        StringTokenizer st = new StringTokenizer(str, "=");
        String front = st.nextToken();
        String back = st.nextToken();

        if (front.equals("teamId")) {
            return Integer.parseInt(back);
        }else return null;
    }

    public Integer checkPlayerId(String str) {
        StringTokenizer st = new StringTokenizer(str, "=");
        String front = st.nextToken();
        String back = st.nextToken();

        if (front.equals("playerId")) {
            return Integer.parseInt(back);
        }else return null;
    }

    public String checkPosition(String str) {
        StringTokenizer st = new StringTokenizer(str, "=");
        String front = st.nextToken();
        String back = st.nextToken();

        if (front.equals("position")) {
            return back;
        }else return null;
    }

    public String checkReason(String str) {
        StringTokenizer st = new StringTokenizer(str, "=");
        String front = st.nextToken();
        String back = st.nextToken();

        if (front.equals("reason")) {
            return back;
        }else return null;
    }
}

