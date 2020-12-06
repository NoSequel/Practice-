package io.github.nosequel.practice.match.type.solo;

import io.github.nosequel.practice.match.MatchParticipant;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PlayerMatchParticipant implements MatchParticipant<Player> {

    private final Player player;

    public PlayerMatchParticipant(Player player) {
        this.player = player;
    }

    @Override
    public String getName() {
        return this.player.getName();
    }

    @Override
    public UUID getUniqueId() {
        return this.player.getUniqueId();
    }

    @Override
    public Player get() {
        return this.player;
    }
}
