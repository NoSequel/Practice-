package io.github.nosequel.practice.kit;

import io.github.nosequel.practice.handler.Handler;
import io.github.nosequel.practice.util.Config;
import io.github.nosequel.practice.util.json.JsonUtil;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

@Getter
public class KitHandler implements Handler {

    private final List<Kit> kits = new ArrayList<>();
    private Config config;

    @Override
    public void load() {
        this.config = new Config("kits");
        
        final ConfigurationSection section = this.config.getConfiguration().getConfigurationSection("kits");
        if (section != null) {
            section.getKeys(false).forEach(key -> this.kits.add(new Kit(JsonUtil.parser.parse(this.config.getConfiguration().getString("kits." + key)).getAsJsonObject())));
        }
    }

    @Override
    public void unload() {
        if (this.config.getConfiguration() != null) {
            this.kits.forEach(kit -> this.config.getConfiguration().set("kits." + kit.getUuid().toString(), kit.toJson().toString()));
            this.config.save();
        }
    }

    public Kit findKit(String kit) {
        return this.kits.stream()
                .filter(current -> current.getName().equalsIgnoreCase(kit))
                .findFirst().orElse(null);
    }
}