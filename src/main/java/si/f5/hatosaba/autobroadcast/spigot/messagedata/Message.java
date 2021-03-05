package si.f5.hatosaba.autobroadcast.spigot.messagedata;

import java.util.List;

public class Message {

    public final String identifier;
    public final boolean isEnabled;
    public final boolean isRandom;
    public final String time;
    public final int expiry;
    public final List<String> messages;
    private transient int currentIndex = 0;

    public Message(
            String identifier,
            Boolean isEnabled,
            Boolean isRandom,
            String time,
            int expiry,
            List<String> messages
    ) {
        this.identifier = identifier;
        this.isEnabled = isEnabled;
        this.isRandom = isRandom;
        this.time = time;
        this.expiry = expiry;
        this.messages = messages;
    }

    public void setCurrentIndex(int index) {
        this.currentIndex = index;

        if (currentIndex >= messages.size() || currentIndex < 0) {
            this.currentIndex = 0;
        }
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

}
