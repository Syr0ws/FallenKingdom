package com.github.syr0ws.fallenkingdom.settings;

public class SettingException extends RuntimeException {

    public SettingException(String message) {
        super(message);
    }

    public SettingException(String message, Throwable cause) {
        super(message, cause);
    }
}
