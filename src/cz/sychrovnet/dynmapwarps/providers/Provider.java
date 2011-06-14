package cz.sychrovnet.dynmapwarps.providers;

import cz.sychrovnet.dynmapwarps.DynmapWarps;
import cz.sychrovnet.dynmapwarps.Mark;

import java.util.List;

/**
 * Base class for integration with warp and home plugins.
 * Extend this class to add ability to export marks from other plugins.
 *
 * User: stilett
 * Date: 6/10/11
 * Time: 1:45 PM
 */
public abstract class Provider {
    private DynmapWarps plugin;

    public Provider(DynmapWarps plugin) {
        this.plugin = plugin;
    }

    /**
     * Returns whether the plugin is enabled.
     * @return true if the plugin is enabled
     */
    public abstract boolean isEnabled();

    /**
     * Action to perform when the provider is enabled.
     */
    public abstract void onEnable();

    /**
     * Action to perform when the provider is disabled.
     */
    public abstract void onDisable();

    /**
     * In this method the provider should add its marks to the list.
     *
     * @param marks List the marks should be added to
     */
    public abstract void setMarks(List<Mark> marks);

    public DynmapWarps getPlugin() {
        return plugin;
    }
}
