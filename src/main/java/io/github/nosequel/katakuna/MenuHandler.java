package io.github.nosequel.katakuna;


import io.github.nosequel.katakuna.listeners.ButtonListener;
import io.github.nosequel.katakuna.menu.Menu;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
public class MenuHandler {

    private static MenuHandler instance;

    private final Map<Player, Menu> menus = new HashMap<>();
    private final JavaPlugin plugin;

    /**
     * Constructor for creating a new MenuHandler instance
     *
     * @param plugin the plugin the listener gets registered to
     */
    public MenuHandler(JavaPlugin plugin) {
        instance = this;
        this.plugin = plugin;

        Bukkit.getPluginManager().registerEvents(new ButtonListener(), plugin);
    }

    /**
     * Find a menu by a player
     *
     * @param player the player
     * @return the found menu or null
     */
    public Menu findMenu(Player player) {
        return menus.get(player);
    }

    /**
     * Get the current instance of the MenuHandler
     *
     * @return the instance
     */
    public static MenuHandler get() {
        return instance;
    }

}
