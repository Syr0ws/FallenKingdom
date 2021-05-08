package com.github.syr0ws.fallenkingdom.display;

import com.github.syr0ws.fallenkingdom.display.placeholders.Placeholder;
import com.github.syr0ws.fallenkingdom.utils.TextUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class TextDisplay implements Display {

    private final Map<Placeholder, String> placeholders = new HashMap<>();

    protected String format(String text) {

        for(Map.Entry<Placeholder, String> entry : this.placeholders.entrySet()) {
            text = text.replace(entry.getKey().get(), entry.getValue());
        }
        return TextUtils.parseColors(text);
    }

    public void addPlaceholder(Placeholder placeholder, String value) {
        this.placeholders.put(placeholder, value);
    }

    public void removePlaceholder(Placeholder placeholder) {
        this.placeholders.remove(placeholder);
    }

    public Map<Placeholder, String> getPlaceholders() {
        return Collections.unmodifiableMap(this.placeholders);
    }
}
