package com.github.syr0ws.fallenkingdom.plugin.game.cycles.displays;

public enum GameRunningDisplayEnum {

    PVP_ENABLED("pvp.enabled"),
    PVP_DISABLED("pvp.disabled"),
    ASSAULTS_ENABLED("assaults.enabled"),
    ASSAULTS_DISABLED("assaults.disabled"),
    NETHER_ENABLED("nether.enabled"),
    NETHER_DISABLED("nether.disabled"),
    END_ENABLED("end.enabled"),
    END_DISABLED("end.disabled"),
    WIN_ALL("win.all"),
    WIN_SELF("win.self"),
    WIN_OTHER("win.other");

    private final String path;

    GameRunningDisplayEnum(String path) {
        this.path = path;
    }

    public String getPath() {
        return this.path;
    }
}
