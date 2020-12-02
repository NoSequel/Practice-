package io.github.nosequel.practice.match.type;

import io.github.nosequel.practice.PracticePlugin;
import io.github.nosequel.practice.arena.schematic.ArenaTemplate;
import io.github.nosequel.practice.kit.Kit;
import io.github.nosequel.practice.match.Match;
import io.github.nosequel.practice.match.MatchStatus;
import io.github.nosequel.practice.match.result.MatchResult;
import io.github.nosequel.practice.match.result.type.SoloMatchResult;
import io.github.nosequel.practice.player.ProfileHandler;
import io.github.nosequel.practice.player.ProfileState;
import io.github.nosequel.practice.util.Color;
import io.github.nosequel.practice.util.Ping;
import io.github.nosequel.practice.util.TimeUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

@Getter
public class SoloMatch implements Match<Player, SoloMatchResult> {

    private final ProfileHandler profileHandler = PracticePlugin.getPlugin().getHandler().find(ProfileHandler.class);
    private final SoloMatchResult result = new SoloMatchResult(this);
    private final List<Player> players = new ArrayList<>();

    private final Player player1;
    private final Player player2;

    private final Kit kit;
    private final ArenaTemplate arena;

    private MatchStatus status;
    private long startEpoch = System.currentTimeMillis();

    /**
     * Constructor for making a new match
     *
     * @param kit     the kit the players will receive
     * @param arena   the arena the match will be in
     * @param player1 the first player
     * @param player2 the second player
     */
    public SoloMatch(Kit kit, ArenaTemplate arena, Player player1, Player player2) {
        this.kit = kit;
        this.arena = arena;
        this.player1 = player1;
        this.player2 = player2;
        this.players.addAll(Arrays.asList(player1, player2));

        this.callGlobally(player -> this.getMatchHandler().startMatch(player, this));
    }

    @Override
    public void handleStart() {
        if (arena.getLocation1() == null || arena.getLocation2() == null) {
            throw new RuntimeException("Arena#getLocation1 or Location#getLocation2 returned null");
        }

        this.status = MatchStatus.STARTING;
        this.broadcast(ChatColor.GREEN + "Match found!");
        this.callGlobally(player -> {
            profileHandler.findProfile(player.getUniqueId()).setState(ProfileState.MATCH);
            Bukkit.getScheduler().runTaskLater(PracticePlugin.getPlugin(), () -> kit.equip(player), 1L);
        });

        this.arena.setUsable(false);
        this.player1.teleport(this.arena.getLocation1());
        this.player2.teleport(this.arena.getLocation2());

        final AtomicInteger timer = new AtomicInteger(5);

        new BukkitRunnable() {

            @Override
            public void run() {
                if (timer.get() == 0) {
                    broadcast(ChatColor.GREEN + "Match started.");
                    callGlobally(player -> {
                        player.closeInventory();
                        player.getLocation().getWorld().playSound(player.getLocation(), Sound.NOTE_PLING, 10F, 1F);
                    });


                    startEpoch = System.currentTimeMillis();
                    status = MatchStatus.STARTED;
                    player1.teleport(arena.getLocation1());
                    player2.teleport(arena.getLocation2());
                    result.setStartEpoch(System.currentTimeMillis());
                    this.cancel();
                } else {
                    broadcast(ChatColor.YELLOW + String.valueOf(timer.get()) + "...");
                    callGlobally(player -> player.getLocation().getWorld().playSound(player.getLocation(), Sound.NOTE_PLING, 10F, 2F));

                    timer.decrementAndGet();
                }
            }
        }.runTaskTimerAsynchronously(PracticePlugin.getPlugin(), 0L, 20L);
    }

    @Override
    public void handleEnd() {
        if (this.result.getWinner() == null || this.result.getLoser() == null) {
            throw new RuntimeException("MatchResult#getWinner or MatchResult#getLoser returned null");
        }

        this.status = MatchStatus.ENDED;
        this.broadcast(new String[]{
                ChatColor.RED + "The match has ended.",
                "",
                ChatColor.YELLOW + "Winner: " + ChatColor.GREEN + this.result.getWinner().getName() + ChatColor.DARK_RED + " [" + this.result.getPotions(this.result.getWinner()) + "]",
                ChatColor.YELLOW + "Loser: " + ChatColor.RED + this.result.getLoser().getName() + ChatColor.DARK_RED + " [" + this.result.getPotions(this.result.getLoser()) + "]"
        });

        this.callGlobally(player -> {
            player.setHealth(player.getMaxHealth());
            player.getInventory().setContents(new ItemStack[]{});
            player.getInventory().setArmorContents(new ItemStack[]{null, null, null, null});
        });


        Bukkit.getScheduler().runTaskLater(PracticePlugin.getPlugin(), () -> {
            this.callGlobally(player -> profileHandler.findProfile(player.getUniqueId()).setState(ProfileState.LOBBY));
            this.getMatchHandler().removeAllFromMatch(this);
            this.getMatchHandler().getMatchHistory().add(this.getResult());

            if (this.arena.isShouldDestroy()) {
                this.arena.destroy();
            }

            this.arena.setUsable(true);

        }, 60L);
    }

    @Override
    public List<String> getStrings(Player player) {
        final List<String> strings = new ArrayList<>();

        if (this.status.equals(MatchStatus.STARTING)) {
            strings.addAll(Arrays.asList(
                    Color.PRIMARY_COLOR_SCOREBOARD + "Opponent: " + Color.SECONDARY_COLOR_SCOREBOARD + this.getOpponent(player).getName(),
                    Color.PRIMARY_COLOR_SCOREBOARD + "Duration: " + Color.SECONDARY_COLOR_SCOREBOARD + TimeUtil.formatToMMss((System.currentTimeMillis() - this.startEpoch))
            ));
        } else if (this.status.equals(MatchStatus.STARTED)) {
            strings.addAll(Arrays.asList(
                    Color.PRIMARY_COLOR_SCOREBOARD + "Opponent: " + Color.SECONDARY_COLOR_SCOREBOARD + this.getOpponent(player).getName(),
                    Color.PRIMARY_COLOR_SCOREBOARD + "Duration: " + Color.SECONDARY_COLOR_SCOREBOARD + TimeUtil.formatToMMss((System.currentTimeMillis() - this.startEpoch)),
                    "",
                    ChatColor.GREEN + "Your Ping: " + ChatColor.WHITE + Ping.getPing(player) + "ms",
                    ChatColor.RED + "Their Ping: " + ChatColor.WHITE + Ping.getPing(this.getOpponent(player)) + "ms"
            ));
        } else if (this.status.equals(MatchStatus.ENDED)) {
            strings.add(
                    Color.TERTIARY_COLOR_SCOREBOARD + "Match ended."
            );
        }

        return strings;
    }

    @Override
    public Player getOpponent(Player toCheck) {
        return this.player1.equals(toCheck) ? this.player2 : this.player1;
    }

    @Override
    public void callGlobally(Consumer<Player> consumer) {
        this.getPlayers().stream()
                .filter(Player::isOnline)
                .forEach(consumer);
    }

    @Override
    public void broadcast(String message) {
        this.callGlobally(player -> player.sendMessage(message));
    }

    @Override
    public void broadcast(String[] message) {
        this.callGlobally(player -> player.sendMessage(message));
    }
}