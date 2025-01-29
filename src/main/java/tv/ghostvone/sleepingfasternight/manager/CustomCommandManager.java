package tv.ghostvone.sleepingfasternight.manager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.SimplePluginManager;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public abstract class CustomCommandManager extends BukkitCommand {

    CommandMap map;

    // Constructeur de nos commandes
    public CustomCommandManager(String command, String[] aliases, String description, String permission) {
        // On dit a bukkit quelle commande est exécuté
        super(command);

        // On paramètre la commande dans bukkit (normalement ses infos sont dans plugin.yml)
        if (aliases != null)
            this.setAliases(Arrays.asList(aliases));

        this.setDescription(description);
        this.setPermission(permission);
        this.setPermissionMessage(ChatColor.RED + "You must have " + permission + " to use this command");

        try {
            // Permet d'enregistrer a Bukkit la commande qui est tappé
            Field field = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            field.setAccessible(true);
            map = (CommandMap) field.get(Bukkit.getServer());
            map.register(command, this);
        } catch (NoSuchFieldException | IllegalAccessException exception) {
            exception.printStackTrace();
        }
    }

    public void unregisterCommand(String... commands) {
        try {
            // On récupère toutes les commandes déclarés
            Field knownCommandsField = SimpleCommandMap.class.getDeclaredField("knownCommands");
            knownCommandsField.setAccessible(true);
            CommandMap commandMap = getCommandMap();
            // On récupère celle qui nous intéresse
            Map<String, Command> knownCommands = (Map<String, Command>) knownCommandsField.get(commandMap);

            for (String commandName : commands) {
                Command command = commandMap.getCommand(commandName);
                // Si la commande fait bien partie du plugin alors on remove
                if (command.getClass() == this.getClass()) {
                    for (String alias : command.getAliases())
                        knownCommands.remove(alias);

                    command.unregister(commandMap);
                    knownCommands.remove(command.getName());
                }
            }
            knownCommandsField.setAccessible(false);
        } catch (Exception exception) {}
    }

    public void invokeSyncCommands() {
        try {
            Method syncCommandsMethod = Bukkit.getServer().getClass().getDeclaredMethod("syncCommands");
            syncCommandsMethod.setAccessible(true);
            syncCommandsMethod.invoke(Bukkit.getServer());
            syncCommandsMethod.setAccessible(false);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private static CommandMap getCommandMap() {
        try {
            PluginManager pluginManager = Bukkit.getPluginManager();
            if (!(pluginManager instanceof SimplePluginManager)) {
                return null;
            }
            Field field = SimplePluginManager.class.getDeclaredField("commandMap");
            field.setAccessible(true);
            return (CommandMap) field.get(Bukkit.getPluginManager());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Parce qu'on extends bukkit commande il faut overide cette méthode
    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        return execute(commandSender, strings);
    }

    public abstract boolean execute(CommandSender sender, String[] args);

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        return onTabComplete(sender, args);
    }

    public abstract List<String> onTabComplete(CommandSender sender, String[] args);
}
