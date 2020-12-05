package io.github.nosequel.practice.match.command.menu;

import io.github.nosequel.katakuna.button.Button;
import io.github.nosequel.katakuna.button.impl.ButtonBuilder;
import io.github.nosequel.katakuna.menu.paginated.PaginatedMenu;
import io.github.nosequel.practice.PracticePlugin;
import io.github.nosequel.practice.match.MatchHandler;
import io.github.nosequel.practice.util.Color;
import io.github.nosequel.practice.util.TimeUtil;
import io.github.nosequel.practice.util.WoolColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class MatchHistoryMenu extends PaginatedMenu {

    private final MatchHandler matchHandler = PracticePlugin.getPlugin().getHandler().find(MatchHandler.class);

    public MatchHistoryMenu(Player player) {
        super(player, "Match history", 18);
    }

    @Override
    public List<Button> getButtons() {
        final AtomicInteger index = new AtomicInteger();

        return matchHandler.getMatchHistory().stream()
                .map(result -> new ButtonBuilder(Material.WOOL)
                        .setIndex(index.getAndIncrement())
                        .setData(WoolColor.getWoolColor(ChatColor.GOLD))
                        .setDisplayName(Color.PRIMARY_COLOR + result.getMatchUniqueId().toString())
                        .setLore(
                                Color.SECONDARY_COLOR + TimeUtil.formatToDate(result.getEndEpoch()),
                                Color.SECONDARY_COLOR + "Kit: " + Color.TERTIARY_COLOR + result.getKit().getName(),
                                Color.SECONDARY_COLOR + "Server: " + Color.TERTIARY_COLOR + Bukkit.getServerName(),
                                "",
                                Color.TERTIARY_COLOR + "Click for more information"
                        )
                ).collect(Collectors.toList());
    }
}