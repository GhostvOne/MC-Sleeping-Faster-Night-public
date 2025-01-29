package tv.ghostvone.sleepingfasternight.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tv.ghostvone.sleepingfasternight.GFasterNight;
import tv.ghostvone.sleepingfasternight.Permission;
import tv.ghostvone.sleepingfasternight.manager.ConfigManager;
import tv.ghostvone.sleepingfasternight.manager.CustomCommandManager;

import java.util.ArrayList;
import java.util.List;

public class FasterNightCommand extends CustomCommandManager {
    GFasterNight plugin;
    public FasterNightCommand(GFasterNight plugin) {
        super(ConfigManager.getString("command"),
                null,
                "use reload argument for reload config file",
                Permission.RUN_COMMAND.getName());
        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player))
            return false;

        Player player = (Player) sender;

        if (args.length > 0) {
           switch (args[0]) {
               case "reload":
                   player.sendMessage("Config reloaded");
                   this.unregisterCommand(ConfigManager.config.getString("command"));
                   ConfigManager.reloadConfig(plugin);
                   new FasterNightCommand(plugin);
                   this.invokeSyncCommands();
                   return true;
           }
       }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        if (sender.isOp() && args.length > 0) {
            List<String> helper = new ArrayList<>();
            helper.add("reload");
            return helper;
        }
        return null;
    }
}
