package io.github.nosequel.practice.util.loadout;

import io.github.nosequel.practice.PracticePlugin;
import io.github.nosequel.practice.player.ProfileState;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface Loadout {

    /**
     * Equip the loadout to a player
     *
     * @param player the player to equip it to
     */
    void equip(Player player);

    /**
     * Get the state required for this loadout
     *
     * @return the required state
     */
    ProfileState getState();

    /**
     * Refresh a player's state
     *
     * @param player the player
     */
    default void refresh(Player player) {
        player.getActivePotionEffects().forEach(potion -> player.removePotionEffect(potion.getType()));
        player.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
        player.setFireTicks(0);
        player.setSaturation(20);
        player.setHealth(20);
        player.setFoodLevel(20);

        player.getInventory().setContents(new ItemStack[]{});
        player.getInventory().setArmorContents(new ItemStack[]{});
        player.updateInventory();
    }
}