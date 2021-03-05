package si.f5.hatosaba.autobroadcast.spigot.util;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import si.f5.hatosaba.autobroadcast.spigot.AutoBroadcast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class Yaml {

    private final JavaPlugin plugin = AutoBroadcast.instance();

    private final String fileName;
    private final File file;
    private FileConfiguration config = null;

    public Yaml(String fileName) {
        this.fileName = fileName;
        file = new File(plugin.getDataFolder(), fileName);
    }

    public void saveDefault() {
        if (!file.exists()) plugin.saveResource(fileName, false);
    }

    public FileConfiguration config() {
        if (config == null) reload();
        return config;
    }

    public void save() {
        if (config == null) return;

        try {
            config().save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reload() {
        config = YamlConfiguration.loadConfiguration(file);

        InputStream in = plugin.getResource(fileName);
        if (in == null) return;

        config.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(in, StandardCharsets.UTF_8)));
    }

}