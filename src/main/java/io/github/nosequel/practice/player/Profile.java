package io.github.nosequel.practice.player;

import io.github.nosequel.practice.PracticePlugin;
import io.github.nosequel.practice.kit.Kit;
import io.github.nosequel.practice.kit.loadout.KitLoadout;
import io.github.nosequel.practice.match.Match;
import io.github.nosequel.practice.match.MatchHandler;
import io.github.nosequel.practice.queue.QueueEntry;
import io.github.nosequel.practice.queue.QueueHandler;
import io.github.nosequel.practice.util.loadout.Loadout;
import io.github.nosequel.practice.util.loadout.LoadoutHandler;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;

import java.util.*;

@Getter
@Setter
public class Profile {

    private final UUID uuid;
    private final Map<Kit, List<KitLoadout>> loadouts = new HashMap<>();

    private long lastEnderpearlThrow = -1L;
    private ProfileState state;

    /**
     * Constructor for making a new profile
     *
     * @param uuid the unique identifier of the profile
     */
    public Profile(UUID uuid) {
        this.uuid = uuid;
    }

    /**
     * Get the loadouts by a kit
     *
     * @param kit the kit
     * @return the loadouts
     */
    public List<KitLoadout> getLoadouts(Kit kit) {
        if (!loadouts.containsKey(kit)) {
            this.loadouts.put(kit, Collections.singletonList(new KitLoadout("Default", kit)));
        }

        return this.loadouts.get(kit);
    }

    public void setState(ProfileState state) {
        this.state = state;

        // update the player's loadout
        PracticePlugin.getPlugin().getHandler().find(LoadoutHandler.class).equipLoadout(this);
    }

    /**
     * Add a new loadout to the list of loadouts for a kit
     *
     * @param kit     the kit whose loadout it is
     * @param loadout the loadout to add
     */
    public void addLoadout(Kit kit, KitLoadout loadout) {
        final List<KitLoadout> loadouts = new ArrayList<>(this.getLoadouts(kit));
        loadouts.add(loadout);

        this.loadouts.put(kit, loadouts);
    }

    /**
     * Remove a loadout from the list of loadouts
     *
     * @param loadout the loadout
     */
    public void removeLoadout(KitLoadout loadout) {
        this.loadouts.values().forEach(loadouts -> loadouts.removeIf(obj -> obj.equals(loadout)));
    }

    /**
     * Get a match the player is in
     *
     * @return the match
     */
    public Match<?, ?> getMatch() {
        return PracticePlugin.getPlugin().getHandler().find(MatchHandler.class).getMatch(Bukkit.getPlayer(this.uuid));
    }

    /**
     * Get a match the player is in
     *
     * @return the match
     */
    public QueueEntry<?> getQueue() {
        return PracticePlugin.getPlugin().getHandler().find(QueueHandler.class).getQueue(Bukkit.getPlayer(this.uuid));
    }
}