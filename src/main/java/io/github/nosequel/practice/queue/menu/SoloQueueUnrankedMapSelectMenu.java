package io.github.nosequel.practice.queue.menu;

import io.github.nosequel.katakuna.button.Button;
import io.github.nosequel.katakuna.button.impl.ButtonBuilder;
import io.github.nosequel.katakuna.menu.Menu;
import io.github.nosequel.practice.PracticePlugin;
import io.github.nosequel.practice.arena.ArenaHandler;
import io.github.nosequel.practice.kit.Kit;
import io.github.nosequel.practice.player.Profile;
import io.github.nosequel.practice.player.ProfileHandler;
import io.github.nosequel.practice.player.ProfileState;
import io.github.nosequel.practice.queue.Queue;
import io.github.nosequel.practice.queue.QueueHandler;
import io.github.nosequel.practice.util.Color;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class SoloQueueUnrankedMapSelectMenu extends Menu {

    private final ArenaHandler arenaHandler = PracticePlugin.getPlugin().getHandler().find(ArenaHandler.class);
    private final QueueHandler queueHandler = PracticePlugin.getPlugin().getHandler().find(QueueHandler.class);
    private final ProfileHandler profileHandler = PracticePlugin.getPlugin().getHandler().find(ProfileHandler.class);

    private final Queue<Player> queue = queueHandler.findQueue(Player.class);
    private final Kit kit;


    public SoloQueueUnrankedMapSelectMenu(Player player, Kit kit) {
        super(player, "Select a map", 18);
        this.kit = kit;
    }

    @Override
    public List<Button> getButtons() {
        final AtomicInteger index = new AtomicInteger();
        final Profile profile = this.profileHandler.findProfile(getPlayer().getUniqueId());

        return arenaHandler.getArenas().stream() // loop through all arenas and map them to a ButtonBuilder
                .map(arena -> new ButtonBuilder(arena.getIcon())
                        .setIndex(index.getAndIncrement())
                        .setDisplayName(Color.PRIMARY_COLOR + arena.getName())
                        .setLore(
                                Color.SECONDARY_COLOR + "Click to select this arena"
                        ).setAction((type, player) -> {
                            // close the player's inventory
                            player.closeInventory();

                            // set the state of the player
                            profile.setState(ProfileState.QUEUEING);

                            // add the player to the queue
                            queue.addToQueue(player, kit, arena);
                        })
                )
                .collect(Collectors.toList());
    }
}
