package pl.m4code.utils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class TimeOutUtil {
    private static Map<UUID, Long> timedOut = new LinkedHashMap<UUID, Long>();

    public static void addTimedOut(UUID uuid) {
        TimeOutUtil.getTimedOut().put(uuid, System.currentTimeMillis() + 60000L);
    }

    public static void removeFromTimedOut(UUID uuid) {
        TimeOutUtil.getTimedOut().remove(uuid);
    }

    public static Map<UUID, Long> getTimedOut() {
        return timedOut;
    }
}
