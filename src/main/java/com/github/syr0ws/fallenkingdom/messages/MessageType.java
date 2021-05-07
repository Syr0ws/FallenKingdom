package com.github.syr0ws.fallenkingdom.messages;

import java.util.Arrays;
import java.util.Optional;

public enum MessageType {

    TEXT;

    public static Optional<MessageType> getByName(String name) {
        return Arrays.stream(values())
                .filter(type -> type.name().equals(name))
                .findFirst();
    }
}
