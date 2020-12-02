package io.github.nosequel.practice.arena.command;

import io.github.nosequel.practice.PracticePlugin;
import io.github.nosequel.practice.arena.Arena;
import io.github.nosequel.practice.arena.ArenaHandler;
import io.github.nosequel.practice.arena.menu.ArenaTemplateMenu;
import io.github.nosequel.practice.util.command.annotation.Command;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ArenaTemplateCommand {

    private final ArenaHandler arenaHandler = PracticePlugin.getPlugin().getHandler().find(ArenaHandler.class);

    @Command(label = "templates", permission = "practice.viewtemplates")
    public void execute(Player player, String name) {
        final Arena arena = this.arenaHandler.findArena(name);

        if (arena == null) {
            player.sendMessage(ChatColor.RED + "This does not arena exist.");
            return;
        }

        new ArenaTemplateMenu(player, arena).updateMenu();
    }
}

