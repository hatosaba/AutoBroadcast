package si.f5.hatosaba.autobroadcast.spigot.config;

import org.bukkit.configuration.ConfigurationSection;
import si.f5.hatosaba.autobroadcast.spigot.AutoBroadcast;
import si.f5.hatosaba.autobroadcast.spigot.messagedata.Message;
import si.f5.hatosaba.autobroadcast.spigot.models.MessageSend;
import si.f5.hatosaba.autobroadcast.spigot.util.Yaml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessageConfiguration {

    private final Yaml yaml;

    public final HashMap<String, Message> messagesList = new HashMap<>();
    public final HashMap<String, String> childrenToParents = new HashMap<>();
    public List<MessageSend> messageSends = new ArrayList<>();

    public MessageConfiguration(Yaml yaml) {
        this.yaml = yaml;
    }

    public void load() {
        messagesList.clear();
        childrenToParents.clear();
        messageSends.clear();

        ConfigurationSection messagesSection = yaml.config().getConfigurationSection("message-lists");
        for (String messageIdentifier : messagesSection.getKeys(false)) {
            ConfigurationSection section = messagesSection.getConfigurationSection(messageIdentifier);
            Boolean isEnabled = section.getBoolean("enabled");
            Boolean isRandom = section.getBoolean("random");
            String time = section.getString("time");
            int expiry = section.getInt("expiry");
            List<String> messages = section.getStringList("messages");
            Message message = new Message(messageIdentifier, isEnabled, isRandom, time, expiry, messages);
            messagesList.put(messageIdentifier, message);
        }

        this.messagesList.forEach((s, message) -> {
            MessageSend messageSend = new MessageSend(message);
            messageSend.start();
            messageSends.add(messageSend);
        });
    }
}
