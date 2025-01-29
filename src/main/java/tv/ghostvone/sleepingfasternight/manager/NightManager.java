package tv.ghostvone.sleepingfasternight.manager;

import org.bukkit.GameMode;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.entity.Player;
import tv.ghostvone.sleepingfasternight.GFasterNight;
import tv.ghostvone.sleepingfasternight.runnable.TimerTask;

import java.util.*;
import java.util.stream.Collectors;

public class NightManager {

    public static void setJavaPlugin(GFasterNight gFasterNight) {
        NightManager.gFasterNight = gFasterNight;
    }

    private static GFasterNight gFasterNight;
    private static HashMap<String, TimerTask> worldTimerTasks = new HashMap<>();
    private static HashMap<String, List<UUID>> worldSleepingPlayers = new HashMap<>();

    private static Integer startNightTicks = 12542;
    private static Integer EndNightTicks = 23460;

    public static void refreshNightSpeed(World world) {
        // World data
        Long currentWorldTime = world.getTime();
        String playerWorldName = world.getName();

        // World players
        Collection<Player> worldPlayers = world.getPlayers();

        int frequency = 1;
        float percentOfSleepingPlayers = (float) worldSleepingPlayers.get(playerWorldName).size() / (float) worldPlayers.size();

        int ticksToAdd;
        if (percentOfSleepingPlayers != 1)
            ticksToAdd = (int) (percentOfSleepingPlayers * 10);
        else // Si tous les jouers veulent faire passer la nuit, on fait une belle transition
            ticksToAdd =  ConfigManager.getInt("skipping-ticks-speed");

        // Si une task est déjà active
        if (worldTimerTasks.containsKey(world.getName())) {
            // Si ce n'est plus la nuit
            if (!(isNight(currentWorldTime)))
                endNight(world);

            stopTask(world);
        }

        worldTimerTasks.put(world.getName(), new TimerTask(gFasterNight, world, ticksToAdd, frequency));
    }

    public static boolean isNight(Long currentTime) {
        if (currentTime >= startNightTicks && currentTime <= EndNightTicks)
            return true;

        return false;
    }

    public static void endNight(World world) {
        stopTask(world);

        // On reset le nombre de joueur qui dort
        worldSleepingPlayers.remove(world.getName());
        // On remet la gestion du cycle du temps a la normale
        world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, true);
        return;
    }

    public static void stopTask(World world) {
        // Arrêt de la task
        worldTimerTasks.get(world.getName()).stop();
        // Suppression de la task
        worldTimerTasks.remove(world.getName());
    }

    public static void enableTime(World world) {
        world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, true);
    }

    public static void disableTime(World world) {
        world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
    }

    public static void newPlayerSleeping(Player player) {
        // Init / First call
        if (!worldSleepingPlayers.containsKey(player.getWorld().getName())) {
            NightManager.disableTime(player.getWorld());
            worldSleepingPlayers.put(player.getWorld().getName(), new ArrayList<>());
        }

        // Si le joueur n'est pas dans la liste des joueurs au lit, on l'ajoute
        if (!worldSleepingPlayers.get(player.getWorld().getName()).contains(player.getUniqueId()))
            worldSleepingPlayers.get(player.getWorld().getName()).add(player.getUniqueId());

        // Envoi d'un message à tous les joueurs pour afficher le nombre de joueur qui souhaitent passer la nuit
        List<Player> worldPlayers = player.getWorld().getPlayers()
                .stream()
                .filter(player2 -> player2.getGameMode() == GameMode.SPECTATOR ? false : true)
                .collect(Collectors.toList());
        Integer sleepingPlayersAmount = worldSleepingPlayers.get(player.getWorld().getName()).size();

        HashMap<String, String> replacements = new HashMap<>();
        replacements.put("{sleeping_players}", sleepingPlayersAmount.toString());
        replacements.put("{world_players}", String.valueOf(worldPlayers.size()));
        replacements.put("{speed_percent}", String.valueOf((double) Math.round(((float) sleepingPlayersAmount / (float) worldPlayers.size() * 100) * 100) / 100));
        String message = ConfigManager.replaceWords(ConfigManager.getString("messages.sleeping-players"), replacements);

        for (Player player1 : worldPlayers) {
            player1.sendMessage(message);
        }

        player.sendMessage(ConfigManager.getString("messages.sleeping-info"));

        refreshNightSpeed(player.getWorld());
    }

    public static void playerLeavingBed(Player player) {
//        if (!sleepingPlayers.containsKey(player.getWorld().getName())) {
//            sleepingPlayers.put(player.getWorld().getName(), 0);
//
//            refreshSpeedTime(player);
//            return;
//        }
//
//        sleepingPlayers.put(player.getWorld().getName(), sleepingPlayers.get(player.getWorld().getName()) - 1);
//        refreshSpeedTime(player);
    }
}
