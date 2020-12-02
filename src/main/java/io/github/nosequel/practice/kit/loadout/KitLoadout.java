package io.github.nosequel.practice.kit.loadout;

import io.github.nosequel.practice.kit.Kit;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Getter
@Setter
public class KitLoadout {

    private final Kit kit;
    private final long createDate;

    private ItemStack[] contents;
    private ItemStack[] armor;

    private Material icon = Material.BOOK;

    private String name;

    /**
     * Constructor to make a new kit loadout object
     *
     * @param name the name of the loadout
     * @param kit  the kit to load the default items from
     */
    public KitLoadout(String name, Kit kit) {
        this.name = name;
        this.kit = kit;
        this.createDate = System.currentTimeMillis();
        this.contents = kit.getItems();
        this.armor = kit.getArmor();
    }

    /**
     * Equip the loadout to the player
     *
     * @param player the player to equip the loadout to
     */
    public void equip(Player player) {
        player.sendMessage(ChatColor.YELLOW + "You have equipped the " + this.name + " loadout for the " + this.kit.getName() + " kit.");

        player.getInventory().setContents(this.contents);
        player.getInventory().setArmorContents(this.armor);
    }
}