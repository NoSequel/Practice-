package io.github.nosequel.practice.kit.command;

import io.github.nosequel.practice.PracticePlugin;
import io.github.nosequel.practice.kit.Kit;
import io.github.nosequel.practice.kit.KitHandler;
import io.github.nosequel.practice.util.Color;
import io.github.nosequel.practice.util.command.annotation.Command;
import io.github.nosequel.practice.util.command.annotation.Subcommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;
import java.util.stream.IntStream;

public class KitCommand {
    
    private final KitHandler kitHandler = PracticePlugin.getPlugin().getHandler().find(KitHandler.class);
    
    @Command(label="kit", permission = "practice.kit")
    @Subcommand(parentLabel = "kit", label="help")
    public void execute(Player player) {
        player.sendMessage(new String[]{
                Color.SECONDARY_COLOR + "------- [" + ChatColor.AQUA + "Kit Help" + Color.SECONDARY_COLOR + "] ------",
                ChatColor.AQUA + "/kit create <name>",
                ChatColor.AQUA + "/kit delete <name>",
                ChatColor.AQUA + "/kit setinv <name>",
                ChatColor.AQUA + "/kit icon <name>",
                "",
        });
    }

    @Subcommand(parentLabel = "kit", label = "create")
    public void create(Player player, String kit) {
        if (kitHandler.findKit(kit) != null) {
            player.sendMessage(ChatColor.RED + "This kit already exists.");
            return;
        }

        this.kitHandler.getKits().add(new Kit(UUID.randomUUID(), kit));
        player.sendMessage(Color.SECONDARY_COLOR + "The kit " + Color.PRIMARY_COLOR + kit + Color.SECONDARY_COLOR + " has been created.");
    }

    @Subcommand(parentLabel = "kit", label = "delete")
    public void delete(Player player, String name) {
        final Kit kit = this.kitHandler.findKit(name);

        if (kit == null) {
            player.sendMessage(ChatColor.RED + "This does not kit exist.");
            return;
        }

        this.kitHandler.getKits().remove(kit);
        player.sendMessage(Color.SECONDARY_COLOR + "The kit " + Color.PRIMARY_COLOR + kit + Color.SECONDARY_COLOR + " has been created.");
    }

    @Subcommand(parentLabel = "kit", label="setinv")
    public void setinv(Player player, String name) {
        final Kit kit = this.kitHandler.findKit(name);

        if (kit == null) {
            player.sendMessage(ChatColor.RED + "This does not kit exist.");
            return;
        }

        kit.setItems(IntStream.range(0, 36).boxed().map(player.getInventory()::getItem).toArray(ItemStack[]::new));
        kit.setArmor(player.getInventory().getArmorContents());

        player.sendMessage(Color.SECONDARY_COLOR + "The kit " + Color.PRIMARY_COLOR + kit.getName() + Color.SECONDARY_COLOR + "'s inventory has been updated.");
    }

    @Subcommand(parentLabel = "kit", label = "icon")
    public void icon(Player player, String name) {
        final Kit kit = this.kitHandler.findKit(name);

        if (kit == null) {
            player.sendMessage(ChatColor.RED + "This does not kit exist.");
            return;
        }

        if(player.getItemInHand() == null) {
            player.sendMessage(ChatColor.RED + "You must be holding an item inside of your hand.");
            return;
        }

        kit.setIcon(player.getItemInHand());

        player.sendMessage(Color.SECONDARY_COLOR + "The kit " + Color.PRIMARY_COLOR + kit.getName() + Color.SECONDARY_COLOR + "'s icon has been updated.");
    }
    
}
