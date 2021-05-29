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
        this.send(player);
    }

    @Override
    public void displayAll() {
        Bukkit.getOnlinePlayers().forEach(this::send);
    }

    private void send(Player player) {

        String text = super.getText();

        try {

            Class<?> chatBaseComponentClass = Reflection.getNMSClass("IChatBaseComponent");
            Class<?> chatBaseComponentSerializer = chatBaseComponentClass.getDeclaredClasses()[0];

            Method method = chatBaseComponentSerializer.getMethod("a", String.class);

            Object formattedText = method.invoke(null, "{\"text\": \"" + text + "\"}");

            Class<?> packetClass = Reflection.getNMSClass("PacketPlayOutChat");

            // Constructor : PacketPlayOutChat(IChatBaseComponent component, byte value)
            Constructor<?> constructor = packetClass.getConstructor(chatBaseComponentClass, byte.class);

            Object packet = constructor.newInstance(formattedText, (byte) 2);

            Reflection.sendPacket(player, packet);

        } catch (Exception e) { e.printStackTrace(); }
    }
}
