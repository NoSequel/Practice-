package io.github.nosequel.practice.util.command.adapter.defaults;

import io.github.nosequel.practice.util.command.adapter.TypeAdapter;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;

public class MaterialTypeAdapter implements TypeAdapter<Material> {

    @Override
    public Material convert(CommandSender sender, String source) throws Exception {
        return Material.getMaterial(source);
    }

    @Override
    public Class<Material> getType() {
        return Material.class;
    }
}
