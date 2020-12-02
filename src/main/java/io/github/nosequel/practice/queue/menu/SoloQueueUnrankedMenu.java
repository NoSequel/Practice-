package io.github.nosequel.practice.queue.menu;

import io.github.nosequel.katakuna.button.Button;
import io.github.nosequel.katakuna.button.impl.ButtonBuilder;
import io.github.nosequel.katakuna.menu.Menu;
import io.github.nosequel.practice.PracticePlugin;
import io.github.nosequel.practice.kit.KitHandler;
import io.github.nosequel.practice.match.MatchHandler;
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

public class SoloQueueUnrankedMenu extends Menu {

    private final KitHandler kitHandler = PracticePlugin.getPlugin().getHandler().find(KitHandler.class);
    private final QueueHandler queueHandler = PracticePlugin.getPlugin().getHandler().find(QueueHandler.class);
    private final MatchHandler matchHandler = PracticePlugin.getPlugin().getHandler().find(MatchHandler.class);
    private final ProfileHandler profileHandler = PracticePlugin.getPlugin().getHandler().find(ProfileHandler.class);

    private final Queue<Player> queue = queueHandler.findQueue(Player.class);

    public SoloQueueUnrankedMenu(Player player) {
        super(player, "Choose Queue", 18);
    }

    @Override
    public List<Button> getButtons() {
        final AtomicInteger integer = new AtomicInteger();
        final Profile profile = this.profileHandler.findProfile(getPlayer().getUniqueId());

        return kitHandler.getKits().stream() // loop through all kits and map them to a ButtonBuilder
                .map(kit -> new ButtonBuilder(kit.getIcon())
                        .setIndex(integer.getAndIncrement())
                        .setDisplayName(Color.PRIMARY_COLOR + kit.getName())
                        .setLore(
                                Color.SECONDARY_COLOR + "Queueing: " + Color.TERTIARY_COLOR + queue.getEntries(kit).size(),
                                Color.SECONDARY_COLOR + "In Fights: " + Color.TERTIARY_COLOR + matchHandler.getMatches().values().stream()
                                        .filter(match -> match.getKit().equals(kit)).count(),
                                "",
                                ChatColor.GRAY + "Click to queue"
                        )
                        .setAction((type, player) -> {
                            // close the player's inventory
                            player.closeInventory();

                            // if the player has permission to view the arena selection menu, open that
                            if (player.hasPermission("practice.selectarena")) {
                                new SoloQueueUnrankedMapSelectMenu(player, kit).updateMenu();
                            } else {
                                // add the player to the queue
                                queue.addToQueue(player, kit, null);

                                // set the state of the player
                                profile.setState(ProfileState.QUEUEING);
                            }
                        })
                ).collect(Collectors.toList());
    }
}