package com.github.syr0ws.fallenkingdom.game.notifiers;

import com.github.syr0ws.fallenkingdom.attributes.Attribute;
import com.github.syr0ws.fallenkingdom.attributes.AttributeObserver;
import com.github.syr0ws.fallenkingdom.game.GameAttribute;
import com.github.syr0ws.fallenkingdom.game.GameModel;
import com.github.syr0ws.fallenkingdom.messages.types.SimpleMessage;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.util.Collection;
import java.util.Collections;

public class PvPNotifier implements AttributeObserver {

    private final GameModel model;
    private final Plugin plugin;

    public PvPNotifier(GameModel model, Plugin plugin) {
        this.model = model;
        this.plugin = plugin;
    }

    @Override
    public void onUpdate(Attribute attribute) {

        FileConfiguration config = this.plugin.getConfig();
        ConfigurationSection section = config.getConfigurationSection("pvp");

        String path = this.model.isPvPEnabled() ? "enabled" : "disabled";
        String message = section.getString(path);

        new SimpleMessage(message).broadcast();
    }

    @Override
    public Collection<Attribute> observed() {
        return Collections.singleton(GameAttribute.PVP_STATE);
    }
}
