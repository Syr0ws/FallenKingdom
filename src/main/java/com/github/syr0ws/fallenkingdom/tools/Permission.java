package com.github.syr0ws.fallenkingdom.tools;

public enum Permission {

    COMMAND_PVP("command.pvp"), COMMAND_ASSAULTS("command.assaults");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String get() {
        return "fk." + this.permission;
    }
}
