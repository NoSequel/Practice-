package io.github.nosequel.practice.scoreboard;

import org.bukkit.entity.Player;

import java.util.List;

public interface Scoreboardable {

    List<String> getStrings(Player player);

}
