package yuquiz.domain.user.entity;

import lombok.Getter;

@Getter
public enum SuspendDay {
    FIRST(1), SECOND(3), THIRD(5), FOURTH(7), FIFTH(10), MORE(15);

    private final int day;

    SuspendDay(int day) {
        this.day = day;
    }

    public static int getDay(int bannedCnt) {
        return switch (bannedCnt){
            case 1 -> SuspendDay.FIRST.getDay();
            case 2 -> SuspendDay.SECOND.getDay();
            case 3 -> SuspendDay.THIRD.getDay();
            case 4 -> SuspendDay.FOURTH.getDay();
            case 5 -> SuspendDay.FIFTH.getDay();
            default -> SuspendDay.MORE.getDay();
        };
    }
}
