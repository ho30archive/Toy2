package toy.baseball.management.enums;

public enum Positions {
    FIRST_BASE("1루수"),
    SECOND_BASE("2루수"),
    THIRD_BASE("3루수"),
    CATCHER("포수"),
    PITCHER("투수"),
    SHORTSTOP("유격수"),
    LEFT_FIELDER("좌익수"),
    CENTER_FIELDER("중견수"),
    RIGHT_FIELDER("우익수");

    private final String positionName;

    private Positions(String positionName) {
        this.positionName = positionName;
    }

    public String getPositionName() {
        return positionName;
    }


}
