package io.github.nosequel.practice.listener;

import io.github.nosequel.practice.PracticePlugin;
import io.github.nosequel.practice.player.Profile;
import io.github.nosequel.practice.player.ProfileHandler;
import io.github.nosequel.practice.player.ProfileState;
import io.github.nosequel.practice.util.Color;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.ServerListPingEvent;

public class PlayerListener implements Listener {

    private final ProfileHandler profileHandler = PracticePlugin.getPlugin().getHandler().find(ProfileHandler.class);

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);

        final Player player = event.getPlayer();
        final Profile profile;

        if (profileHandler.findProfile(player.getUniqueId()) == null) {
            profile = new Profile(player.getUniqueId());
            profileHandler.getProfiles().put(player.getUniqueId(), new Profile(player.getUniqueId()));
        } else {
            profile = profileHandler.findProfile(player.getUniqueId());
        }

        profile.setState(ProfileState.LOBBY);
    }

    @EventHandler
    public void onPing(ServerListPingEvent event) {
        event.setMotd(ChatColor.DARK_AQUA + "Xingqiu Practice "
                + ChatColor.GRAY + "(" + ChatColor.AQUA + "EU" + ChatColor.GRAY + ") "
                + ChatColor.BOLD.toString() + " ｜ "
                + ChatColor.AQUA + "Whitelisted\n" +
                ChatColor.GRAY + "Follow progress at " + ChatColor.AQUA + "https://discord.gg/KcDjDhta");
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        if (!event.getPlayer().getGameMode().equals(GameMode.CREATIVE) || !event.getPlayer().hasPermission("practice.build")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        if (!event.getPlayer().getGameMode().equals(GameMode.CREATIVE) || !event.getPlayer().hasPermission("practice.build")) {
            event.setCancelled(true);
        }
    }
}