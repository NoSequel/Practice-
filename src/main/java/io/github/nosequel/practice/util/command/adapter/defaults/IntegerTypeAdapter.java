package io.github.nosequel.practice.util.command.adapter.defaults;

import io.github.nosequel.practice.util.command.adapter.TypeAdapter;
import org.bukkit.command.CommandSender;

public class IntegerTypeAdapter implements TypeAdapter<Integer> {

    @Override
    public Integer convert(CommandSender sender, String source) {
        return Integer.parseInt(source);
    }

    @Override
    public Class<Integer> getType() {
        return Integer.class;
    }
}
