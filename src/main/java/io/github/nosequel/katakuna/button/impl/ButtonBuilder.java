package io.github.nosequel.katakuna.button.impl;

import io.github.nosequel.katakuna.button.Button;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@Getter
public class ButtonBuilder implements Button {

    private BiConsumer<ClickType, Player> action;

    private List<String> lore = new ArrayList<>();

    private Material material;

    private String displayName = "";
    private byte data = 0;
    private int index = 0;
    private int amount = 0;


    /**
     * Constructor for creating a new ButtonBuilder with default values
     *
     * @param material the material
     */
    public ButtonBuilder(Material material) {
        this.material = material;
        this.action = (type, player) -> {
        };
    }

    /**
     * Constructor for creating a new ButtonBuilder with the values pre set from a {@link Button}
     *
     * @param button the button
     */
    public ButtonBuilder(Button button) {
        this.material = button.getMaterial();
        this.action = button.getAction();
        this.lore = button.getLore();
        this.data = button.getData();
        this.amount = button.getAmount();
        this.displayName = button.getDisplayName();
        this.index = button.getIndex();
    }

    /**
     * Constructor for creating a new ButtonBuilder with the values pre set from a {@link ItemStack}
     *
     * @param itemStack the itemstack to load it from
     */
    public ButtonBuilder(ItemStack itemStack) {
        this.material = itemStack.getType();
        this.data = itemStack.getData().getData();
        this.amount = itemStack.getAmount();
        this.displayName = itemStack.getItemMeta().getDisplayName();
        this.lore = itemStack.getItemMeta().getLore();
    }

    /**
     * Set the index of the Button
     *
     * @param index the new index
     * @return the current ButtonBuilder instance
     */
    public ButtonBuilder setIndex(int index) {
        this.index = index;

        return this;
    }

    @Override
    public void index(int index) {
        this.index = index;
    }

    /**
     * Set the display name of the Button
     *
     * @param displayName the new index
     * @return the current ButtonBuilder instance
     */
    public ButtonBuilder setDisplayName(String displayName) {
        this.displayName = displayName;

        return this;
    }

    /**
     * Set the material of the Button
     *
     * @param material the new index
     * @return the current ButtonBuilder instance
     */
    public ButtonBuilder setMaterial(Material material) {
        this.material = material;

        return this;
    }

    /**
     * Set the action of the Button
     *
     * @param action the new index
     * @return the current ButtonBuilder instance
     */
    public ButtonBuilder setAction(BiConsumer<ClickType, Player> action) {
        this.action = action;

        return this;
    }

    /**
     * Set the lore of a Button
     *
     * @param lore the lore
     * @return the current ButtonBuilder instance
     */
    public ButtonBuilder setLore(String... lore) {
        this.lore = new ArrayList<>(Arrays.asList(lore));

        return this;
    }

    /**
     * Sets the data of a Button
     *
     * @param data the data
     * @return the current ButtonBuilder instance
     */
    public ButtonBuilder setData(byte data) {
        this.data = data;

        return this;
    }

    public ButtonBuilder setAmount(int amount) {
        this.amount = amount;

        return this;
    }
}