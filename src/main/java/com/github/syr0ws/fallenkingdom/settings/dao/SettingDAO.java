package com.github.syr0ws.fallenkingdom.settings.dao;

import com.github.syr0ws.fallenkingdom.settings.Setting;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.Collection;

public interface SettingDAO {

    void readSetting(String section, Setting<?> setting);

    void readSettings(String section, Collection<Setting<?>> settings);

    YamlConfiguration getConfiguration();
}
