package com.github.syr0ws.fallenkingdom.tools;

public class Validate {

    public static boolean isInt(String string) {

        try {
            Integer.parseInt(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
