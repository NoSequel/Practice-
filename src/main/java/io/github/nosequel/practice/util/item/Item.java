package io.github.nosequel.practice.util.item;

import io.github.nosequel.practice.PracticePlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Item implements Listener {

    private final ItemStack itemStack;
    private final int index;

    private Consumer<Player> interact;

    /**
     * Constructor for creating a new item object
     *
     * @param itemStack the type of the item
     */
    public Item(ItemStack itemStack, int index) {
        this.itemStack = itemStack;
        this.index = index;

        Bukkit.getPluginManager().registerEvents(this, PracticePlugin.getPlugin());
    }

    /**
     * Set the interact action of an item
     *
     * @param interact the interact action
     * @return the current instance of the item
     */
    public Item interact(Consumer<Player> interact) {
        this.interact = interact;
        return this;
    }

    /**
     * Set the display name of an item
     *
     * @param displayName the display name
     * @return the current instance of the item
     */
    public Item displayName(String displayName) {
        final ItemMeta meta = this.itemStack.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));
        this.itemStack.setItemMeta(meta);

        return this;
    }

    /**
     * Set the lore of an item
     *
     * @param lore the lore
     * @return the current instance of the item
     */
    public Item lore(String... lore) {
        final ItemMeta meta = this.itemStack.getItemMeta();
        meta.setLore(Arrays.stream(lore).map(string -> ChatColor.translateAlternateColorCodes('&', string)).collect(Collectors.toList()));
        this.itemStack.setItemMeta(meta);

        return this;
    }

    /**
     * Give the item to a player
     *
     * @param player the player to give it to
     */
    public void give(Player player) {
        if (this.index == -1) {
            player.getInventory().addItem(this.itemStack);
        } else {
            player.getInventory().setItem(this.index, this.itemStack);
        }

        player.updateInventory();
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getItem() != null && event.getItem().isSimilar(this.itemStack) && this.interact != null) {
            this.interact.accept(event.getPlayer());
        }
    }
}