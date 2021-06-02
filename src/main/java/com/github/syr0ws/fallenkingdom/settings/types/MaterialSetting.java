package com.github.syr0ws.fallenkingdom.settings.types;

import com.github.syr0ws.fallenkingdom.settings.SettingFilter;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MaterialSetting extends ConfigSetting<List<Material>>  {

    protected MaterialSetting(String name, List<Material> defaultValue, List<Material> value, SettingFilter<List<Material>> filter, String path) {
        super(name, defaultValue, value, filter, path);
    }

    @Override
    public void read(ConfigurationSection section) {

        List<String> list = section.getStringList(super.getPath());

        List<Material> materials = list.stream()
                .map(Material::matchMaterial)
                .filter(Objects::nonNull)
                .filter(Material::isBlock)
                .collect(Collectors.toList());

        super.setValue(materials);
    }

    public static class Builder extends ConfigSetting.Builder<List<Material>> {

        public Builder(String name, List<Material> defaultValue, String path) {
            super(name, defaultValue, path);
        }

        @Override
        public MaterialSetting build() {
            return new MaterialSetting(
                    super.getName(),
                    super.getDefaultValue(),
                    super.getValue(),
                    super.getFilter(),
                    super.getPath()
            );
        }
    }
}
