package io.github.nosequel.practice.listener;

import io.github.nosequel.practice.PracticePlugin;
import io.github.nosequel.practice.queue.QueueHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QueueListener implements Listener {

    private final QueueHandler queueHandler = PracticePlugin.getPlugin().getHandler().find(QueueHandler.class);

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        final Player player = event.getPlayer();

        if (queueHandler.getQueue(player) != null) {
            queueHandler.unqueue(player);
        }
    }
}