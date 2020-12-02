package io.github.nosequel.practice.util;

import io.github.nosequel.practice.PracticePlugin;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;


@UtilityClass
public class PlayerUtil {

    /**
     * Respawn a player
     *
     * @param player the player to respawn
     */
    public void respawn(Player player) {
        final Location location = player.getLocation();
        player.spigot().respawn();

        Bukkit.getScheduler().runTaskLater(PracticePlugin.getPlugin(), () -> {
            player.teleport(location);
            player.setVelocity(new Vector(0, 0.7, 0));
        }, 1L);
    }

}