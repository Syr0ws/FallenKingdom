package com.github.syr0ws.fallenkingdom.settings.types;

import com.github.syr0ws.fallenkingdom.settings.SettingFilter;
import com.github.syr0ws.fallenkingdom.settings.SettingValidationException;
import org.bukkit.configuration.ConfigurationSection;

public class CharacterSetting extends ConfigSetting<Character> {

    protected CharacterSetting(String name, Character defaultValue, Character value, SettingFilter<Character> filter, String path) {
        super(name, defaultValue, value, filter, path);
    }

    @Override
    public void read(ConfigurationSection section) {

        String string = section.getString(super.getPath(), "");

        if(string.length() != 1)
            throw new SettingValidationException(String.format("No character found at '%s.%s'.", section.getName(), super.getPath()));

        super.setValue(string.charAt(0));
    }

    public static class Builder extends ConfigSetting.Builder<Character> {

        public Builder(String name, Character defaultValue, String path) {
            super(name, defaultValue, path);
        }

        @Override
        public CharacterSetting build() {
            return new CharacterSetting(
                    super.getName(),
                    super.getDefaultValue(),
                    super.getValue(),
                    super.getFilter(),
                    super.getPath()
            );
        }
    }
}
