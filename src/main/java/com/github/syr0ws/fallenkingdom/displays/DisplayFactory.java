package com.github.syr0ws.fallenkingdom.displays;

import com.github.syr0ws.fallenkingdom.displays.loaders.ActionBarLoader;
import com.github.syr0ws.fallenkingdom.displays.loaders.MessageLoader;
import com.github.syr0ws.fallenkingdom.displays.loaders.SoundLoader;
import com.github.syr0ws.fallenkingdom.displays.loaders.TitleLoader;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;

public class DisplayFactory {

    private static final Map<String, DisplayLoader> DISPLAY_LOADERS = new HashMap<>();

    static {
        DISPLAY_LOADERS.put("MESSAGE", new MessageLoader());
        DISPLAY_LOADERS.put("SOUND", new SoundLoader());
        DISPLAY_LOADERS.put("TITLE", new TitleLoader());
        DISPLAY_LOADERS.put("ACTION_BAR", new ActionBarLoader());
    }

    public static Display getDisplay(ConfigurationSection section) {

        String type = section.getString("type").toUpperCase();

        if(!DISPLAY_LOADERS.containsKey(type))
            throw new IllegalArgumentException(String.format("No loader found for type '%s'.", type));

        DisplayLoader loader = DISPLAY_LOADERS.get(type);

        return loader.load(section);
    }

    public static void registerLoader(String type, DisplayLoader loader) {
        DISPLAY_LOADERS.put(type.toUpperCase(), loader);
    }
}
