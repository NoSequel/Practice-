package io.github.nosequel.practice.util.loadout.impl;

import io.github.nosequel.practice.kit.loadout.menu.KitLoadoutEditSelectMenu;
import io.github.nosequel.practice.player.ProfileState;
import io.github.nosequel.practice.queue.menu.SoloQueueUnrankedMenu;
import io.github.nosequel.practice.util.item.Item;
import io.github.nosequel.practice.util.loadout.Loadout;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class LobbyLoadout implements Loadout {

    private final Item[] lobbyItems = new Item[]{
            new Item(new ItemStack(Material.STONE_SWORD), 0).displayName("&3Join Unranked Queue").interact(player -> new SoloQueueUnrankedMenu(player).updateMenu()),
            //new Item(new ItemStack(Material.IRON_SWORD), 1).displayName("&aJoin Ranked Queue").interact(player -> player.sendMessage(ChatColor.RED + "Coming soon.")),
            //new Item(new ItemStack(Material.DIAMOND), 4).displayName("&aCreate a Party").interact(player -> player.sendMessage(ChatColor.RED + "Coming soon.")),
            new Item(new ItemStack(Material.BOOK), 8).displayName("&aEdit Kits").interact(player -> new KitLoadoutEditSelectMenu(player).updateMenu()),
    };

    @Override
    public void equip(Player player) {
        this.refresh(player);

        Arrays.stream(lobbyItems)
                .forEach(item -> item.give(player));
    }

    @Override
    public ProfileState getState() {
        return ProfileState.LOBBY;
    }
}
