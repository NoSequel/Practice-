package io.github.nosequel.practice.arena;

import io.github.nosequel.practice.arena.schematic.ArenaTemplate;
import io.github.nosequel.practice.handler.Handler;
import io.github.nosequel.practice.util.Config;
import io.github.nosequel.practice.util.json.JsonUtil;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Getter
public class ArenaHandler implements Handler {

    private final List<Arena> arenas = new ArrayList<>();
    private Config config;

    @Override
    public void load() {
        this.config = new Config("arenas");

        final ConfigurationSection section = this.config.getConfiguration().getConfigurationSection("arenas");
        if (section != null) {
            section.getKeys(false).forEach(key -> this.arenas.add(new Arena(JsonUtil.parser.parse(this.config.getConfiguration().getString("arenas." + key)).getAsJsonObject())));
        }
    }

    @Override
    public void unload() {
        this.arenas.forEach(arena -> arena.getTemplates().forEach(ArenaTemplate::destroy));

        if (this.config.getConfiguration() != null) {
            this.arenas.forEach(arena -> this.config.getConfiguration().set("arenas." + arena.getUuid().toString(), arena.toJson().toString()));
            this.config.save();
        }
    }

    /**
     * Find an arena by their name
     *
     * @param name the name of the arena
     * @return the arena or null
     */
    public Arena findArena(String name) {
        return this.arenas.stream()
                .filter(arena -> arena.getName().equalsIgnoreCase(name))
                .findFirst().orElse(null);
    }

    /**
     * Get a random arena
     *
     * @return the random arena found
     */
    public Arena getRandomArena() {
        final List<Arena> availableArenas = this.arenas.stream()
                .filter(Arena::isUsable)
                .collect(Collectors.toList());

        return availableArenas.isEmpty() ? null : availableArenas.get(new Random().nextInt(availableArenas.size()));
    }
}
