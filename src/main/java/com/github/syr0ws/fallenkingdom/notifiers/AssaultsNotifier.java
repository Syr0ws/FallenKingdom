package com.github.syr0ws.fallenkingdom.notifiers;

import com.github.syr0ws.fallenkingdom.attributes.Attribute;
import com.github.syr0ws.fallenkingdom.attributes.AttributeObserver;
import com.github.syr0ws.fallenkingdom.displays.types.Message;
import com.github.syr0ws.fallenkingdom.game.model.GameModel;
import com.github.syr0ws.fallenkingdom.game.model.attributes.GameAttribute;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.util.Collection;
import java.util.Collections;

public class AssaultsNotifier implements AttributeObserver {

    private final GameModel model;
    private final Plugin plugin;

    public AssaultsNotifier(GameModel model, Plugin plugin) {
        this.model = model;
        this.plugin = plugin;
    }

    @Override
    public void onUpdate(Attribute attribute) {

        FileConfiguration config = this.plugin.getConfig();
        ConfigurationSection section = config.getConfigurationSection("assaults");

        String path = this.model.areAssaultsEnabled() ? "enabled" : "disabled";
        String message = section.getString(path);

        new Message(message).displayAll();
    }

    @Override
    public Collection<Attribute> observed() {
        return Collections.singleton(GameAttribute.ASSAULTS_STATE);
    }
}
