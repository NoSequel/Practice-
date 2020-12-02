package io.github.nosequel.practice.kit.loadout.menu;

import io.github.nosequel.katakuna.button.Button;
import io.github.nosequel.katakuna.button.impl.ButtonBuilder;
import io.github.nosequel.katakuna.menu.paginated.PaginatedMenu;
import io.github.nosequel.practice.PracticePlugin;
import io.github.nosequel.practice.kit.Kit;
import io.github.nosequel.practice.player.Profile;
import io.github.nosequel.practice.player.ProfileHandler;
import io.github.nosequel.practice.util.Color;
import io.github.nosequel.practice.util.WoolColor;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class KitLoadoutEditSelectLoadoutMenu extends PaginatedMenu {

    private final Kit kit;
    private final Profile profile;

    public KitLoadoutEditSelectLoadoutMenu(Player player, Kit kit) {
        super(player, "Select Loadout", 9);

        this.kit = kit;
        this.profile = PracticePlugin.getPlugin().getHandler().find(ProfileHandler.class).findProfile(player.getUniqueId());
    }

    @Override
    public List<Button> getButtons() {
        final List<Button> buttons = new ArrayList<>();
        final AtomicInteger index = new AtomicInteger(2);

        buttons.add(new ButtonBuilder(Material.WOOL)
                .setData(WoolColor.getWoolColor(ChatColor.GREEN))
                .setDisplayName(ChatColor.GREEN + "New loadout")
                .setLore(Color.SECONDARY_COLOR + "Click to make a new loadout")
                .setIndex(0)
                .setAction((type, player) -> new KitLoadoutEditMenu(player, kit).updateMenu())
        );

        buttons.add(new ButtonBuilder(new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.GRAY.getData())).setIndex(1));

        buttons.addAll(profile.getLoadouts(kit).stream()
                .map(loadout -> new ButtonBuilder(loadout.getIcon())
                        .setIndex(index.getAndIncrement())
                        .setDisplayName(Color.PRIMARY_COLOR + loadout.getName())
                        .setLore(Color.SECONDARY_COLOR + "Click to edit this loadout")
                        .setAction((type, player) -> new KitLoadoutEditMenu(player, loadout).updateMenu())
                ).collect(Collectors.toList()));

        return buttons;
    }
}