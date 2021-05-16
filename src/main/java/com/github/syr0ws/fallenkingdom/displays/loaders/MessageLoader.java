package com.github.syr0ws.fallenkingdom.displays.loaders;

import com.github.syr0ws.fallenkingdom.displays.Display;
import com.github.syr0ws.fallenkingdom.displays.DisplayLoader;
import com.github.syr0ws.fallenkingdom.displays.impl.Message;
import org.bukkit.configuration.ConfigurationSection;

public class MessageLoader implements DisplayLoader {

    @Override
    public Display load(ConfigurationSection section) {

        String text = section.getString("text");

        return new Message(text);
    }
}

