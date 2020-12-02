package io.github.nosequel.practice.listener;

import io.github.nosequel.practice.PracticePlugin;
import io.github.nosequel.practice.match.Match;
import io.github.nosequel.practice.match.MatchHandler;
import io.github.nosequel.practice.match.MatchStatus;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class MatchListener implements Listener {

    private final MatchHandler matchHandler = PracticePlugin.getPlugin().getHandler().find(MatchHandler.class);

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);

        final Player player = event.getPlayer();
        final Match<?, ?> match = this.matchHandler.getMatch(player);

        if (match != null) {
            match.getPlayers().forEach(current -> match.getResult().saveInventory(current));
            match.getResult().handleDeath(player);
            match.broadcast(ChatColor.RED + player.getName() + ChatColor.GRAY + " quit.");
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        event.setDeathMessage(null);

        final Player player = event.getEntity();
        final Match<?, ?> match = this.matchHandler.getMatch(player);

        if (match != null) {
            if (player.getKiller() != null) {
                match.broadcast(ChatColor.GREEN + player.getKiller().getName() + ChatColor.GRAY + " killed " + ChatColor.RED + player.getName());
            } else {
                match.broadcast(ChatColor.RED + player.getName() + "died.");
            }

            // save all inventories
            match.getPlayers().forEach(current -> match.getResult().saveInventory(current));

            // clear all entity drops
            event.getDrops().clear();

            // handle the death
            match.getResult().handleDeath(player);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        // check if the entity is a player
        if (event.getEntity() instanceof Player) {
            final Player player = (Player) event.getEntity();
            final Match<?, ?> match = this.matchHandler.getMatch(player);

            // if the player is in a match and the match has started cancel the damage
            if (match == null || !match.getStatus().equals(MatchStatus.STARTED)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        // cancel the drop event if the player is not in a match
        if (this.matchHandler.getMatch(event.getPlayer()) == null) {
            event.setCancelled(true);
        } else {
            // if the player is in a match destroy the dropped entity after 60 ticks (3 seconds)
            Bukkit.getScheduler().runTaskLater(PracticePlugin.getPlugin(), () -> event.getItemDrop().remove(), 60L);
        }
    }

    @EventHandler
    public void onFood(FoodLevelChangeEvent event) {
        final Player player = (Player) event.getEntity();

        if (this.matchHandler.getMatch(player) == null) {
            event.setCancelled(true);
            player.setFoodLevel(20);
        }
    }
}