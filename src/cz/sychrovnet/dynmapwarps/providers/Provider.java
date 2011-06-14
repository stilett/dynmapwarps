package cz.sychrovnet.dynmapwarps.providers;

import cz.sychrovnet.dynmapwarps.DynmapWarps;
import cz.sychrovnet.dynmapwarps.Mark;

import java.util.List;

/**
 * User: stilett
 * Date: 6/10/11
 * Time: 1:45 PM
 */
public abstract class Provider {
    private DynmapWarps plugin;

    public Provider(DynmapWarps plugin) {
        this.plugin = plugin;
    }

    public abstract boolean isEnabled();
    public abstract void onEnable();
    public abstract void onDisable();
    public abstract void setMarks(List<Mark> marks);

    public DynmapWarps getPlugin() {
        return plugin;
    }
}
