package apricot.workshopsystem.common.util;

import java.time.LocalDateTime;

public class TimeUtil {
    public static boolean overlaps(final LocalDateTime left1, final LocalDateTime right1,
                                   final LocalDateTime left2, final LocalDateTime right2) {
        if (right2.isBefore(left1)) {
            return false;
        }

        if (left2.isAfter(right1)) {
            return false;
        }

        return true;
    }
}
