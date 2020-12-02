package io.github.nosequel.practice.arena.command;

import io.github.nosequel.practice.PracticePlugin;
import io.github.nosequel.practice.arena.Arena;
import io.github.nosequel.practice.arena.ArenaHandler;
import io.github.nosequel.practice.arena.menu.ArenaMenu;
import io.github.nosequel.practice.arena.schematic.ArenaTemplate;
import io.github.nosequel.practice.util.Color;
import io.github.nosequel.practice.util.command.annotation.Command;
import io.github.nosequel.practice.util.command.annotation.Subcommand;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ArenaCommand {

    private final ArenaHandler arenaHandler = PracticePlugin.getPlugin().getHandler().find(ArenaHandler.class);

    @Command(label = "arena", permission = "practice.arena")
    @Subcommand(parentLabel = "arena", label = "help")
    public void execute(Player player) {
        player.sendMessage(new String[]{
                Color.SECONDARY_COLOR + "------- [" + ChatColor.AQUA + "Arena Help" + Color.SECONDARY_COLOR + "] ------",
                ChatColor.AQUA + "/arena create <name>",
                ChatColor.AQUA + "/arena delete <name>",
                ChatColor.AQUA + "/arena setpos <1|2> <name> <x> <y> <z>",
                ChatColor.AQUA + "/arena setcorner <1|2> <name>",
                ChatColor.AQUA + "/arena prepare <name> <amount>",
                ChatColor.AQUA + "/arena icon <name> <icon>",
                ChatColor.AQUA + "/arena list",
                "",
        });
    }

    @Subcommand(parentLabel = "arena", label = "create")
    public void create(Player player, String arena) {
        if (arenaHandler.findArena(arena) != null) {
            player.sendMessage(ChatColor.RED + "This arena already exists.");
            return;
        }

        this.arenaHandler.getArenas().add(new Arena(UUID.randomUUID(), arena));
        player.sendMessage(Color.SECONDARY_COLOR + "The arena " + Color.PRIMARY_COLOR + arena + Color.SECONDARY_COLOR + " has been created.");
    }

    @Subcommand(parentLabel = "arena", label = "delete")
    public void delete(Player player, String name) {
        final Arena arena = this.arenaHandler.findArena(name);

        if (arena == null) {
            player.sendMessage(ChatColor.RED + "This does not arena exist.");
            return;
        }

        this.arenaHandler.getArenas().remove(arena);
        player.sendMessage(Color.SECONDARY_COLOR + "The arena " + Color.PRIMARY_COLOR + arena + Color.SECONDARY_COLOR + " has been created.");
    }

    @Subcommand(parentLabel = "arena", label = "setpos")
    public void setpos(Player player, Integer pos, String name, Integer x, Integer y, Integer z, Integer yaw, Integer pitch) {
        if (pos != 1 && pos != 2) {
            player.sendMessage(ChatColor.RED + "Usage: /arena setpos <1|2> <name> <x> <y> <z>");
        }

        final Arena arena = this.arenaHandler.findArena(name);

        if (arena == null) {
            player.sendMessage(ChatColor.RED + "This does not arena exist.");
            return;
        }

        if (pos == 1) {
            arena.setLocations(new Location(player.getWorld(), x, y, z, yaw, pitch), null);
        } else {
            arena.setLocations(null, new Location(player.getWorld(), x, y, z, yaw, pitch));
        }

        player.sendMessage(Color.SECONDARY_COLOR + "The arena " + Color.PRIMARY_COLOR + arena.getName() + Color.SECONDARY_COLOR + "'s positions have been updated.");
    }

    @Subcommand(parentLabel = "arena", label = "setcorner")
    public void setcorner(Player player, Integer pos, String name) {
        if (pos != 1 && pos != 2) {
            player.sendMessage(ChatColor.RED + "Usage: /arena setcorner <1|2> <name>");
        }

        final Arena arena = this.arenaHandler.findArena(name);

        if (arena == null) {
            player.sendMessage(ChatColor.RED + "This does not arena exist.");
            return;
        }

        if (pos == 1) {
            arena.setCorners(player.getLocation(), null);
        } else {
            arena.setCorners(null, player.getLocation());
        }

        player.sendMessage(Color.SECONDARY_COLOR + "The arena " + Color.PRIMARY_COLOR + arena.getName() + Color.SECONDARY_COLOR + "'s corners have been updated.");
    }

    @Subcommand(parentLabel = "arena", label = "prepare")
    public void prepare(Player player, String name, Integer amount) {
        final Arena arena = this.arenaHandler.findArena(name);

        if (arena == null) {
            player.sendMessage(ChatColor.RED + "This does not arena exist.");
            return;
        }

        player.sendMessage(Color.SECONDARY_COLOR + ChatColor.ITALIC.toString() + "Preparing arena templates for " + name + ", this might take a while...");

        for (int i = 0; i < amount; i++) {
            new ArenaTemplate(arena, false).build();
        }

        player.sendMessage(Color.SECONDARY_COLOR + "You have prepared " + Color.PRIMARY_COLOR + amount + Color.SECONDARY_COLOR + " arenas.");
    }

    @Subcommand(parentLabel = "arena", label = "icon")
    public void icon(Player player, String name, Material icon) {
        final Arena arena = this.arenaHandler.findArena(name);

        if (arena == null) {
            player.sendMessage(ChatColor.RED + "This does not arena exist.");
            return;
        }

        arena.setIcon(icon);
        player.sendMessage(Color.SECONDARY_COLOR + "You have updated the icon of the " + Color.PRIMARY_COLOR + arena.getName() + Color.SECONDARY_COLOR + " arena.");
    }

    @Subcommand(parentLabel = "arena", label = "list")
    public void list(Player player) {
        new ArenaMenu(player).updateMenu();
    }
}