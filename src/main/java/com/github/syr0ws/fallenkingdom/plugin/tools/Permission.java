package com.github.syr0ws.fallenkingdom.plugin.tools;

public enum Permission {

    COMMAND_FK("command.fk"),
    COMMAND_FK_START("command.fk.start"),
    COMMAND_FK_STOP("command.fk.stop"),
    COMMAND_FK_PVP("command.fk.pvp"),
    COMMAND_FK_ASSAULTS("command.fk.assaults"),
    COMMAND_FK_TEAM("command.fk.team");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String get() {
        return "fk." + this.permission;
    }
}
