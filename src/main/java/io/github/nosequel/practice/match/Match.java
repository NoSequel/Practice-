package io.github.nosequel.practice.match;

import io.github.nosequel.practice.PracticePlugin;
import io.github.nosequel.practice.kit.Kit;
import io.github.nosequel.practice.match.result.MatchResult;
import io.github.nosequel.practice.scoreboard.Scoreboardable;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.function.Consumer;

public interface Match<T extends MatchParticipant<?>, M extends MatchResult<?>> extends Scoreboardable {

    /**
     * Get all players inside of a match
     *
     * @return the players
     */
    List<Player> getPlayers();

    /**
     * Get the opponent of an enemy
     *
     * @param toCheck the enemy to check
     * @return the opponent
     */
    T getOpponent(T toCheck);

    /**
     * Get the match participant object from a player
     *
     * @param player the player
     * @return the object
     */
    MatchParticipant<?> getParticipant(Player player);

    /**
     * Get the result of the match
     *
     * @return the result
     */
    M getResult();

    /**
     * Get the kit of the match
     *
     * @return the kit
     */
    Kit getKit();

    /**
     * Get the status of the match
     *
     * @return the status
     */
    MatchStatus getStatus();

    /**
     * Get the time in epoch the match started
     *
     * @return the epoch
     */
    long getStartEpoch();

    /**
     * Call an action globally in the match
     *
     * @param consumer the action
     */
    void callGlobally(Consumer<T> consumer);

    /**
     * Broadcast a message inside of the match
     *
     * @param message the message to broadcast
     */
    void broadcast(String message);

    /**
     * Broadcast an array of messages inside of the match
     *
     * @param message the array of messages to broadcast
     */
    void broadcast(String[] message);

    /**
     * Handle the starting of a match
     */
    void handleStart();

    /**
     * Handle the end of a match
     */
    void handleEnd();

    /**
     * Get the registered {@link MatchHandler} from the {@link io.github.nosequel.practice.handler.HandlerManager}
     *
     * @return the found match handler or null
     */
    default MatchHandler getMatchHandler() {
        return PracticePlugin.getPlugin().getHandler().find(MatchHandler.class);
    }

}
