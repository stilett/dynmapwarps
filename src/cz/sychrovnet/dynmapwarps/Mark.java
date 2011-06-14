package cz.sychrovnet.dynmapwarps;

import org.bukkit.Location;
import org.bukkit.World;

/**
 * User: stilett
 * Date: 6/10/11
 * Time: 1:46 PM
 */
public class Mark {
    public String world;
    public String group;
    public String name;
    public String icon;
    public Location location;

    public Mark(String world, String group, String name, String icon, Location location) {
        this.world = world;
        this.group = group;
        this.name = name;
        this.icon = icon;
        this.location = location;
    }

    public String getUniqueId() {
        return this.world + "//" + this.group + "//" + this.name;
    }
}
