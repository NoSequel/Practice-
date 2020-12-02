package io.github.nosequel.practice.arena.schematic;

import io.github.nosequel.practice.arena.Arena;
import io.github.nosequel.practice.util.LocationUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.logging.Level;

@Getter
@Setter
public class ArenaTemplate {

    private final Arena arena;
    private final boolean shouldDestroy;

    private boolean built;
    private boolean usable;

    private Location buildLocation;

    private Location location1;
    private Location location2;

    /**
     * Constructor for making a new arena template
     *
     * @param arena the arena template
     */
    public ArenaTemplate(Arena arena, boolean shouldDestroy) {
        this.arena = arena;
        this.shouldDestroy = shouldDestroy;
        this.arena.getTemplates().add(this);
    }

    /**
     * Build and setup the template
     */
    public ArenaTemplate build() {
        this.buildLocation = LocationUtil.NEXT_ARENA_TEMPLATE_LOCATION.clone();
        Bukkit.getLogger().log(Level.INFO, "Building arena " + this.arena.getName() + " at ("
                + buildLocation.getBlockX() + ", "
                + buildLocation.getBlockY() +  ", "
                + buildLocation.getBlockZ() + ")");

        LocationUtil.NEXT_ARENA_TEMPLATE_LOCATION.add(1000, 0, 1000);

        this.arena.getSchematic().build(buildLocation);
        this.location1 = new Location(buildLocation.getWorld(), arena.getLocation1().getBlockX() + buildLocation.getBlockX(), arena.getLocation1().getBlockY(), arena.getLocation1().getBlockZ() + buildLocation.getBlockZ(), arena.getLocation1().getYaw(), arena.getLocation1().getPitch());
        this.location2 = new Location(buildLocation.getWorld(), arena.getLocation2().getBlockX() + buildLocation.getBlockX(), arena.getLocation2().getBlockY(), arena.getLocation2().getBlockZ() + buildLocation.getBlockZ(), arena.getLocation2().getYaw(), arena.getLocation2().getPitch());

        this.built = true;
        this.usable = true;

        return this;
    }

    /**
     * Destroy all blocks in the arena
     */
    public void destroy() {
        if (buildLocation == null) {
            throw new RuntimeException("ArenaTemplate#buildLocation returned null");
        }

        Bukkit.getLogger().log(Level.INFO, "Destroying arena " + this.arena.getName() + " at ("
                + buildLocation.getBlockX() + ", "
                + buildLocation.getBlockY() +  ", "
                + buildLocation.getBlockZ() + ")");

        this.arena.getSchematic().destroy(this.buildLocation);
        this.built = false;
    }
}
