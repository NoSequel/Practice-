package io.github.nosequel.practice.scoreboard;

import io.github.nosequel.practice.PracticePlugin;
import io.github.nosequel.practice.match.MatchHandler;
import io.github.nosequel.practice.queue.QueueEntry;
import io.github.nosequel.practice.queue.QueueHandler;
import io.github.nosequel.practice.util.Color;
import io.github.thatkawaiisam.assemble.AssembleAdapter;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ScoreboardImpl implements AssembleAdapter {

    private final MatchHandler matchHandler = PracticePlugin.getPlugin().getHandler().find(MatchHandler.class);
    private final QueueHandler queueHandler = PracticePlugin.getPlugin().getHandler().find(QueueHandler.class);

    @Override
    public String getTitle(Player player) {
        return Color.PRIMARY_COLOR_SCOREBOARD + "Practice" + Color.TERTIARY_COLOR_SCOREBOARD + " [Beta]";
    }

    @Override
    public List<String> getLines(Player player) {
        final List<String> strings = new ArrayList<>();

        if (matchHandler.getMatch(player) == null) {
            strings.addAll(Arrays.asList(
                    Color.PRIMARY_COLOR_SCOREBOARD + "Online: " + Color.SECONDARY_COLOR_SCOREBOARD + Bukkit.getOnlinePlayers().size(),
                    Color.PRIMARY_COLOR_SCOREBOARD + "Fighting: " + Color.SECONDARY_COLOR_SCOREBOARD + matchHandler.getMatches().size(),
                    Color.PRIMARY_COLOR_SCOREBOARD + "Queueing: " + Color.SECONDARY_COLOR_SCOREBOARD + queueHandler.getQueues().stream()
                            .mapToInt(current -> current.getEntries().size()).sum()
            ));

            if(queueHandler.getQueue(player) != null) {
                final QueueEntry<?> entry = this.queueHandler.getQueue(player);

                strings.addAll(Arrays.asList(
                        Color.TERTIARY_COLOR_SCOREBOARD + ChatColor.STRIKETHROUGH.toString() + StringUtils.repeat("-", 20),
                        Color.PRIMARY_COLOR_SCOREBOARD + "Queueing for: ",
                        Color.SECONDARY_COLOR_SCOREBOARD + entry.getKit().getName()
                ));
            }
        } else if (matchHandler.getMatch(player) != null) {
            strings.addAll(matchHandler.getMatch(player).getStrings(player));
        }

        strings.add(0, Color.TERTIARY_COLOR_SCOREBOARD + ChatColor.STRIKETHROUGH.toString() + StringUtils.repeat("-", 20));
        strings.add(Color.TERTIARY_COLOR_SCOREBOARD + ChatColor.STRIKETHROUGH.toString() + StringUtils.repeat("-", 20));

        return strings;
    }
}