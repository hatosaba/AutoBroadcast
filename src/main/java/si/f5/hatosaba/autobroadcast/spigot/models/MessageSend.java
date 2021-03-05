package si.f5.hatosaba.autobroadcast.spigot.models;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import si.f5.hatosaba.autobroadcast.Channels;
import si.f5.hatosaba.autobroadcast.spigot.AutoBroadcast;
import si.f5.hatosaba.autobroadcast.spigot.messagedata.Message;
import si.f5.hatosaba.autobroadcast.spigot.util.service.TimeService;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class MessageSend {

    private final ZoneId timeZone;
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private ZonedDateTime sendTime;
    private Message message;
    private ScheduledFuture<?> resetTask;

    public MessageSend(Message message) {
        this.timeZone = ZoneId.of("Asia/Tokyo");
        this.sendTime = this.parseTime(this.now().withSecond(0), message.time);
        this.message = message;
    }

    public void start() {
        this.resetTask = this.executorService.schedule(() -> {
            this.broadcast();
            this.start();
        }, this.timeToResetSeconds(), TimeUnit.SECONDS);
    }

    public void stop() {
        this.resetTask.cancel(true);
    }

    public void broadcast() {
        if (AutoBroadcast.instance().config().config().getBoolean("settings.enabled")) {
            if (message.isEnabled && message.expiry == -1) {
                List<String> messages = message.messages;
                int index = message.isRandom ? new Random().nextInt(messages.size()) : message.getCurrentIndex();

                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF(Channels.REQUEST);
                out.writeUTF(messages.get(index));

                Bukkit.getServer().sendPluginMessage(AutoBroadcast.instance(), Channels.BUNGEE_CORD, out.toByteArray());
                message.setCurrentIndex(index + 1);
            }
        }
    }

    public String asString() {
        return new TimeService().format(TimeUnit.SECONDS, this.timeToResetSeconds());
    }

    public long timeToResetSeconds() {
        if (this.now().until(this.sendTime, ChronoUnit.SECONDS) <= 0) {
            this.sendTime = this.sendTime.plusDays(1);
        }
        return this.now().until(this.sendTime, ChronoUnit.SECONDS);
    }

    private ZonedDateTime now() {
        return ZonedDateTime.now().withZoneSameInstant(this.timeZone);
    }

    private ZonedDateTime parseTime(ZonedDateTime date, String time) {
        String[] timeSplit = time.split(":");
        return date.withHour(StringUtils.isNumeric(timeSplit[0]) ? Integer.parseInt(timeSplit[0]) : 0).withMinute(timeSplit.length > 1 ? StringUtils.isNumeric(timeSplit[1]) ? Integer.parseInt(timeSplit[1]) : 0 : 0);
    }
}
