package io.github.nosequel.practice.arena.schematic;

import io.github.nosequel.schematic.impl.BasicSchematic;
import org.bukkit.Location;
import org.bukkit.Material;

public class ArenaSchematicImpl extends BasicSchematic {

    public ArenaSchematicImpl(String name) {
        super(name);
    }

    @Override
    public void build(Location location) {
        super.build(location);
    }

    /**
     * Destroy the schematic
     *
     * @param location the location to destroy it at
     */
    public void destroy(Location location) {
        this.getBlocks().forEach(block -> block.getRelativeLocation(location).getBlock().setType(Material.AIR));
    }

}
