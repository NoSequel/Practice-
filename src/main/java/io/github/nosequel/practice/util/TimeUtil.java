package io.github.nosequel.practice.util;

import lombok.experimental.UtilityClass;

import java.text.SimpleDateFormat;
import java.util.Date;

@UtilityClass
public class TimeUtil {

    /**
     * Format a date to MM:ss
     *
     * @param epoch the epoch to format
     * @return the formatted string
     */
    public String formatToMMss(long epoch) {
        return new SimpleDateFormat("mm:ss").format(new Date(epoch));
    }

}
