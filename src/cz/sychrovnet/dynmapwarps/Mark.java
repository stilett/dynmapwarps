package cz.sychrovnet.dynmapwarps;

import org.bukkit.Location;
import org.bukkit.World;

/**
 * Structure representing a mark in the dynamic map.
 *
 * User: stilett
 * Date: 6/10/11
 * Time: 1:46 PM
 */
public class Mark {
    /**
     * Name of world the marks belongs to.
     */
    public String world;

    /**
     * Group (type) of marks the mark belongs to.
     */
    public String group;

    /**
     * Name of mark in the map. It will be displayed on the map.
     */
    public String name;

    /**
     * Icon file representing mark on the map. The path is relative to dynmap/web folder.
     * It is recommended to place icon files to dynmap/web/images folder.
     */
    public String icon;

    /**
     * Location of the mark in Minecraft coordinates.
     */
    public Location location;

    public Mark(String world, String group, String name, String icon, Location location) {
        this.world = world;
        this.group = group;
        this.name = name;
        this.icon = icon;
        this.location = location;
    }

    /**
     * Returns the unique identifier of the mark consiting of the mark world, group and name.
     * @return unique identifier
     */
    public String getUniqueId() {
        return this.world + "//" + this.group + "//" + this.name;
    }
}
