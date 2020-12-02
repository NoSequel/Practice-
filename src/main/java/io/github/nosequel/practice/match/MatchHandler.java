package io.github.nosequel.practice.match;

import io.github.nosequel.practice.handler.Handler;
import io.github.nosequel.practice.match.result.MatchResult;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

@Getter
public class MatchHandler implements Handler {

    private final Map<Player, Match<?, ?>> matches = new HashMap<>();
    private final List<MatchResult<?>> matchHistory = new ArrayList<>();

    @Override
    public void load() {

    }

    @Override
    public void unload() {

    }

    /**
     * Set the match of a player
     *
     * @param player the player
     * @param match  the match to set it to
     */
    public void startMatch(Player player, Match<?, ?> match) {
        this.matches.put(player, match);
    }

    /**
     * Remove a player from their match
     *
     * @param player the player
     */
    public void removeFromMatch(Player player) {
        this.matches.remove(player);
    }

    /**
     * Get the match of a player
     *
     * @param player the player
     * @return the match or null
     */
    public Match<?, ?> getMatch(Player player) {
        return this.matches.get(player);
    }

    /**
     * Remove all players from a match
     *
     * @param match the match
     */
    public void removeAllFromMatch(Match<?, ?> match) {
        this.matches.values().removeAll(Collections.singleton(match));
    }

    /**
     * Filter all results by a player
     *
     * @param player the player
     * @return the results
     */
    public List<? super MatchResult<?>> filterResultsByPlayer(Player player) {
        return this.matchHistory.stream()
                .filter(result -> result.hasPlayer(player))
                .collect(Collectors.toList());
    }

}
