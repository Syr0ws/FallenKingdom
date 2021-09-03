package com.github.syr0ws.fallenkingdom.plugin.tools;

public enum Permission {

    COMMAND_FK("command.fallendkingdom"),
    COMMAND_FK_START("command.fallendkingdom.start"),
    COMMAND_FK_STOP("command.fallendkingdom.stop"),
    COMMAND_FK_PVP("command.fallendkingdom.pvp"),
    COMMAND_FK_ASSAULTS("command.fallendkingdom.assaults"),
    COMMAND_FK_TEAM("command.fallendkingdom.team");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String get() {
        return "fallenkingdom." + this.permission;
    }
}
