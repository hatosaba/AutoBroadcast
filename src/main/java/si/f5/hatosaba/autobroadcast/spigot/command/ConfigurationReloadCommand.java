package si.f5.hatosaba.autobroadcast.spigot.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import si.f5.hatosaba.autobroadcast.spigot.AutoBroadcast;

import static org.bukkit.ChatColor.*;

public class ConfigurationReloadCommand implements CommandExecutor {

    private final AutoBroadcast plugin = AutoBroadcast.instance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        AutoBroadcast.instance().getMessageConfiguration().messageSends.forEach(send -> send.stop());
        plugin.config().reload();
        plugin.getMessageConfiguration().load();
        sender.sendMessage(GRAY + "AutoBroadcast :: config.ymlのリロードが完了しました。");
        return true;
    }
}
