package io.github.nosequel.practice.util;

import io.github.nosequel.practice.PracticePlugin;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Getter
public class Config {

    private final File file;
    private final YamlConfiguration configuration;

    /**
     * Constructor for making a new config
     *
     * @param name the name of the config
     */
    public Config(String name) {
        this.file = new File(
                PracticePlugin.getPlugin().getDataFolder(),
                name.contains(".yml") ? name : name + ".yml"
        );

        if (!file.getParentFile().exists()) {
            if (!file.mkdir()) {
                System.out.println("Failed to create parent folder");
            }
        }

        this.configuration = YamlConfiguration.loadConfiguration(file);
    }

    public void save() {
        try {
            this.configuration.save(this.file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get a string from the configuration file
     *
     * @param path the path
     * @return the found string
     */
    public String getString(String path) {
        return configuration.getString(path);
    }

    /**
     * Get a configuration section from the config file
     *
     * @param path the path
     * @return the found configuration section
     */
    public ConfigurationSection getConfigurationSection(String path) {
        return configuration.getConfigurationSection(path);
    }

    /**
     * Get a string list from the configuration file
     *
     * @param path the path of the string list
     * @return the found string list
     */
    public List<String> getStringList(String path) {
        return configuration.getStringList(path);
    }

    /**
     * Get a boolean from the configuration file
     *
     * @param path the path of the boolean
     * @return the found boolean
     */
    public boolean getBoolean(String path) {
        return configuration.getBoolean(path);
    }

    /**
     * Get an integer from the configuration file
     *
     * @param path the path of the integer
     * @return the found integer
     */
    public Integer getInteger(String path) {
        return configuration.getInt(path);
    }

    /**
     * Get a double from a configuration file
     *
     * @param path the path of th double
     * @return the double found
     */
    public Double getDouble(String path) {
        return configuration.getDouble(path);
    }

    /**
     * Get a long from a configuration file
     *
     * @param path the path of the long
     * @return the long found
     */
    public Long getLong(String path) {
        return configuration.getLong(path);
    }

}
