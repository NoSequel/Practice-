package io.github.nosequel.practice.arena;

import com.google.gson.JsonObject;
import io.github.nosequel.practice.PracticePlugin;
import io.github.nosequel.practice.arena.schematic.ArenaSchematicImpl;
import io.github.nosequel.practice.arena.schematic.ArenaTemplate;
import io.github.nosequel.practice.util.LocationUtil;
import io.github.nosequel.practice.util.json.JsonAppender;
import io.github.nosequel.practice.util.json.JsonSerializable;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class Arena extends JsonSerializable {

    private final String name;
    private final UUID uuid;
    private final ArenaSchematicImpl schematic;

    private final List<ArenaTemplate> templates = new ArrayList<>();

    private Location location1;
    private Location location2;

    private Location corner1;
    private Location corner2;

    @Setter
    private Material icon = Material.GRASS;

    /**
     * Constructor for making a new Arena
     *
     * @param uuid the unique identifier for the arena
     * @param name the name of the arena
     */
    public Arena(UUID uuid, String name) {
        super(null);

        this.uuid = uuid;
        this.name = name;
        this.schematic = (ArenaSchematicImpl) PracticePlugin.getPlugin().getSchematicController().createSchematic(this.name);
    }

    /**
     * Constructor for loading an arena from a jsonobject
     *
     * @param object the json object
     */
    public Arena(JsonObject object) {
        super(object);

        this.uuid = UUID.fromString(object.get("uuid").getAsString());
        this.name = object.get("name").getAsString();
        this.location1 = LocationUtil.deserializeLocation(object.get("location1").getAsString());
        this.location2 = LocationUtil.deserializeLocation(object.get("location2").getAsString());
        this.corner1 = LocationUtil.deserializeLocation(object.get("corner1").getAsString());
        this.corner2 = LocationUtil.deserializeLocation(object.get("corner2").getAsString());
        this.icon = Material.valueOf(object.get("icon").getAsString());

        this.schematic = (ArenaSchematicImpl) PracticePlugin.getPlugin().getSchematicController().findSchematic(this.name);
    }

    @Override
    public JsonObject toJson() {
        return new JsonAppender()
                .append("uuid", this.uuid.toString())
                .append("name", this.name)
                .append("location1", LocationUtil.serializeLocation(location1))
                .append("location2", LocationUtil.serializeLocation(location2))
                .append("corner1", LocationUtil.serializeLocation(corner1))
                .append("corner2", LocationUtil.serializeLocation(corner2))
                .append("icon", this.icon.name()).get();
    }

    /**
     * Set the locations of an arena
     *
     * @param location1 the location array
     * @return the arena instance
     */
    public Arena setLocations(Location location1, Location location2) {
        this.location1 = location1 == null ? this.location1 : location1;
        this.location2 = location2 == null ? this.location2 : location2;

        return this;
    }

    /**
     * Set the corners of an arena
     *
     * @param location1 the location array
     * @return the arena instance
     */
    public Arena setCorners(Location location1, Location location2) {
        this.corner1 = location1 == null ? this.corner1 : location1;
        this.corner2 = location2 == null ? this.corner2 : location2;

        if (this.corner1 != null && this.corner2 != null) {
            LocationUtil.setupSchematic(schematic, corner1, corner2);
        }

        return this;
    }

    /**
     * Get an arena template
     *
     * @return the template
     */
    public ArenaTemplate getTemplate() {
        final ArenaTemplate preparedTemplate = this.templates.stream()
                .filter(ArenaTemplate::isUsable)
                .findFirst().orElse(null);

        if (preparedTemplate != null && !preparedTemplate.isBuilt() && preparedTemplate.isUsable()) {
            preparedTemplate.build();
        }

        return preparedTemplate == null ? new ArenaTemplate(this, false).build() : preparedTemplate;
    }

    /**
     * Check if the arena is usable
     *
     * @return {@link Boolean} whether the arena is usable or not
     */
    public boolean isUsable() {
        return this.corner1 != null && this.corner2 != null && this.location1 != null && this.location2 != null;
    }
}