package com.github.syr0ws.fallenkingdom.utils;

import org.bukkit.ChatColor;

public class TextUtils {

    private static final char COLOR_CHAR = '&';

    public static String parseColors(String message) {
        return ChatColor.translateAlternateColorCodes(COLOR_CHAR, message);
    }
}
