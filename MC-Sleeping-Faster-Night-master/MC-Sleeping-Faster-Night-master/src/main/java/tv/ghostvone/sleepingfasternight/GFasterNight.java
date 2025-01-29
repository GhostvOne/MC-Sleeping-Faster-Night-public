package tv.ghostvone.sleepingfasternight;

import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import tv.ghostvone.sleepingfasternight.commands.FasterNightCommand;
import tv.ghostvone.sleepingfasternight.listeners.SleepListener;
import tv.ghostvone.sleepingfasternight.manager.ConfigManager;
import tv.ghostvone.sleepingfasternight.manager.NightManager;
import tv.ghostvone.sleepingfasternight.utils.Ghostvone;

public final class GFasterNight extends JavaPlugin {

    private NightManager nightManager;

    @Override
    public void onEnable() {
        // Init Plugin
        Ghostvone.displayCredit(this, false);
        ConfigManager.setupConfig(this);
        NightManager.setJavaPlugin(this);

        // Todo : cancel le time lorsque le timer s'activer

        Bukkit.getPluginManager().registerEvents(new SleepListener(this), this);
        new FasterNightCommand(this);

        if (ConfigManager.getBoolean("metrics")) {
            int pluginId = 17323;
            Metrics metrics = new Metrics(this, pluginId);
        }
    }

    @Override
    public void onDisable() {
        Ghostvone.displayCredit(this, true);
    }

    public NightManager getSleepingManager() {return nightManager;}
}
