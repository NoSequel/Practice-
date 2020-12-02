package io.github.nosequel.practice.listener;

import io.github.nosequel.practice.PracticePlugin;
import io.github.nosequel.practice.player.Profile;
import io.github.nosequel.practice.player.ProfileHandler;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class EnderpearlListener implements Listener {

    private final ProfileHandler profileHandler = PracticePlugin.getPlugin().getHandler().find(ProfileHandler.class);

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        if (event.getCause().equals(PlayerTeleportEvent.TeleportCause.ENDER_PEARL)) {
            final Location to = event.getTo();

            if (to.getX() - to.getBlockX() > 0.3 || to.getZ() - to.getBlockZ() > 0.3) {
                to.subtract(to.getX() - to.getBlockX() - 0.3, 0, to.getZ() - to.getBlockZ() - 0.3);
            } else if (to.getX() - to.getBlockX() < -0.3 || to.getZ() - to.getBlockZ() < -0.03) {
                to.subtract(to.getX() - to.getBlockX() + 0.3, 0, to.getZ() + to.getBlockZ() - 0.3);
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final Profile profile = this.profileHandler.findProfile(player.getUniqueId());

        if (profile != null && player.getItemInHand() != null && (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK))) {
            if(player.getItemInHand().getType().equals(Material.ENDER_PEARL)) {
                if (profile.getLastEnderpearlThrow() - System.currentTimeMillis() > 0) {
                    player.sendMessage(ChatColor.RED + "You are still on an enderpearl cooldown.");
                    event.setCancelled(true);
                } else {
                    profile.setLastEnderpearlThrow(System.currentTimeMillis() + 16000);
                }
            }
        }
    }

}
