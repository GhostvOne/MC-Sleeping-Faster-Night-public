package tv.ghostvone.sleepingfasternight.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.world.TimeSkipEvent;
import tv.ghostvone.sleepingfasternight.GFasterNight;
import tv.ghostvone.sleepingfasternight.manager.NightManager;

public class SleepListener implements Listener {
    private final GFasterNight gFasterNight;

    public SleepListener(GFasterNight gFasterNight) {
        this.gFasterNight = gFasterNight;
    }

    @EventHandler
    public void onPlayerBedEnter(PlayerBedEnterEvent event) {
        Player player = event.getPlayer();

        if (event.getBedEnterResult() != PlayerBedEnterEvent.BedEnterResult.OK)
            return;

        NightManager.newPlayerSleeping(player);
    }

//    @EventHandler
//    public void onPlayerBedLeave(PlayerBedLeaveEvent event) {
//        Player player = event.getPlayer();
//
//        SleepingManager.playerLeavingBed(player);
//    }

//    @EventHandler
//    public void onPlayerQuit(PlayerQuitEvent event) {
//        Player player = event.getPlayer();
//
//        SleepingManager.playerLeavingBed(player);
//    }

    @EventHandler
    // On bloque la gestion auto du skip night
    public void onTimeSkip(TimeSkipEvent event) {
        if (event.getSkipReason() == TimeSkipEvent.SkipReason.NIGHT_SKIP)
            event.setCancelled(true);
    }
}
