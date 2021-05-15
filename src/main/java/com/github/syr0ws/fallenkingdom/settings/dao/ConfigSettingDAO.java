package com.github.syr0ws.fallenkingdom.settings.dao;

import com.github.syr0ws.fallenkingdom.settings.Readable;
import com.github.syr0ws.fallenkingdom.settings.Setting;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.Collection;

public abstract class ConfigSettingDAO implements SettingDAO {

    @Override
    public void readSetting(String sectionName, Setting<?> setting) {

        YamlConfiguration config = this.getConfiguration();

        if(!config.isSet(sectionName))
            throw new IllegalArgumentException(String.format("Section '%s' not found.", sectionName));

        ConfigurationSection section = config.getConfigurationSection(sectionName);

        if(!(setting instanceof Readable))
            throw new IllegalArgumentException("Setting not readable.");

        if(!section.isSet(setting.getName()))
            return;

        Readable readable = (Readable) setting;
        readable.read(section, setting.getName());
    }

    @Override
    public void readSettings(String sectionName, Collection<Setting<?>> settings) {

        YamlConfiguration config = this.getConfiguration();

        if(!config.isSet(sectionName))
            throw new IllegalArgumentException(String.format("Section '%s' not found.", sectionName));

        ConfigurationSection section = config.getConfigurationSection(sectionName);

        settings.stream()
                .filter(setting -> setting instanceof Readable)
                .filter(setting -> section.isSet(setting.getName()))
                .forEach(setting -> ((Readable) setting).read(section, setting.getName()));
    }
}
