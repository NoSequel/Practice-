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

    /**
     * Format an epoch date to a human readable date
     *
     * @param epoch the epoch date to format
     * @return the formatted date
     */
    public String formatToDate(long epoch) { return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date(epoch)); }

}
