package cz.sychrovnet.dynmapwarps;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import cz.sychrovnet.dynmapwarps.providers.Provider;
import cz.sychrovnet.dynmapwarps.providers.Warpz0rProvider;
import org.bukkit.event.Event;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.ServerListener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;
import org.dynmap.DynmapPlugin;

import java.util.ArrayList;
import java.util.List;

/**
 * Main plugin class plugin. It creates connections with dynmap and Warpz0r plugins.
 * Plugin integration with the other plugins needs some improvements.
 *
 * User: stilett
 * Date: 6/10/11
 * Time: 12:58 PM
 */
public class DynmapWarps extends JavaPlugin {
    public static Logger log = Logger.getLogger("Minecraft");
    protected static final String LOG_PREFIX = "[DynmapWarps] ";
    public Map<String, String> providerMap = new HashMap<String, String>();

    public DynmapPlugin dynmap;
    public Map<String, Provider> providers = new HashMap<String, Provider>();
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

            } else {
                for (Map.Entry<String, String> providerEntry : providerMap.entrySet()) {
                    if (pluginName.equals(providerEntry.getKey())) {
                        plugin.loadProvider(providerEntry.getKey(), providerEntry.getValue());
                        break;
                    }
                }
            }
        }
    }

    public void onDisable() {
        removeDynmapHandler();
        dynmap = null;
        for (Map.Entry<String, Provider> provider: providers.entrySet()) {
            provider.getValue().onDisable();
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
        providerMap.clear();
        providerMap.put("Warpz0r", "cz.sychrovnet.dynmapwarps.providers.Warpz0rProvider");
        providers.clear();

        dynmap = (DynmapPlugin) getServer().getPluginManager().getPlugin("dynmap");
        if (dynmap == null) {
            log.severe(LOG_PREFIX + "dynmap plugin not loaded, DynmapWarps cannot be enabled");
            return;
        }

        if (dynmap.isEnabled()) {
            log.info(LOG_PREFIX + "dynmap plugin detected");
            addDynmapHandler();
        }

        for (Map.Entry<String, String> providerEntry : providerMap.entrySet()) {
            Plugin sourcePlugin = getServer().getPluginManager().getPlugin(providerEntry.getKey());

            if (sourcePlugin != null && sourcePlugin.isEnabled()) {
                this.loadProvider(providerEntry.getKey(), providerEntry.getValue());
            }
        }

        getServer().getPluginManager().registerEvent(Event.Type.PLUGIN_ENABLE, new Listener(this), Event.Priority.Low, this);
    }


    public List<Mark> getMarks() {
        List<Mark> marks = new ArrayList<Mark>();
        for (Map.Entry<String, Provider> providerEntry: providers.entrySet()) {
            Provider provider = providerEntry.getValue();
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


    public void loadProvider(String pluginName, String className) {
        try {
            Class[] parameterTemplate = new Class[1];
            parameterTemplate[0] = DynmapWarps.class;

            Class providerClass = Class.forName(className);
            Constructor constructor = providerClass.getConstructor(parameterTemplate);

            Object[] parameters = new Object[1];
            parameters[0] = this;

            Provider provider = (Provider) constructor.newInstance(parameters);
            providers.put(pluginName, provider);
            provider.onEnable();
            log.info(LOG_PREFIX + pluginName + " plugin loaded");

        } catch (ClassNotFoundException e) {
            log.info(LOG_PREFIX + "Provider class " + className + " for " + pluginName + " not found");

        } catch (Exception e) {
            log.info(LOG_PREFIX + "Failed to load provider for " + pluginName);
        }
    }
}
