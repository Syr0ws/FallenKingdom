package com.github.syr0ws.fallenkingdom.tools.v2;

public class TimeUnitTranslation {

    private final String wording, shortWording, pluralWording;

    public TimeUnitTranslation(String wording, String shortWording, String pluralWording) {
        this.wording = wording == null ? "" : wording;
        this.shortWording = shortWording == null ? "" : shortWording;
        this.pluralWording = pluralWording == null ? "" : pluralWording;
    }

    public String getWording() {
        return this.wording;
    }

    public String getShortWording() {
        return this.shortWording;
    }

    public String getPluralWording() {
        return this.pluralWording;
    }
}
