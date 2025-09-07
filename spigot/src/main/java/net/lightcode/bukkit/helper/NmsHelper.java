package net.lightcode.bukkit.helper;

import net.lightcode.NmsService;
import org.bukkit.plugin.Plugin;

public class NmsHelper {

    public static NmsService findNmsService(Plugin plugin) {
        final String nmsVersion = findBukkitVersion(plugin);
        final String className = String.format("net.lightcode.%s.%sNmsService", nmsVersion, nmsVersion);

        try {
            return (NmsService) Class.forName(className).newInstance();
        } catch (Exception exception) {
            throw new RuntimeException("Not found nms for version " + nmsVersion);
        }
    }

    protected static String findBukkitVersion(Plugin plugin) {
        StringBuilder nmsVersion = new StringBuilder(plugin.getServer().getClass().getPackage().getName().split("\\.")[3]);

        int revPosition = nmsVersion.lastIndexOf("_");
        nmsVersion.deleteCharAt(revPosition);

        return nmsVersion.toString();
    }

}
