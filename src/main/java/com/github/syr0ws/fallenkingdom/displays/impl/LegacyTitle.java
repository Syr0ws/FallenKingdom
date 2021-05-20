package com.github.syr0ws.fallenkingdom.displays.impl;

import com.github.syr0ws.fallenkingdom.tools.Reflection;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class LegacyTitle extends Title {

    public LegacyTitle(String title, String subtitle) {
        super(title, subtitle);
    }

    public LegacyTitle(String title, String subtitle, int fadeIn, int fadeOut, int stay) {
        super(title, subtitle, fadeIn, fadeOut, stay);
    }

    @Override
    public void displayTo(Player player) {
        this.send(player);
    }

    @Override
    public void displayAll() {
        Bukkit.getOnlinePlayers().forEach(this::displayTo);
    }

    private void send(Player player) {

        String title = super.getTitle();
        String subtitle = super.getSubtitle();

        int fadeIn = super.getFadeIn();
        int fadeOut = super.getFadeOut();
        int stay = super.getStay();

        try {

            Class<?> chatBaseComponentClass = Reflection.getNMSClass("IChatBaseComponent");
            Class<?> chatBaseComponentSerializer = chatBaseComponentClass.getDeclaredClasses()[0];

            Method method = chatBaseComponentSerializer.getMethod("a", String.class);

            Object formattedTitle = method.invoke(null, "{\"text\": \"" + title + "\"}");
            Object formattedSubtitle = method.invoke(null, "{\"text\": \"" + subtitle + "\"}");

            Class<?> packetTitleClass = Reflection.getNMSClass("PacketPlayOutTitle");
            Class<?> declaredTitleClass = packetTitleClass.getDeclaredClasses()[0];

            // PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction action, IChatBaseComponent component, int fadeIn, int stay, int fadeOut)
            Constructor<?> constructor = packetTitleClass.getConstructor(declaredTitleClass, chatBaseComponentClass, int.class, int.class, int.class);

            Object packetTitle = constructor.newInstance(declaredTitleClass.getField("TITLE").get(null), formattedTitle, fadeIn, stay, fadeOut);
            Object packetSubtitle = constructor.newInstance(declaredTitleClass.getField("SUBTITLE").get(null), formattedSubtitle, fadeIn, stay, fadeOut);

            Reflection.sendPacket(player, packetTitle);
            Reflection.sendPacket(player, packetSubtitle);

        } catch (Exception e) { e.printStackTrace(); }
    }
}
