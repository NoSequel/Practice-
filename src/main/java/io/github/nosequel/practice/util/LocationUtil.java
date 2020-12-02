package io.github.nosequel.practice.util;

import io.github.nosequel.schematic.Schematic;
import io.github.nosequel.schematic.block.SchematicBlock;
import lombok.experimental.UtilityClass;
import org.bukkit.*;
import org.bukkit.block.Block;

@UtilityClass
public class LocationUtil {

    public Location NEXT_ARENA_TEMPLATE_LOCATION = new Location(getArenasWorld(), 0, 100, 0);

    /**
     * Serialize a location to a string
     *
     * @param location the location
     * @return the string
     */
    public String serializeLocation(Location location) {
        return String.join(",", new String[]{
                location.getWorld().getName(),
                String.valueOf(location.getX()),
                String.valueOf(location.getY()),
                String.valueOf(location.getZ()),
                String.valueOf(location.getYaw()),
                String.valueOf(location.getPitch())
        });
    }

    /**
     * Deserialize a location from a string
     *
     * @param string the string to deserialize the location from
     * @return the {@link Location}
     */
    public Location deserializeLocation(String string) {
        final String[] split = string.split(",");
        return new Location(Bukkit.getWorld(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3]), Float.parseFloat(split[4]), Float.parseFloat(split[5]));
    }

    /**
     * Get the arenas world
     *
     * @return
     */
    public World getArenasWorld() {
        if (Bukkit.getWorld("arenas") == null) {
            Bukkit.createWorld(new WorldCreator("arenas").environment(World.Environment.NORMAL));
        }

        return Bukkit.getWorld("arenas");
    }

    /**
     * Get the center of 2 locations
     *
     * @param location1  the first location
     * @param location2  thes second location
     * @param yIncrement the amount the y axis should increase
     * @return the center of the 2 provided locations
     */
    public Location getCenter(Location location1, Location location2, double yIncrement) {
        return new Location(location1.getWorld(),
                Math.max(location1.getBlockX(), location2.getBlockX()) - Math.min(location1.getBlockX(), location2.getBlockX()) / 2,
                (Math.max(location1.getBlockY(), location2.getBlockY()) - Math.min(location1.getBlockY(), location2.getBlockY()) / 2) + yIncrement,
                Math.max(location1.getBlockZ(), location2.getBlockZ()) - Math.min(location1.getBlockZ(), location2.getBlockZ()) / 2
        );
    }

    /**
     * Setup a schematic from 2 locations
     *
     * @param schematic the schematic to setup
     * @param point1    the first location
     * @param point2    the second location
     */
    public void setupSchematic(Schematic schematic, Location point1, Location point2) {
        int startX = Math.min(point1.getBlockX(), point2.getBlockX());
        int startY = Math.min(point1.getBlockY(), point2.getBlockY());
        int startZ = Math.min(point1.getBlockZ(), point2.getBlockZ());

        int dimX = Math.max(point1.getBlockX(), point2.getBlockX()) - startX;
        int dimY = Math.max(point1.getBlockY(), point2.getBlockY()) - startY;
        int dimZ = Math.max(point1.getBlockZ(), point2.getBlockZ()) - startZ;

        for (int j = 0; j <= dimY; ++j) {
            for (int i = 0; i <= dimX; ++i) {
                for (int k = 0; k <= dimZ; ++k) {
                    final Block block = new Location(point1.getWorld(), i + startX, j + startY, k + startZ).getBlock();

                    if (!block.getType().equals(Material.AIR)) {
                        schematic.getBlocks().add(new SchematicBlock(i, j, k, block.getType(), block.getData()));
                    }
                }
            }
        }
    }
}
