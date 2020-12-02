package io.github.nosequel.practice.util.loadout;

import io.github.nosequel.practice.PracticePlugin;
import io.github.nosequel.practice.handler.Handler;
import io.github.nosequel.practice.player.Profile;
import io.github.nosequel.practice.player.ProfileHandler;
import io.github.nosequel.practice.player.ProfileState;
import io.github.nosequel.practice.util.loadout.impl.LobbyLoadout;
import io.github.nosequel.practice.util.loadout.impl.QueueLoadout;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class LoadoutHandler implements Handler {

    private final List<Loadout> loadouts = new ArrayList<>(Arrays.asList(
            new LobbyLoadout(),
            new QueueLoadout()
    ));

    @Override
    public void load() {

    }

    @Override
    public void unload() {

    }

    /**
     * Equip a loadout to a player
     *
     * @param player the player to equip the loadout to
     */
    public void equipLoadout(Player player) {
        final ProfileHandler profileHandler = PracticePlugin.getPlugin().getHandler().find(ProfileHandler.class);
        final Profile profile = profileHandler.findProfile(player.getUniqueId());

        if (profile != null) {
            this.equipLoadout(profile);
        }
    }

    /**
     * Equip a loadout to a profile
     *
     * @param profile the profile to equip it to
     */
    public void equipLoadout(Profile profile) {
        final Loadout loadout = this.find(profile.getState());
        final Player player = Bukkit.getPlayer(profile.getUuid());

        if (loadout != null && player != null) {
            loadout.equip(player);
        }
    }

    /**
     * Find a loadout by their required state
     *
     * @param state the required state
     * @return the loadout or null
     */
    public Loadout find(ProfileState state) {
        return this.loadouts.stream()
                .filter(loadout -> loadout.getState().equals(state))
                .findFirst().orElse(null);
    }
}