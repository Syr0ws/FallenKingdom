package com.github.syr0ws.fallenkingdom.settings.impl;

import com.github.syr0ws.fallenkingdom.settings.Readable;
import com.github.syr0ws.fallenkingdom.settings.SettingFilter;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MaterialSetting extends MutableSetting<List<Material>> implements Readable {

    private static final SettingFilter<List<Material>> FILTER = list -> list != null && list.stream().allMatch(Material::isBlock);

    public MaterialSetting(String name, List<Material> defaultValue) {
        super(name, defaultValue, FILTER);
    }

    public MaterialSetting(String name, List<Material> value, List<Material> defaultValue) {
        super(name, value, defaultValue, FILTER);
    }

    @Override
    public void read(ConfigurationSection section, String key) {

        List<String> list = section.getStringList(key);

        List<Material> materials = list.stream()
                .map(Material::matchMaterial)
                .filter(Objects::nonNull)
                .filter(Material::isBlock)
                .collect(Collectors.toList());

        super.setValue(materials);
    }
}
