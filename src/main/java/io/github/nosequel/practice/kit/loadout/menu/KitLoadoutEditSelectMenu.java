package io.github.nosequel.practice.kit.loadout.menu;

import io.github.nosequel.katakuna.button.Button;
import io.github.nosequel.katakuna.button.impl.ButtonBuilder;
import io.github.nosequel.katakuna.menu.Menu;
import io.github.nosequel.practice.PracticePlugin;
import io.github.nosequel.practice.kit.KitHandler;
import io.github.nosequel.practice.util.Color;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class KitLoadoutEditSelectMenu extends Menu {

    private final KitHandler kitHandler = PracticePlugin.getPlugin().getHandler().find(KitHandler.class);

    public KitLoadoutEditSelectMenu(Player player) {
        super(player, "Select a Ladder", 18);
    }

    @Override
    public List<Button> getButtons() {
        final AtomicInteger index = new AtomicInteger();

        return kitHandler.getKits().stream()
                .map(kit -> new ButtonBuilder(kit.getIcon())
                        .setIndex(index.getAndIncrement())
                        .setDisplayName(Color.PRIMARY_COLOR + kit.getName())
                        .setLore(Color.SECONDARY_COLOR + "Click to edit kit")
                        .setAction((type, player) -> new KitLoadoutEditSelectLoadoutMenu(player, kit).updateMenu()))
                .collect(Collectors.toList());
    }
}