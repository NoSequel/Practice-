package io.github.nosequel.practice.arena.menu;

import io.github.nosequel.katakuna.button.Button;
import io.github.nosequel.katakuna.button.impl.ButtonBuilder;
import io.github.nosequel.katakuna.menu.paginated.PaginatedMenu;
import io.github.nosequel.practice.PracticePlugin;
import io.github.nosequel.practice.arena.ArenaHandler;
import io.github.nosequel.practice.util.Color;
import io.github.nosequel.practice.util.WoolColor;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class ArenaMenu extends PaginatedMenu {

    private final ArenaHandler arenaHandler = PracticePlugin.getPlugin().getHandler().find(ArenaHandler.class);

    public ArenaMenu(Player player) {
        super(player, "Arena list", 18);
    }

    @Override
    public List<Button> getButtons() {
        final AtomicInteger index = new AtomicInteger();
        return arenaHandler.getArenas().stream()
                .map(arena -> new ButtonBuilder(arena.getIcon())
                        .setData(WoolColor.getWoolColor(arena.isUsable() ? ChatColor.GREEN : ChatColor.RED))
                        .setIndex(index.getAndIncrement())
                        .setDisplayName(Color.PRIMARY_COLOR + arena.getName())
                        .setLore(
                                Color.SECONDARY_COLOR + "Templates: " + arena.getTemplates().size(),
                                Color.SECONDARY_COLOR + "Usable: " + arena.isUsable(),
                                "",
                                Color.SECONDARY_COLOR + "Click here to view templates"
                        )
                        .setAction((type, player) -> new ArenaTemplateMenu(player, arena).updateMenu())
                ).collect(Collectors.toList());
    }
}
