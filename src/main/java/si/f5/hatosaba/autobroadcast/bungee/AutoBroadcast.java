package si.f5.hatosaba.autobroadcast.bungee;

import lombok.NonNull;
import net.kyori.adventure.platform.bungeecord.BungeeAudiences;
import net.md_5.bungee.api.plugin.Plugin;
import si.f5.hatosaba.autobroadcast.Channels;
import si.f5.hatosaba.autobroadcast.bungee.listener.RequestReceiveListener;

public class AutoBroadcast extends Plugin {

    private static AutoBroadcast instance;
    private BungeeAudiences adventure;

    public @NonNull BungeeAudiences adventure() {
        if(this.adventure == null) {
            throw new IllegalStateException("Cannot retrieve audience provider while plugin is not enabled");
        }
        return this.adventure;
    }

    @Override
    public void onEnable() {
        instance = this;
        this.adventure = BungeeAudiences.create(this);
        getProxy().registerChannel(Channels.BUNGEE_CORD);
        getProxy().getPluginManager().registerListener(this, new RequestReceiveListener());
    }

    @Override
    public void onDisable() {
        if(this.adventure != null) {
            this.adventure.close();
            this.adventure = null;
        }
        getProxy().unregisterChannel(Channels.BUNGEE_CORD);
    }

    public static AutoBroadcast instance() {
        return instance;
    }

}