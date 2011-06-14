package cz.sychrovnet.dynmapwarps;

import java.io.File;
import java.util.logging.Logger;
import cz.sychrovnet.dynmapwarps.providers.Provider;
import cz.sychrovnet.dynmapwarps.providers.Warpz0rProvider;
import org.bukkit.event.Event;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.ServerListener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;
import org.dynmap.DynmapPlugin;

import java.util.ArrayList;
import java.util.List;

/**
 * User: stilett
 * Date: 6/10/11
 * Time: 12:58 PM
 */
public class DynmapWarps extends JavaPlugin {
    public static Logger log = Logger.getLogger("Minecraft");
    protected static final String LOG_PREFIX = "[DynmapWarps] ";

    public DynmapPlugin dynmap;
    public List<Provider> providers = new ArrayList<Provider>();
    public Configuration configuration;
    public static File configurationFile;

    public boolean disabledHomes = false;
    public boolean disabledWarps = false;


    private class Listener extends ServerListener {
        private DynmapWarps plugin;

        private Listener(DynmapWarps plugin) {
            this.plugin = plugin;
        }

        @Override
        public void onPluginEnable(PluginEnableEvent event) {
            String pluginName = event.getPlugin().getDescription().getName();
            if (pluginName.equals("dynmap")) {
                log.info(LOG_PREFIX + "dynmap plugin loaded");
                addDynmapHandler();
            } else if (pluginName.equals("Warpz0r")) {
                log.info(LOG_PREFIX + "Warpz0r plugin loaded");
                Provider warpz0r = new Warpz0rProvider(plugin);
                providers.add(warpz0r);
                warpz0r.onEnable();
            }
        }
    }

    public void onDisable() {
        removeDynmapHandler();
        dynmap = null;
        for (Provider provider: providers) {
            provider.onDisable();
        }
        providers.clear();
    }


    public void saveConfig() {
        if (configuration == null)  return;

        configuration.setProperty("disable-warps", disabledWarps);
        configuration.setProperty("disable-homes", disabledHomes);
        configuration.save();
    }


    public void loadConfig() {
        if (configuration == null)  return;

        if (configurationFile.exists()) {
            configuration.load();
            disabledWarps = configuration.getBoolean("disable-warps", false);
            disabledHomes = configuration.getBoolean("disable-homes", false);
        } else {
            saveConfig();
        }
    }


    public void onEnable() {
        configurationFile = new File(this.getDataFolder(), "config.yml");
        configuration = new Configuration(configurationFile);
        loadConfig();

        dynmap = (DynmapPlugin) getServer().getPluginManager().getPlugin("dynmap");
        if (dynmap == null) {
            log.severe(LOG_PREFIX + "dynmap plugin not loaded, DynmapWarps cannot be enabled");
            return;
        }

        if (dynmap.isEnabled()) {
            log.info(LOG_PREFIX + "dynmap plugin detected");
            addDynmapHandler();
        }

        getServer().getPluginManager().registerEvent(Event.Type.PLUGIN_ENABLE, new Listener(this), Event.Priority.Low, this);
    }


    public List<Mark> getMarks() {
        List<Mark> marks = new ArrayList<Mark>();
        for (Provider provider: providers) {
            if (provider.isEnabled()) {
                provider.setMarks(marks);
            }
        }

        return marks;
    }


    public void addDynmapHandler() {
        dynmap.getWebServer().handlers.put("/marks", new DynmapHandler(this, dynmap));
    }

    public void removeDynmapHandler() {
        if (dynmap.getWebServer() != null) {
            dynmap.getWebServer().handlers.remove("/marks");
        }
    }
}
