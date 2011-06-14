package cz.sychrovnet.dynmapwarps.providers;

import cz.sychrovnet.dynmapwarps.DynmapWarps;
import cz.sychrovnet.dynmapwarps.Mark;
import net.TheDgtl.Warpz0r.Locations;
import net.TheDgtl.Warpz0r.Warpz0r;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

/**
 * Adds Warpoz0r support. Warpz0r homes and warps can be exported.
 *
 * User: stilett
 * Date: 6/10/11
 * Time: 1:57 PM
 */

public class Warpz0rProvider extends Provider {
    Warpz0r warpz0r;
    public static final String iconWarp = "images/warp-small.png";
    public static final String iconHome = "images/home-small.png";

    public Warpz0rProvider(DynmapWarps plugin) {
        super(plugin);
    }

    @Override
    public boolean isEnabled() {
        return warpz0r != null;
    }

    @Override
    public void onEnable() {
        warpz0r = (Warpz0r) this.getPlugin().getServer().getPluginManager().getPlugin("Warpz0r");
    }

    @Override
    public void onDisable() {
        warpz0r = null;
    }

    @Override
    public void setMarks(List<Mark> marks) {
        if (!this.getPlugin().disabledWarps) {
            // warps
            for (Locations.Warp warp: Locations.warps.values()) {
                marks.add(new Mark(warp.world, "warpz0rWarps", warp.fullName, iconWarp, warp.loc));
            }
        }

        if (!this.getPlugin().disabledHomes) {
            // homes
            for (Locations.Warp warp: Locations.homes.values()) {
                marks.add(new Mark(warp.world, "warpz0rHomes", warp.fullName, iconHome, warp.loc));
            }
        }
    }
}
