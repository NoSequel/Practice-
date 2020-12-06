package io.github.nosequel.practice.queue.type;

import io.github.nosequel.practice.PracticePlugin;
import io.github.nosequel.practice.arena.Arena;
import io.github.nosequel.practice.arena.ArenaHandler;
import io.github.nosequel.practice.kit.Kit;
import io.github.nosequel.practice.match.type.solo.SoloMatch;
import io.github.nosequel.practice.queue.Queue;
import io.github.nosequel.practice.queue.QueueEntry;
import io.github.nosequel.practice.util.Color;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class SoloQueue implements Queue<Player> {

    private final List<QueueEntry<Player>> entries = new ArrayList<>();
    private final ArenaHandler arenaHandler = PracticePlugin.getPlugin().getHandler().find(ArenaHandler.class);

    @Override
    public List<QueueEntry<Player>> getEntries(Kit kit) {
        return this.entries.stream()
                .filter(current -> current.getKit().equals(kit))
                .collect(Collectors.toList());
    }

    @Override
    public void addToQueue(Player entry, Kit kit, Arena providedArena) {
        // check if the player is already queueing
        if (this.entries.stream().anyMatch(current -> current.getObject().equals(entry))) {
            entry.sendMessage(ChatColor.RED + "You are already queueing");
        } else {
            // check if the provided arena is null, if it's null then get a random arena from the ArenaHandler.
            final Arena arena = providedArena == null ? arenaHandler.getRandomArena() : providedArena;

            // check if the arena list is empty or the arena is null.
            if (arenaHandler.getArenas().isEmpty() && arena == null) {
                entry.sendMessage(ChatColor.RED + "No arenas are available at the moment, retry later.");
            } else {
                // add the player to the queue
                this.entries.add(new QueueEntry<>(entry, this, arena, kit));

                // get the list of entries for the kit the player is queueing
                final List<QueueEntry<Player>> entries = this.getEntries(kit);

                entry.sendMessage(Color.SECONDARY_COLOR + "You have joined the " + Color.PRIMARY_COLOR + kit.getName() + Color.SECONDARY_COLOR + " queue.");

                // check if there are more than 2 people in the queue
                if (entries.size() >= 2) {
                    final QueueEntry<Player> entry1 = entries.get(0);
                    final QueueEntry<Player> entry2 = entries.get(1);

                    // remove the first and second queue entry from the queue list
                    Arrays.asList(entry1, entry2).forEach(this::removeEntry);

                    // start a match between the first and second queue entry
                    new SoloMatch(kit, entry1.getArena().getTemplate(), entry1.getObject(), entry2.getObject()).handleStart();
                }
            }
        }
    }

    @Override
    public void removeEntry(QueueEntry<Player> entry) {
        // remove the entry from the list of queueing players
        this.entries.remove(entry);
    }

    @Override
    public void removeEntry(Player player) {
        // remove the player from the entry list
        final QueueEntry<Player> entry = this.getEntry(player);

        if (entry != null) {
            this.removeEntry(entry);
        }
    }

    @Override
    public QueueEntry<Player> getEntry(Player player) {
        return this.entries.stream()
                .filter(entry -> entry.getObject().equals(player))
                .findFirst().orElse(null);
    }

    @Override
    public Class<Player> getType() {
        return Player.class;
    }
}