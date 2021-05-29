package com.github.syr0ws.fallenkingdom.displays.impl;

import com.github.syr0ws.fallenkingdom.tools.Reflection;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class LegacyActionBar extends ActionBar {

    public LegacyActionBar(String text) {
        super(text);
    }

    @Override
    public void displayTo(Player player) {
        Object packet = this.getPacket();
        Reflection.sendPacket(player, packet);
    }

    @Override
    public void displayAll() {
        Object packet = this.getPacket();
        Bukkit.getOnlinePlayers().forEach(player -> Reflection.sendPacket(player, packet));
    }

    private Object getPacket() {

        String text = super.getText();

        Object packet = null;

        try {

            Class<?> chatBaseComponentClass = Reflection.getNMSClass("IChatBaseComponent");
            Class<?> chatBaseComponentSerializer = chatBaseComponentClass.getDeclaredClasses()[0];

            Method method = chatBaseComponentSerializer.getMethod("a", String.class);

            Object formattedText = method.invoke(null, "{\"text\": \"" + text + "\"}");

            Class<?> packetClass = Reflection.getNMSClass("PacketPlayOutChat");

            // Constructor : PacketPlayOutChat(IChatBaseComponent component, byte value)
            Constructor<?> constructor = packetClass.getConstructor(chatBaseComponentClass, byte.class);

            packet = constructor.newInstance(formattedText, (byte) 2);

        } catch (Exception e) { e.printStackTrace(); }

        return packet;
    }
}
