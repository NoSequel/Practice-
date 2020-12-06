package io.github.nosequel.practice.match.command.menu;

import io.github.nosequel.katakuna.button.Button;
import io.github.nosequel.katakuna.button.impl.ButtonBuilder;
import io.github.nosequel.katakuna.menu.paginated.PaginatedMenu;
import io.github.nosequel.practice.PracticePlugin;
import io.github.nosequel.practice.match.MatchHandler;
import io.github.nosequel.practice.match.result.MatchResult;
import io.github.nosequel.practice.util.Color;
import io.github.nosequel.practice.util.TimeUtil;
import io.github.nosequel.practice.util.WoolColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class MatchHistoryMenu extends PaginatedMenu {

    private final MatchHandler matchHandler = PracticePlugin.getPlugin().getHandler().find(MatchHandler.class);
    private final UUID filtered;

    public MatchHistoryMenu(Player player, UUID filtered) {
        super(player, "Match history", 18);
        this.filtered = filtered;
    }

    @Override
    public List<Button> getButtons() {
        final AtomicInteger index = new AtomicInteger();
        final List<? extends MatchResult<?>> results = this.filtered == null ? matchHandler.getMatchHistory() : matchHandler.filterResultsByPlayer(this.filtered);

        return results.stream()
                .map(result -> new ButtonBuilder(Material.WOOL)
                        .setIndex(index.getAndIncrement())
                        .setData(WoolColor.getWoolColor(ChatColor.GOLD))
                        .setDisplayName(Color.PRIMARY_COLOR + result.getWinner().getName() + Color.SECONDARY_COLOR + " vs " + Color.PRIMARY_COLOR + result.getLoser().getName())
                        .setLore(
                                Color.SECONDARY_COLOR + TimeUtil.formatToDate(result.getEndEpoch()),
                                Color.SECONDARY_COLOR + "Kit: " + Color.TERTIARY_COLOR + result.getKit().getName(),
                                Color.SECONDARY_COLOR + "Server: " + Color.TERTIARY_COLOR + Bukkit.getServerId(),
                                Color.SECONDARY_COLOR + "Winner: " + ChatColor.GREEN + result.getWinner().getName(),
                                "",
                                Color.FOOTER_COLOR + "Click for more information"
                        ).setAction((type, player) -> new MatchHistoryInformationMenu(player, result).updateMenu())
                ).collect(Collectors.toList());
    }
}