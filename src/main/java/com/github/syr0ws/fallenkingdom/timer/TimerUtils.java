package com.github.syr0ws.fallenkingdom.timer;

import com.github.syr0ws.fallenkingdom.timer.impl.DisplayAction;
import com.github.syr0ws.universe.displays.Display;
import com.github.syr0ws.universe.displays.DisplayException;
import com.github.syr0ws.universe.displays.DisplayFactory;
import com.github.syr0ws.universe.displays.impl.SimpleDisplayFactory;
import com.github.syr0ws.universe.modules.lang.LangService;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Collection;
import java.util.Map;

public class TimerUtils {

    public static void loadDisplayActions(TimerActionManager manager, LangService service, ConfigurationSection section) {

        DisplayFactory factory = new SimpleDisplayFactory(service);
        TimerDisplayDAO dao = new TimerDisplayDAO(factory, section);

        try {

            Map<Integer, Collection<Display>> displays = dao.getTimeDisplays("displays");

            displays.forEach((time, list) -> list.stream()
                    .map(DisplayAction::new)
                    .forEach(action -> manager.addAction(time, action)));

        } catch (DisplayException e) { e.printStackTrace(); }
    }
}
