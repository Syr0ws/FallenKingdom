package com.github.syr0ws.fallenkingdom.settings.dao;

import com.github.syr0ws.fallenkingdom.settings.Setting;

import java.util.Collection;

public interface SettingLoader {

    void load(Collection<Setting<?>> settings);
}
