package tv.ghostvone.sleepingfasternight.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class Ghostvone {

    public static void displayCredit(JavaPlugin javaPlugin, Boolean disabledEvent) {
        ConsoleCommandSender console = Bukkit.getConsoleSender();
        String color = disabledEvent ? ChatColor.RED.toString() : ChatColor.GREEN.toString();
        console.sendMessage(color + "[" + javaPlugin.getDescription().getName() + "] ====================================================");
        console.sendMessage(color + "[" + javaPlugin.getDescription().getName() + "] Thanks for using my plugin");
        console.sendMessage(color + "[" + javaPlugin.getDescription().getName() + "] If you have any issue or suggestions use my Discord : discord.gg/KMB5CXyf7b");
        console.sendMessage(color + "[" + javaPlugin.getDescription().getName() + "] Don't forget to rate my plugin on Spigot website. It's very appreciated :)");
        console.sendMessage(color + "[" + javaPlugin.getDescription().getName() + "] ====================================================");
    }
}
