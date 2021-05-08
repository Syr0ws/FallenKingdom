package com.github.syr0ws.fallenkingdom.display;

import java.util.Arrays;
import java.util.Optional;

public enum DisplayType {

    MESSAGE, SOUND;

    public static Optional<DisplayType> getByName(String name) {
        return Arrays.stream(values())
                .filter(type -> type.name().equals(name))
                .findFirst();
    }
}
