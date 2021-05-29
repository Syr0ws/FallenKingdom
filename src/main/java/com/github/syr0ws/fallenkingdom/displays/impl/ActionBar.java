package com.github.syr0ws.fallenkingdom.displays.impl;

import com.github.syr0ws.fallenkingdom.displays.TextDisplay;

public abstract class ActionBar extends TextDisplay {

    private final String text;

    public ActionBar(String text) {

        if(text == null)
            throw new IllegalArgumentException("Text cannot be null.");

        this.text = text;
    }

    public String getText() {
        return super.format(this.text);
    }
}
