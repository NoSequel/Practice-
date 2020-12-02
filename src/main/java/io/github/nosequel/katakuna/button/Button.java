package io.github.nosequel.katakuna.button;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public interface Button {

    /**
     * Get the index of the button in the menu
     *
     * @return the index
     */
    int getIndex();

    /**
     * Set the index of the button in the menu
     *
     * @param index the index
     */
    void index(int index);

    /**
     * Get the display name of the button
     *
     * @return the display name
     */
    default String getDisplayName() {
        return this.getMaterial().name();
    }

    /**
     * Get the lore of the button
     *
     * @return the lore
     */
    default List<String> getLore() {
        return Collections.emptyList();
    }

    /**
     * Get the material of the button
     *
     * @return the material
     */
    Material getMaterial();

    /**
     * Get the data of the button
     *
     * @return the data
     */
    byte getData();

    /**
     * Get the action which will be executed upon click
     *
     * @return the action
     */
    BiConsumer<ClickType, Player> getAction();

    /**
     * Get the amount of the item
     *
     * @return the amount
     */
    int getAmount();

    /**
     * Change a Button into an ItemStack
     *
     * @return the item stack
     */
    default ItemStack toItemStack() {
        final ItemStack itemStack = new ItemStack(this.getMaterial(), this.getAmount(), this.getData());
        final ItemMeta itemMeta = itemStack.getItemMeta() == null ? Bukkit.getItemFactory().getItemMeta(this.getMaterial()) : itemStack.getItemMeta();

        itemMeta.setDisplayName(this.getDisplayName());
        itemMeta.setLore(this.getLore());

        itemStack.setItemMeta(itemMeta);


        return itemStack;
    }
}