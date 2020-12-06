package io.github.nosequel.practice.match.command.menu;

import io.github.nosequel.katakuna.button.Button;
import io.github.nosequel.katakuna.button.impl.ButtonBuilder;
import io.github.nosequel.katakuna.menu.Menu;
import io.github.nosequel.practice.match.result.MatchResult;
import io.github.nosequel.practice.util.Color;
import io.github.nosequel.practice.util.WoolColor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class MatchHistoryInformationMenu extends Menu {

    private final MatchResult<?> result;

    public MatchHistoryInformationMenu(Player player, MatchResult<?> result) {
        super(player, "Match result information", 18);
        this.result = result;
    }

    @Override
    public List<Button> getButtons() {
        final List<Button> buttons = new ArrayList<>();

        buttons.add(new ButtonBuilder(Material.WOOL)
                .setIndex(0)
                .setData(WoolColor.getWoolColor(ChatColor.GREEN))
                .setDisplayName(ChatColor.GREEN + result.getWinner().getName())
                .setLore(
                        ChatColor.GRAY + "Click to filter results"
                ).setAction((type, player) -> new MatchHistoryMenu(player, result.getWinner().getUniqueId()).updateMenu())
        );

        buttons.add(new ButtonBuilder(Material.WOOL)
                .setIndex(8)
                .setData(WoolColor.getWoolColor(ChatColor.RED))
                .setDisplayName(ChatColor.RED + result.getLoser().getName())
                .setLore(
                        Color.FOOTER_COLOR + "Click to filter results"
                ).setAction((type, player) -> new MatchHistoryMenu(player, result.getLoser().getUniqueId()).updateMenu())
        );

        if(result.getWinner().get() instanceof Player && result.getLoser().get() instanceof Player) {
            final Player winner = (Player) result.getWinner().get();
            final Player loser = (Player) result.getLoser().get();

            buttons.add(new ButtonBuilder(Material.POTION)
                    .setIndex(9)
                    .setDisplayName(ChatColor.GREEN + "Winner's Health Potions")
                    .setLore(
                            Color.SECONDARY_COLOR + String.valueOf(result.getPotions(winner))
                    )
            );

            buttons.add(new ButtonBuilder(Material.POTION)
                    .setIndex(17)
                    .setDisplayName(ChatColor.RED + "Loser's Health Potions")
                    .setLore(
                            Color.SECONDARY_COLOR + String.valueOf(result.getPotions(loser))
                    )
            );

        }

        return buttons;
    }
}