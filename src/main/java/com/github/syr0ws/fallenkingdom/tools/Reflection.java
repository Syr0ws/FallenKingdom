package com.github.syr0ws.fallenkingdom.tools;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Reflection {

    public static void sendPacket(Player player, Object packet) {

        try {

            Object handle = player.getClass().getMethod("getHandle").invoke(player);
            Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
            playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(playerConnection, packet);

        } catch (Exception e) { e.printStackTrace(); }
    }

    public static Class<?> getNMSClass(String name) {

        Class<?> clazz = null;

        try {

            String packageName = Bukkit.getServer().getClass().getPackage().getName();
            String nmsVersion = packageName.split("\\.")[3];

            clazz = Class.forName(String.format("net.minecraft.server.%s.%s", nmsVersion, name));

        } catch (ClassNotFoundException e) { e.printStackTrace(); }

        return clazz;
    }
}
