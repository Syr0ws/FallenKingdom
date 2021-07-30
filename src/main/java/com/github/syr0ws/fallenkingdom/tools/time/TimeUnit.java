package com.github.syr0ws.fallenkingdom.tools.time;

public enum TimeUnit {

    SECOND("second", "seconds"),
    MINUTE("minute", "minutes"),
    HOUR("hour", "hours"),
    DAY("day", "days");

    private final String key;
    private final String placeholder;

    TimeUnit(String key, String placeholder) {
        this.key = key;
        this.placeholder = placeholder;
    }

    public String getKey() {
        return this.key;
    }

    public String getPlaceholder() {
        return "%" + this.placeholder + "%";
    }

    public String getShortPlaceholder() {
        return "%short_" + this.placeholder + "%";
    }
}
