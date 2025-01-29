package tv.ghostvone.sleepingfasternight.runnable;

import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import tv.ghostvone.sleepingfasternight.manager.NightManager;

public class TimerTask extends BukkitRunnable {


    private final JavaPlugin plugin;
    private final World world;
    private final Integer ticksToAdd;
    private final Integer frequency;

    public TimerTask(JavaPlugin plugin, World world, Integer ticksToAdd, Integer frequency) {
        this.plugin = plugin;
        this.world = world;
        this.ticksToAdd = ticksToAdd;
        this.frequency = frequency;

        start();
    }

    public void start() {
        runTaskTimer(plugin, 1, frequency);
    }

    public void stop() {
        cancel();
    }

    @Override
    public void run() {
        if (!NightManager.isNight(world.getTime())) {
            NightManager.enableTime(world);
            NightManager.endNight(world);
            return;
        }

        world.setTime(world.getTime() + ticksToAdd);
    }
}
