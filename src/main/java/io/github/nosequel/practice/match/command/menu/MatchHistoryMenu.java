package io.github.nosequel.practice.match.command.menu;

import io.github.nosequel.katakuna.button.Button;
import io.github.nosequel.katakuna.button.impl.ButtonBuilder;
import io.github.nosequel.katakuna.menu.paginated.PaginatedMenu;
import io.github.nosequel.practice.PracticePlugin;
import io.github.nosequel.practice.match.MatchHandler;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

public class MatchHistoryMenu extends PaginatedMenu {

    private final MatchHandler matchHandler = PracticePlugin.getPlugin().getHandler().find(MatchHandler.class);

    public MatchHistoryMenu(Player player) {
        super(player, "Match history", 18);
    }

    @Override
    public List<Button> getButtons() {
        return matchHandler.getMatchHistory().stream()
                .map(result -> new ButtonBuilder(Material.WOOL))
    }
}
