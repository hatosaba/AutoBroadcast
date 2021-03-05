package si.f5.hatosaba.autobroadcast.bungee.listener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.bukkit.ChatColor;
import org.json.JSONObject;
import si.f5.hatosaba.autobroadcast.Channels;
import si.f5.hatosaba.autobroadcast.bungee.AutoBroadcast;


public class RequestReceiveListener implements Listener {

    private final AutoBroadcast plugin = AutoBroadcast.instance();

    @EventHandler
    public void on(PluginMessageEvent event) {
        if (!event.getTag().equals(Channels.BUNGEE_CORD)) return;

        ByteArrayDataInput in = ByteStreams.newDataInput(event.getData());
        if (!in.readUTF().equals(Channels.REQUEST)) return;

        String message = in.readUTF();

        Component component;

        if(isJsonMessage(message)) {
            component = GsonComponentSerializer.gson().deserialize(message);
        }else {
            component = LegacyComponentSerializer.legacy('&').deserialize(message);
        }

        plugin.getProxy().getPlayers().forEach((player) -> plugin.adventure().sender(player).sendMessage(component));
    }

    //これをなんとかする
    public boolean isJsonMessage(String msg) {
        try {
            new JSONObject(msg);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}