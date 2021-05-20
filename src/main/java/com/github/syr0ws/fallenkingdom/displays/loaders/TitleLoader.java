package com.github.syr0ws.fallenkingdom.displays.loaders;

import com.github.syr0ws.fallenkingdom.displays.Display;
import com.github.syr0ws.fallenkingdom.displays.DisplayLoader;
import com.github.syr0ws.fallenkingdom.displays.impl.LegacyTitle;
import com.github.syr0ws.fallenkingdom.displays.impl.NewTitle;
import com.github.syr0ws.fallenkingdom.displays.impl.Title;
import com.github.syr0ws.fallenkingdom.tools.Version;
import org.bukkit.configuration.ConfigurationSection;

public class TitleLoader implements DisplayLoader {

    private static final Version VERSION = new Version();

    @Override
    public Display load(ConfigurationSection section) {

        String title = section.getString("title", "");
        String subtitle = section.getString("subtitle", "");

        int fadeIn = section.getInt("fade-in", Title.DEFAULT_FADE_IN);
        int stay = section.getInt("stay", Title.DEFAULT_STAY);
        int fadeOut = section.getInt("fade-out", Title.DEFAULT_FADE_OUT);

        return VERSION.isLegacy() ? new LegacyTitle(title, subtitle, fadeIn, stay, fadeOut) : new NewTitle(title, subtitle, fadeIn, stay, fadeOut);
    }
}
