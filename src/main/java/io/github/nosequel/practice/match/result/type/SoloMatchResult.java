package io.github.nosequel.practice.match.result.type;

import io.github.nosequel.practice.kit.Kit;
import io.github.nosequel.practice.match.result.ItemElement;
import io.github.nosequel.practice.match.result.MatchResult;
import io.github.nosequel.practice.match.type.solo.PlayerMatchParticipant;
import io.github.nosequel.practice.match.type.solo.SoloMatch;
import io.github.nosequel.practice.util.PlayerUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
public class SoloMatchResult implements MatchResult<PlayerMatchParticipant> {

    private final SoloMatch soloMatch;
    private final Map<Player, Integer> hits = new HashMap<>();
    private final Map<Player, List<ItemElement>> inventories = new HashMap<>();

    private final UUID matchUniqueId = UUID.randomUUID();

    private long startEpoch;
    private long endEpoch;

    private PlayerMatchParticipant winner;
    private PlayerMatchParticipant loser;

    /**
     * Constructor for making a new result for a {@link SoloMatch}
     *
     * @param soloMatch the {@link SoloMatch}
     */
    public SoloMatchResult(SoloMatch soloMatch) {
        this.soloMatch = soloMatch;
    }

    @Override
    public void handleDeath(Player player) {
        this.winner = this.soloMatch.getOpponent(this.soloMatch.getParticipant(player));
        this.loser = this.soloMatch.getParticipant(player);
        this.endEpoch = System.currentTimeMillis();
        this.soloMatch.handleEnd();

        PlayerUtil.respawn(player);
    }

    @Override
    public Integer getPotions(Player player) {
        return Integer.valueOf(String.valueOf(this.getInventory(player).stream()
                .filter(element -> element.getItemStack() != null)
                .count()));
    }

    @Override
    public List<ItemElement> getInventory(Player player) {
        return this.inventories.get(player);
    }

    @Override
    public Integer getHits(Player player) {
        return this.hits.get(player);
    }

    @Override
    public void saveInventory(Player player) {
        final List<ItemStack> contents = Arrays.asList(player.getInventory().getContents());
        final List<ItemStack> armorContents = Arrays.asList(player.getInventory().getArmorContents());
        final List<ItemElement> itemElements = new ArrayList<>();

        itemElements.addAll(contents.stream().map(item -> new ItemElement(contents.indexOf(item), item)).collect(Collectors.toList()));
        itemElements.addAll(armorContents.stream().map(item -> new ItemElement(armorContents.indexOf(item), item)).collect(Collectors.toList()));

        this.inventories.put(player, itemElements);
    }

    @Override
    public boolean hasPlayer(Player player) {
        return this.soloMatch.getPlayers().stream().anyMatch(participant -> participant.equals(player));
    }

    @Override
    public boolean hasPlayer(UUID uuid) {
        return this.soloMatch.getPlayers().stream().anyMatch(participant -> participant.getUniqueId().equals(uuid));
    }

    @Override
    public Kit getKit() {
        return this.soloMatch.getKit();
    }
}