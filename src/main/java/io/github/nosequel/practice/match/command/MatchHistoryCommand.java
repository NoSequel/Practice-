package io.github.nosequel.practice.match.command;

import io.github.nosequel.practice.match.command.menu.MatchHistoryMenu;
import io.github.nosequel.practice.util.command.annotation.Command;
import org.bukkit.entity.Player;

public class MatchHistoryCommand {

    @Command(label = "matchhistory", aliases = {"matchhist"})
    public void execute(Player player) {
        new MatchHistoryMenu(player).updateMenu();
    }

}
