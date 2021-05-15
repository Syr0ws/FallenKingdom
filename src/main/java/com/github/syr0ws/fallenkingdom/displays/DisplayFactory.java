package com.github.syr0ws.fallenkingdom.displays;

import com.github.syr0ws.fallenkingdom.displays.impl.Message;
import com.github.syr0ws.fallenkingdom.displays.impl.SoundEffect;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Optional;

public class DisplayFactory {

    public static Display getDisplay(ConfigurationSection section) {

        String type = section.getString("type", "unspecified");

        Optional<DisplayType> optional = DisplayType.getByName(type);

        if(!optional.isPresent())
            throw new NullPointerException(String.format("Message type invalid or not found in '%s'.", section.getName()));

        DisplayType displayType = optional.get();

        switch (displayType) {
            case MESSAGE:
                return new Message(section);
            case SOUND:
                return new SoundEffect(section);
            default:
                throw new NullPointerException(String.format("No display class found for '%s'.", displayType.name())); // TODO Change the type of the exception.
        }
    }
}
