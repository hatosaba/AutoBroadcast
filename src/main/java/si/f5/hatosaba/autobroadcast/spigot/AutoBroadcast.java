package si.f5.hatosaba.autobroadcast.spigot;

import org.bukkit.plugin.java.JavaPlugin;
import si.f5.hatosaba.autobroadcast.Channels;
import si.f5.hatosaba.autobroadcast.spigot.command.ConfigurationReloadCommand;
import si.f5.hatosaba.autobroadcast.spigot.config.MessageConfiguration;
import si.f5.hatosaba.autobroadcast.spigot.util.Yaml;

public final class AutoBroadcast extends JavaPlugin {

    private static AutoBroadcast instance;
    private Yaml configuration;
    private MessageConfiguration messageConfiguration;

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.instance = this;
        this.configuration = new Yaml("config.yml");
        this.configuration.saveDefault();
        this.messageConfiguration = new MessageConfiguration(configuration);
        this.messageConfiguration.load();

        getServer().getMessenger().registerOutgoingPluginChannel(this, Channels.BUNGEE_CORD);
        getCommand("autobroadcast").setExecutor(new ConfigurationReloadCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        this.configuration.save();
        this.messageConfiguration.messageSends.forEach(send -> send.stop());
    }

    public static AutoBroadcast instance() {
        return instance;
    }

    public Yaml config() {
        return this.configuration;
    }

    public MessageConfiguration getMessageConfiguration() {
        return messageConfiguration;
    }
}
