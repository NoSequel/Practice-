package io.github.nosequel.practice.util.loadout.impl;

import io.github.nosequel.practice.PracticePlugin;
import io.github.nosequel.practice.player.Profile;
import io.github.nosequel.practice.player.ProfileHandler;
import io.github.nosequel.practice.player.ProfileState;
import io.github.nosequel.practice.queue.QueueHandler;
import io.github.nosequel.practice.util.item.Item;
import io.github.nosequel.practice.util.loadout.Loadout;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class QueueLoadout implements Loadout {

    private final QueueHandler queueHandler = PracticePlugin.getPlugin().getHandler().find(QueueHandler.class);
    private final ProfileHandler profileHandler = PracticePlugin.getPlugin().getHandler().find(ProfileHandler.class);

    private final Item[] queueItems = new Item[]{
            new Item(new ItemStack(Material.REDSTONE), 8).displayName("&cLeave Queue").interact(player -> {
                final Profile profile = profileHandler.findProfile(player.getUniqueId());
                queueHandler.unqueue(player);

                if (profile != null) {
                    profile.setState(ProfileState.LOBBY);
                }
            }),
    };

    @Override
    public void equip(Player player) {
        this.refresh(player);
        Arrays.stream(queueItems)
                .forEach(item -> item.give(player));
    }

    @Override
    public ProfileState getState() {
        return ProfileState.QUEUEING;
    }
}
