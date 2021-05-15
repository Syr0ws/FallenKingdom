package com.github.syr0ws.fallenkingdom.settings.dao;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FKSettingDAO extends ConfigSettingDAO {

    private static final String SETTING_FILE_NAME = "settings.yml";

    private final Plugin plugin;
    private YamlConfiguration config;

    public FKSettingDAO(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public YamlConfiguration getConfiguration() {

        if(this.config == null) this.loadConfiguration();

        return this.config;
    }

    private void loadConfiguration() {

        Path path = this.getSettingFile();

        try {

            this.createFile();
            this.config = YamlConfiguration.loadConfiguration(path.toFile());

        } catch (IOException e) { e.printStackTrace(); }
    }

    private void createFile() throws IOException {

        Path path = this.getSettingFile();

        if(!Files.exists(path)) this.plugin.saveResource(SETTING_FILE_NAME, false);
    }

    private Path getSettingFile() {
        return Paths.get(this.plugin.getDataFolder() + "/" + SETTING_FILE_NAME);
    }
}
