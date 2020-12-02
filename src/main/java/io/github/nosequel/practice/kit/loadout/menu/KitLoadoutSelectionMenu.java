package io.github.nosequel.practice.kit.loadout.menu;

import io.github.nosequel.katakuna.button.Button;
import io.github.nosequel.katakuna.button.impl.ButtonBuilder;
import io.github.nosequel.katakuna.menu.Menu;
import io.github.nosequel.practice.PracticePlugin;
import io.github.nosequel.practice.kit.Kit;
import io.github.nosequel.practice.player.Profile;
import io.github.nosequel.practice.player.ProfileHandler;
import io.github.nosequel.practice.util.Color;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class KitLoadoutSelectionMenu extends Menu {

    private final Kit kit;
    private final ProfileHandler profileHandler = PracticePlugin.getPlugin().getHandler().find(ProfileHandler.class);
    private final AtomicBoolean equipped = new AtomicBoolean(false);

    public KitLoadoutSelectionMenu(Kit kit, Player player) {
        super(player, "Select a kit loadout", 9);
        this.kit = kit;
    }

    @Override
    public List<Button> getButtons() {
        final AtomicInteger integer = new AtomicInteger();

        return profileHandler.findProfile(getPlayer().getUniqueId()).getLoadouts(kit).stream()
                .map(loadout -> new ButtonBuilder(loadout.getIcon())
                        .setIndex(integer.getAndIncrement())
                        .setDisplayName(Color.PRIMARY_COLOR + loadout.getName())
                        .setLore(
                                Color.SECONDARY_COLOR + "Date: " + new SimpleDateFormat("MM/dd/yyyy HH:mm").format(new Date(loadout.getCreateDate())),
                                Color.SECONDARY_COLOR + "Click to select this loadout."
                        )
                        .setAction((type, player) -> {
                            loadout.equip(player);
                            equipped.set(true);
                            player.closeInventory();
                        })
                ).collect(Collectors.toList());
    }

    @Override
    public void onClose(InventoryCloseEvent event) {
        if (!this.equipped.get()) {
            final Profile profile = this.profileHandler.findProfile(event.getPlayer().getUniqueId());
            profile.getLoadouts(this.kit).get(0).equip((Player) event.getPlayer());
        }
    }
}