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
        String packageName = plugin.getServer().getClass().getPackage().getName();
        String[] parts = packageName.split("\\.");

        if (parts.length > 3) {
            return parts[3];
        }

        String version = plugin.getServer().getBukkitVersion();
        String clean = version.split("-")[0];
        String[] nums = clean.split("\\."); 

        return "v" + nums[0] + "_" + nums[1] + "R1";
    }

}
