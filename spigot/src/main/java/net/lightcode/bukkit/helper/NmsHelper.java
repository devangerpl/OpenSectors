package net.lightcode.bukkit.helper;

import net.lightcode.NmsService;
import net.lightcode.bukkit.BukkitSectorPlugin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class NmsHelper {

    public static NmsService findNmsService(BukkitSectorPlugin plugin) {
        final String nmsVersion = findBukkitVersion(plugin);
        final String className = String.format("net.lightcode.%s.%sNmsService", nmsVersion, nmsVersion);

        if (isAtLeast117(nmsVersion) && !isPaperLike()) {
            plugin.logger().severe("==================================================");
            plugin.logger().severe(" ");
            plugin.logger().severe("   ERROR: This plugin requires Paper (or a fork) ");
            plugin.logger().severe("   when running Minecraft version 1.17 or higher! ");
            plugin.logger().severe(" ");
            plugin.logger().severe("   Detected server: " + plugin.getServer().getName());
            plugin.logger().severe("   Detected NMS:    " + nmsVersion);
            plugin.logger().severe(" ");
            plugin.logger().severe("   Please switch to Paper, Purpur, or Pufferfish.");
            plugin.logger().severe(" ");
            plugin.logger().severe("==================================================");

            plugin.getServer().shutdown();
            throw new IllegalStateException("Unsupported server engine for " + nmsVersion);
        }


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

    private static boolean isAtLeast117(String nmsVersion) {
        try {
            String[] parts = nmsVersion.substring(1).split("_");
            int major = Integer.parseInt(parts[0]);
            int minor = Integer.parseInt(parts[1]);
            return (major > 1) || (major == 1 && minor >= 17);
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean isPaperLike() {
        String name = Bukkit.getServer().getName();
        if (name.equalsIgnoreCase("Paper") || name.equalsIgnoreCase("Purpur") || name.equalsIgnoreCase("Pufferfish")) {
            return true;
        }

        try {
            Class.forName("com.destroystokyo.paper.PaperConfig");
            return true;
        } catch (ClassNotFoundException ignored) {}

        try {
            Class.forName("io.papermc.paper.configuration.Configuration");
            return true;
        } catch (ClassNotFoundException ignored) {}

        return false;
    }

}
