package com.github.syr0ws.fallenkingdom.utils;

import com.github.syr0ws.fallenkingdom.tools.direction.Direction;
import com.github.syr0ws.fallenkingdom.tools.time.TimeUnit;
import com.github.syr0ws.fallenkingdom.tools.time.TimeUnitTranslation;
import com.github.syr0ws.universe.commons.modules.lang.LangService;
import com.github.syr0ws.universe.commons.modules.lang.messages.impl.Text;

import java.util.HashMap;
import java.util.Map;

public class LangUtils {

    public static String getDirection(Direction direction, LangService service) {

        if(direction == null)
            throw new IllegalArgumentException("Direction cannot be null.");

        if(service == null)
            throw new IllegalArgumentException("LangService cannot be null.");

        String key = direction.name().toLowerCase().replace("_", "-");

        return service.getMessage("direction." + key, Text.class).getText();
    }

    public static Map<TimeUnit, TimeUnitTranslation> loadTranslations(LangService service) {

        if(service == null)
            throw new IllegalArgumentException("LangService cannot be null.");

        Map<TimeUnit, TimeUnitTranslation> translations = new HashMap<>();

        for(TimeUnit unit : TimeUnit.values()) {

            TimeUnitTranslation translation = LangUtils.loadTranslation(unit, service);
            translations.put(unit, translation);
        }
        return translations;
    }

    public static TimeUnitTranslation loadTranslation(TimeUnit unit, LangService service) {

        if(unit == null)
            throw new IllegalArgumentException("TimeUnit cannot be null.");

        if(service == null)
            throw new IllegalArgumentException("LangService cannot be null.");

        String section = "time-units";

        String wording = service.getMessage(section + "." + unit.getKey() + ".long", Text.class).getText();
        String shortWording = service.getMessage(section + "." + unit.getKey() + ".short", Text.class).getText();
        String pluralWording = service.getMessage(section + "." + unit.getKey() + ".plural", Text.class).getText();

        return new TimeUnitTranslation(wording, shortWording, pluralWording);
    }
}
