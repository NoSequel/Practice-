package io.github.nosequel.practice.kit;

import com.google.gson.JsonObject;
import io.github.nosequel.practice.PracticePlugin;
import io.github.nosequel.practice.kit.loadout.menu.KitLoadoutSelectionMenu;
import io.github.nosequel.practice.player.Profile;
import io.github.nosequel.practice.player.ProfileHandler;
import io.github.nosequel.practice.util.ItemStackUtil;
import io.github.nosequel.practice.util.json.JsonAppender;
import io.github.nosequel.practice.util.json.JsonSerializable;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
public class Kit extends JsonSerializable {

    private final UUID uuid;
    private final String name;

    private ItemStack icon = new ItemStack(Material.IRON_SWORD);
    private ItemStack[] items = new ItemStack[]{};
    private ItemStack[] armor = new ItemStack[]{};

    /**
     * Constructor for making a new kit
     *
     * @param uuid the unique identifier of the kit
     * @param name the name of the kit
     */
    public Kit(UUID uuid, String name) {
        super(null);
        this.uuid = uuid;
        this.name = name;
    }

    /**
     * Constructor for loading a kit from a json object
     *
     * @param object the json object
     */
    public Kit(JsonObject object) {
        super(object);

        this.uuid = UUID.fromString(object.get("uuid").getAsString());
        this.name = object.get("name").getAsString();
        this.items = ItemStackUtil.deserializeItemStack(object.get("contents").getAsString());
        this.armor = ItemStackUtil.deserializeItemStack(object.get("armor").getAsString());
        this.icon = Objects.requireNonNull(ItemStackUtil.deserializeItemStack(object.get("icon").getAsString()))[0];
    }

    @Override
    public JsonObject toJson() {
        return new JsonAppender()
                .append("uuid", this.uuid.toString())
                .append("name", this.name)
                .append("icon", ItemStackUtil.serializeItemStack(new ItemStack[] { this.icon }))
                .append("contents", ItemStackUtil.serializeItemStack(this.items))
                .append("armor", ItemStackUtil.serializeItemStack(this.armor))
                .get();
    }

    /**
     * Set the armor of a kit and automatically update it's loadouts
     *
     * @param armor the new armor
     */
    public void setArmor(ItemStack[] armor) {
        this.armor = armor;

        PracticePlugin.getPlugin().getHandler().find(ProfileHandler.class)
                .getProfiles().values().stream()
                .filter(profile -> !profile.getLoadouts(this).isEmpty())
                .forEach(profile -> profile.getLoadouts().remove(this));
    }

    /**
     * Set the contents of a kit and automatically update it's loadouts
     *
     * @param items the new contents
     */
    public void setItems(ItemStack[] items) {
        this.items = items;

        PracticePlugin.getPlugin().getHandler().find(ProfileHandler.class)
                .getProfiles().values().stream()
                .filter(profile -> !profile.getLoadouts(this).isEmpty())
                .forEach(profile -> profile.getLoadouts().remove(this));
    }

    /**
     * Method for equipping the kit to a player
     *
     * @param player the player
     */
    public void equip(Player player) {
        final Profile profile = PracticePlugin.getPlugin().getHandler().find(ProfileHandler.class).findProfile(player.getUniqueId());

        if (profile.getLoadouts(this).size() == 1) {
            profile.getLoadouts(this).get(0).equip(player);
        } else {
            new KitLoadoutSelectionMenu(this, player).updateMenu();
        }
    }
}
