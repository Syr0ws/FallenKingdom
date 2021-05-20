package com.github.syr0ws.fallenkingdom.displays.impl;

import com.github.syr0ws.fallenkingdom.displays.TextDisplay;

public abstract class Title extends TextDisplay {

    public static final int DEFAULT_FADE_IN = 10;
    public static final int DEFAULT_STAY = 20;
    public static final int DEFAULT_FADE_OUT = 70;

    private final String title, subtitle;
    private final int fadeIn, fadeOut, stay;

    public Title(String title, String subtitle) {

        if(title == null)
            throw new IllegalArgumentException("Title cannot be null.");

        if(subtitle == null)
            throw new IllegalArgumentException("Subtitle cannot be null.");

        this.title = title;
        this.subtitle = subtitle;
        this.fadeIn = DEFAULT_FADE_IN;
        this.stay = DEFAULT_STAY;
        this.fadeOut = DEFAULT_FADE_OUT;
    }

    public Title(String title, String subtitle, int fadeIn, int stay, int fadeOut) {

        if(title == null)
            throw new IllegalArgumentException("Title cannot be null.");

        if(subtitle == null)
            throw new IllegalArgumentException("Subtitle cannot be null.");

        this.title = title;
        this.subtitle = subtitle;
        this.fadeIn = fadeIn;
        this.stay = stay;
        this.fadeOut = fadeOut;
    }

    public String getTitle() {
        return super.format(this.title);
    }

    public String getSubtitle() {
        return super.format(this.subtitle);
    }

    public int getFadeIn() {
        return this.fadeIn;
    }

    public int getFadeOut() {
        return this.fadeOut;
    }

    public int getStay() {
        return this.stay;
    }
}
