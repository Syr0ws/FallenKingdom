package com.github.syr0ws.fallenkingdom.tools;

import com.github.syr0ws.fallenkingdom.displays.impl.Message;
import com.github.syr0ws.fallenkingdom.displays.placeholders.Placeholder;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;

public class PeriodFormatter {

    private final Map<TimeUnit, String> units;
    private final boolean shorts;

    public PeriodFormatter(Map<TimeUnit, String> units, boolean shorts) {

        if(units == null)
            throw new IllegalArgumentException("Units map cannot be null.");

        if(units.isEmpty())
            throw new IllegalArgumentException("Unis map cannot be empty.");

        this.units = units;
        this.shorts = shorts;
    }

    public PeriodFormatter(ConfigurationSection section) {

        Map<TimeUnit, String> units = new HashMap<>();
        units.put(TimeUnit.DAY, section.getString("day", "d"));
        units.put(TimeUnit.HOUR, section.getString("hour", "h"));
        units.put(TimeUnit.MINUTE, section.getString("minute", "m"));
        units.put(TimeUnit.SECOND, section.getString("second", "s"));

        this.units = units;
        this.shorts = section.getBoolean("use-short-units", false);
    }

    public String format(String string, long duration) {

        int[] values = this.getTimeValues(duration);

        Message message = new Message(string);

        message.addPlaceholder(TimePlaceholder.DAY, this.getUnit(TimeUnit.DAY, values[0]));
        message.addPlaceholder(TimePlaceholder.HOUR, this.getUnit(TimeUnit.HOUR, values[1]));
        message.addPlaceholder(TimePlaceholder.MINUTE, this.getUnit(TimeUnit.MINUTE, values[2]));
        message.addPlaceholder(TimePlaceholder.SECOND, this.getUnit(TimeUnit.SECOND, values[3]));

        return message.getText();
    }

    public String formatDuration(long duration) {

        StringBuilder sb = new StringBuilder();

        int[] values = this.getTimeValues(duration);

        if(values[0] > 0) this.appendUnit(sb, TimeUnit.DAY, values[0]);

        if(values[1] > 0) this.appendUnit(sb, TimeUnit.HOUR, values[1]);

        if(values[2] > 0) this.appendUnit(sb, TimeUnit.MINUTE, values[2]);

        if(sb.length() == 0 || values[3] > 0) this.appendUnit(sb, TimeUnit.SECOND, values[3]);

        return sb.toString();
    }

    public String getUnit(TimeUnit unit, int value) {

        StringBuilder sb = new StringBuilder();

        if(value > 0) {

            sb.append(value);
            sb.append(this.shorts ? "" : " ");
            sb.append(this.getUnit(unit));

            if(!this.shorts) sb.append("s");
        }

        return sb.toString();
    }

    public String getUnit(TimeUnit unit) {

        if(!this.units.containsKey(unit))
            throw new IllegalStateException(String.format("TimeUnit '%s' not found.", unit.name()));

        return this.units.get(unit);
    }

    private int[] getTimeValues(long duration) {

        int days = (int) (duration / 86400);
        duration -= days * 86400L;

        int hours = (int) (duration / 3600);
        duration -= hours * 3600L;

        int minutes = (int) (duration / 60);
        duration -= minutes * 60L;

        int seconds = (int) duration;

        return new int[]{days, hours, minutes, seconds};
    }

    private void appendUnit(StringBuilder sb, TimeUnit unit, int value) {

        if(sb.length() != 0) sb.append(" "); // Adding a space before a value if it is not the first.

        sb.append(value)
                .append(this.shorts ? "" : " ")
                .append(this.getUnit(unit))
                .append(this.shorts || value < 1 ? "" : "s");
    }

    private enum TimePlaceholder implements Placeholder {

        DAY("day"), HOUR("hour"), MINUTE("minute"), SECOND("second");

        private final String placeholder;

        TimePlaceholder(String placeholder) {
            this.placeholder = placeholder;
        }

        @Override
        public String get() {
            return "%" + placeholder + "%";
        }
    }
}
