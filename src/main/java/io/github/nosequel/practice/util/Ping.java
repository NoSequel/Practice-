package io.github.nosequel.practice.util;

import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Credits to konsolas @ SpigotMC.org
 * I could've made this class easily but it saves a lot of work.
 */
public class Ping {

    private static Method getHandleMethod;
    private static Field pingField;

    /**
     * Get the ping of a player
     *
     * @param player the player to get the ping from
     * @return the ping value
     */
    public static int getPing(Player player) {
        try {
            if (getHandleMethod == null) {
                getHandleMethod = player.getClass().getDeclaredMethod("getHandle");
                getHandleMethod.setAccessible(true);
            }
            Object entityPlayer = getHandleMethod.invoke(player);
            if (pingField == null) {
                pingField = entityPlayer.getClass().getDeclaredField("ping");
                pingField.setAccessible(true);
            }
            int ping = pingField.getInt(entityPlayer);

            return Math.max(ping, 0);
        } catch (Exception e) {
            return 1;
        }
    }
}
