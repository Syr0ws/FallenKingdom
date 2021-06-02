package com.github.syr0ws.fallenkingdom.settings.types;

import com.github.syr0ws.fallenkingdom.settings.SettingFilter;
import com.github.syr0ws.fallenkingdom.settings.SettingValidationException;
import com.github.syr0ws.fallenkingdom.utils.LocationUtils;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

public class LocationSetting extends ConfigSetting<Location> {
    protected LocationSetting(String name, Location defaultValue, Location value, SettingFilter<Location> filter, String path) {
        super(name, defaultValue, value, filter, path);
    }

    @Override
    public void read(ConfigurationSection section) {

        ConfigurationSection locSection = section.getConfigurationSection(super.getPath());

        if(locSection == null)
            throw new SettingValidationException(String.format("Location not found at %s.%s", section.getName(), super.getPath()));

        Location location = LocationUtils.getLocation(locSection);

        super.setValue(location);
    }

    public static class Builder extends ConfigSetting.Builder<Location> {

        public Builder(String name, Location defaultValue, String path) {
            super(name, defaultValue, path);
        }

        @Override
        public LocationSetting build() {
            return new LocationSetting(
                    super.getName(),
                    super.getDefaultValue(),
                    super.getValue(),
                    super.getFilter(),
                    super.getPath()
            );
        }
    }
}
