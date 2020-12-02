package io.github.nosequel.practice.arena.menu;

import io.github.nosequel.katakuna.button.Button;
import io.github.nosequel.katakuna.button.impl.ButtonBuilder;
import io.github.nosequel.katakuna.menu.paginated.PaginatedMenu;
import io.github.nosequel.practice.arena.Arena;
import io.github.nosequel.practice.util.Color;
import io.github.nosequel.practice.util.WoolColor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class ArenaTemplateMenu extends PaginatedMenu {

    private final Arena arena;

    public ArenaTemplateMenu(Player player, Arena arena) {
        super(player, "Viewing templates", 36);

        this.arena = arena;
    }

    @Override
    public List<Button> getButtons() {
        final AtomicInteger index = new AtomicInteger();
        final List<Button> buttons = new ArrayList<>(Collections.singletonList(new ButtonBuilder(Material.LEVER)
                .setIndex(-5)
                .setDisplayName(Color.PRIMARY_COLOR + "Rebuild all templates")
                .setLore(
                        Color.SECONDARY_COLOR + "Click here to rebuild all templates",
                        Color.SECONDARY_COLOR + "This might take a while if there are multiple templates."
                )
                .setAction((type, player) -> arena.getTemplates().forEach(template -> {
                    if (template.isBuilt()) {
                        template.destroy();
                    }

                    template.build();
                    player.updateInventory();
                }))));

        buttons.addAll(this.arena.getTemplates().stream()
                .map(template -> new ButtonBuilder(Material.WOOL)
                        .setIndex(index.getAndIncrement())
                        .setData(WoolColor.getWoolColor(template.isUsable() && template.isBuilt() ? ChatColor.GREEN : template.isBuilt() ? ChatColor.GOLD : ChatColor.RED))
                        .setDisplayName(Color.PRIMARY_COLOR + arena.getName() + " #" + arena.getTemplates().indexOf(template))
                        .setLore(
                                Color.SECONDARY_COLOR + "Built: " + template.isBuilt(),
                                Color.SECONDARY_COLOR + "Destroyable: " + template.isShouldDestroy(),
                                Color.SECONDARY_COLOR + "Usable: " + template.isUsable(),
                                "",
                                template.isBuilt()
                                        ? Color.SECONDARY_COLOR + "Click here to destroy this template."
                                        : Color.SECONDARY_COLOR + "Click here to build this template."
                        )
                        .setAction((type, player) -> {
                            if (template.isUsable() && template.isBuilt()) {
                                template.destroy();
                                player.sendMessage(ChatColor.YELLOW + "Destroying arena template with id #" + arena.getTemplates().indexOf(template));
                            } else if (!template.isBuilt()) {
                                template.build();
                                player.sendMessage(ChatColor.YELLOW + "Bulding arena template with id #" + arena.getTemplates().indexOf(template));
                            } else {
                                player.sendMessage(ChatColor.RED + "That arena could not be destroyed.");
                            }

                            this.updateMenu();
                        })
                ).collect(Collectors.toList()));

        return buttons;
    }
}