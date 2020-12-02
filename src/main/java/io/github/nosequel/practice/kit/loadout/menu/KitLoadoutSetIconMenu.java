package io.github.nosequel.practice.kit.loadout.menu;

import io.github.nosequel.katakuna.button.Button;
import io.github.nosequel.katakuna.button.impl.ButtonBuilder;
import io.github.nosequel.katakuna.menu.paginated.PaginatedMenu;
import io.github.nosequel.practice.kit.loadout.KitLoadout;
import io.github.nosequel.practice.util.Color;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class KitLoadoutSetIconMenu extends PaginatedMenu {

    private final KitLoadout loadout;

    public KitLoadoutSetIconMenu(Player player, KitLoadout loadout) {
        super(player, "Select an icon", 36);
        this.loadout = loadout;
    }

    @Override
    public List<Button> getButtons() {
        final AtomicInteger index = new AtomicInteger();

        return Arrays.stream(Material.values())
                .filter(material -> !material.equals(Material.AIR))
                .map(material -> new ButtonBuilder(material)
                        .setDisplayName(Color.PRIMARY_COLOR + material.name())
                        .setIndex(index.getAndIncrement())
                        .setAction((type, player) -> {
                            this.loadout.setIcon(material);

                            player.sendMessage(Color.SECONDARY_COLOR + "You have changed the icon of the " + Color.PRIMARY_COLOR + loadout.getName() + Color.SECONDARY_COLOR + " loadout.");
                            player.closeInventory();
                        })
                ).collect(Collectors.toList());
    }
}