package io.github.nosequel.practice.kit.loadout.menu;

import io.github.nosequel.katakuna.button.Button;
import io.github.nosequel.katakuna.button.impl.ButtonBuilder;
import io.github.nosequel.katakuna.menu.Menu;
import io.github.nosequel.practice.PracticePlugin;
import io.github.nosequel.practice.kit.Kit;
import io.github.nosequel.practice.kit.loadout.KitLoadout;
import io.github.nosequel.practice.player.Profile;
import io.github.nosequel.practice.player.ProfileHandler;
import io.github.nosequel.practice.player.ProfileState;
import io.github.nosequel.practice.util.Color;
import io.github.nosequel.practice.util.WoolColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class KitLoadoutEditMenu extends Menu {

    private final KitLoadout loadout;
    private final Profile profile;
    private final Kit kit;

    public KitLoadoutEditMenu(Player player, Kit kit) {
        super(player, "Editing " + kit.getName(), 45);

        this.loadout = null;
        this.kit = kit;
        this.profile = PracticePlugin.getPlugin().getHandler().find(ProfileHandler.class).findProfile(player.getUniqueId());
    }

    public KitLoadoutEditMenu(Player player, KitLoadout loadout) {
        super(player, "Editing " + loadout.getName(), 45);

        this.loadout = loadout;
        this.kit = loadout.getKit();
        this.profile = PracticePlugin.getPlugin().getHandler().find(ProfileHandler.class).findProfile(player.getUniqueId());
    }

    @Override
    public List<Button> getButtons() {
        final List<Button> buttons = new ArrayList<>();
        final AtomicInteger index = new AtomicInteger(9);

        IntStream.range(1, 8).forEach(i -> buttons.add(new ButtonBuilder(new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.GRAY.getData())).setIndex(i)));

        buttons.add(new ButtonBuilder(Material.WOOL)
                .setIndex(0)
                .setData(WoolColor.getWoolColor(ChatColor.GREEN))
                .setDisplayName(ChatColor.GREEN + "Click to save new loadout")
                .setAction((type, player) -> this.saveLoadout(player))
        );

        if (this.loadout != null) {
            buttons.add(new ButtonBuilder(Material.WOOL)
                    .setIndex(3)
                    .setDisplayName(Color.SECONDARY_COLOR + "Click to change icon")
                    .setData(WoolColor.getWoolColor(Color.SECONDARY_COLOR))
                    .setAction((type, player) -> {
                        profile.setState(ProfileState.LOBBY);
                        Bukkit.getScheduler().runTaskLater(PracticePlugin.getPlugin(), () -> new KitLoadoutSetIconMenu(player, loadout).updateMenu(), 2L);
                    })
            );

            buttons.add(new ButtonBuilder(Material.WOOL)
                    .setIndex(4)
                    .setData(WoolColor.getWoolColor(ChatColor.RED))
                    .setDisplayName(ChatColor.RED + "Click to delete loadout")
                    .setAction((type, player) -> this.deleteLoadout(player))
            );

            buttons.add(new ButtonBuilder(Material.WOOL)
                .setIndex(5)
                    .setDisplayName(Color.SECONDARY_COLOR + "Click to duplicate the kit")
                    .setData(WoolColor.getWoolColor(Color.SECONDARY_COLOR))
                    .setAction((type, player) -> this.duplicateLoadout(player))
            );
        }

        buttons.add(new ButtonBuilder(Material.WOOL)
                .setIndex(8)
                .setData(WoolColor.getWoolColor(ChatColor.GOLD))
                .setDisplayName(ChatColor.GOLD + "Click to clear your cursor")
                .setAction(((clickType, player) -> player.setItemOnCursor(null)))
        );

        buttons.addAll(Arrays.stream(kit.getItems()).
                map(itemStack -> new ButtonBuilder(itemStack)
                        .setIndex(index.getAndIncrement())
                        .setAction((clickType, player) -> this.handleItemClick(player, itemStack, clickType))
                ).collect(Collectors.toList()));

        return buttons;
    }

    private void handleItemClick(Player player, ItemStack itemStack, ClickType clickType) {
        if (clickType.equals(ClickType.DOUBLE_CLICK)) {
            return;
        }
        if (clickType.isShiftClick()) {
            player.getInventory().addItem(itemStack);
        } else {
            player.setItemOnCursor(itemStack);
        }
    }

    /**
     * Save a loadout
     *
     * @param player the player which has to save it
     */
    private void saveLoadout(Player player) {
        final String name = this.loadout == null ? kit.getName() + " #" + profile.getLoadouts(kit).size() : this.loadout.getName();
        final KitLoadout loadout = this.loadout == null ? new KitLoadout(name, kit) : this.loadout;

        loadout.setArmor(kit.getArmor());
        loadout.setContents(IntStream.range(0, 36).boxed().map(player.getInventory()::getItem).toArray(ItemStack[]::new));

        if(this.loadout == null) {
            profile.addLoadout(this.kit, loadout);
        }

        player.closeInventory();
        player.sendMessage(Color.SECONDARY_COLOR + "You have saved the " + Color.PRIMARY_COLOR + name + Color.SECONDARY_COLOR + " kit loadout.");
    }

    /**
     * Duplicate a loadout
     *
     * @param player the player who duplicated the loadout
     */
    private void duplicateLoadout(Player player) {
        final String name = kit.getName() + " #" + profile.getLoadouts(kit).size();
        final KitLoadout loadout = new KitLoadout(name, this.kit);

        if (this.loadout != null) {
            loadout.setArmor(this.loadout.getArmor());
            loadout.setContents(this.loadout.getContents());
            loadout.setIcon(this.loadout.getIcon());
        }

        profile.addLoadout(this.kit, loadout);

        player.closeInventory();
        player.sendMessage(Color.SECONDARY_COLOR + "You have duplicated the " + Color.PRIMARY_COLOR + name + Color.SECONDARY_COLOR + " loadout.");
    }

    /**
     * Delete a loadout
     *
     * @param player the player who deleted it
     */
    private void deleteLoadout(Player player) {
        profile.removeLoadout(loadout);

        player.closeInventory();
        player.sendMessage(Color.SECONDARY_COLOR + "You have deleted the " + Color.PRIMARY_COLOR + loadout.getName() + Color.SECONDARY_COLOR + " kit loadout.");
    }

    @Override
    public void updateMenu(List<Button> buttons) {
        super.updateMenu(buttons);

        getPlayer().getInventory().setContents(new ItemStack[]{null});

        if (this.loadout != null) {
            getPlayer().getInventory().setContents(this.loadout.getContents());
        }
    }

    @Override
    public void onClose(InventoryCloseEvent event) {
        Bukkit.getScheduler().runTaskLater(PracticePlugin.getPlugin(), () -> profile.setState(ProfileState.LOBBY), 1L);
    }
}