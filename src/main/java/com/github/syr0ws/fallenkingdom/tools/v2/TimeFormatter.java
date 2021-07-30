package com.github.syr0ws.fallenkingdom.tools.v2;

import com.github.syr0ws.universe.sdk.displays.types.Message;

import java.util.Map;

public class TimeFormatter {

    private final Map<TimeUnit, TimeUnitTranslation> units;

    public TimeFormatter(Map<TimeUnit, TimeUnitTranslation> units) {

        if(units == null)
            throw new IllegalArgumentException("Map cannot be null.");

        if(units.isEmpty())
            throw new IllegalArgumentException("Map cannot be empty.");

        this.units = units;
    }

    public String format(String string, long duration) {

        int[] values = this.getTimeValues(duration);

        Message message = new Message(string);

        for(int index = 0; index < values.length; index++) {

            int value = values[index];

            TimeUnit unit = TimeUnit.values()[index];
            TimeUnitTranslation translation = this.units.get(unit);

            if(translation == null) continue;

            this.addPlaceholder(message, unit, translation, value);
        }

        return message.getText();
    }

    private void addPlaceholder(Message message, TimeUnit unit, TimeUnitTranslation translation, int value) {

        String timeValue;

        if(value == 1) timeValue = value + translation.getWording();
        else timeValue = value + translation.getPluralWording();

        String shortTimeValue = value >= 1 ? value + translation.getShortWording() : "";

        message.addPlaceholder(unit.getPlaceholder(), timeValue);
        message.addPlaceholder(unit.getShortPlaceholder(), shortTimeValue);
    }

    private int[] getTimeValues(long duration) {

        int days = 0, hours = 0, minutes = 0, seconds = 0;

        if(this.isSet(TimeUnit.DAY)) {

            days = (int) (duration / 86400);
            duration -= days * 86400L;
        }

        if(this.isSet(TimeUnit.HOUR)) {

            hours = (int) (duration / 3600);
            duration -= hours * 3600L;
        }

        if(this.isSet(TimeUnit.MINUTE)) {

            minutes = (int) (duration / 60);
            duration -= minutes * 60L;
        }

        if(this.isSet(TimeUnit.SECOND)) {

            seconds = (int) duration;
        }

        return new int[]{seconds, minutes, hours, days};
    }

    private boolean isSet(TimeUnit unit) {
        return this.units.containsKey(unit);
    }
}
