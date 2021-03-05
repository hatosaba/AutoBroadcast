package si.f5.hatosaba.autobroadcast;

public class Channels {

    public static final String BUNGEE_CORD = "BungeeCord";
    public static final String REQUEST;
    public static final String RESPONSE;

    static {
        String name = "AutoBroadcast";
        String separator = ":";
        String prefix = name + separator;

        REQUEST = prefix + "request";
        RESPONSE = prefix + "response";
    }

}