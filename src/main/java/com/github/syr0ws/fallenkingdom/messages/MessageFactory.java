package com.github.syr0ws.fallenkingdom.messages;

import com.github.syr0ws.fallenkingdom.messages.types.SimpleMessage;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Optional;

public class MessageFactory {

    public static Message getMessage(ConfigurationSection section) {

        String type = section.getString("type", "unspecified");

        Optional<MessageType> optional = MessageType.getByName(type);

        if(!optional.isPresent())
            throw new NullPointerException(String.format("Message type invalid or not found in '%s'.", section.getName()));

        switch (optional.get()) {
            case TEXT:
                return new SimpleMessage(section);
            default:
                return null; // Only if the message type is not handled. May not happen.
        }
    }
}
