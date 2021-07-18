package com.github.syr0ws.fallenkingdom.utils;

import com.github.syr0ws.universe.Game;
import com.github.syr0ws.universe.displays.Display;
import com.github.syr0ws.universe.displays.DisplayFactory;
import com.github.syr0ws.universe.displays.DisplayManager;
import com.github.syr0ws.universe.displays.dao.ConfigDisplayDAO;
import com.github.syr0ws.universe.displays.dao.DisplayDAO;
import com.github.syr0ws.universe.displays.impl.SimpleDisplayFactory;
import com.github.syr0ws.universe.displays.impl.SimpleDisplayManager;
import com.github.syr0ws.universe.displays.types.TextDisplay;
import com.github.syr0ws.universe.modules.ModuleEnum;
import com.github.syr0ws.universe.modules.ModuleService;
import com.github.syr0ws.universe.modules.lang.LangModule;
import com.github.syr0ws.universe.modules.lang.LangService;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public class DisplayUtils {

    public static DisplayManager getDisplayManager(Game game) {

        ModuleService service = game.getModuleService();

        Optional<LangModule> optional = service.getModule(ModuleEnum.LANG_MODULE.getName(), LangModule.class);
        LangService langService = optional.map(LangModule::getLangService).orElse(null);

        DisplayFactory factory = new SimpleDisplayFactory(langService);
        DisplayDAO dao = new ConfigDisplayDAO(factory, game.getConfig());

        return new SimpleDisplayManager(dao);
    }

    public static void addPlaceholders(Collection<Display> displays, Map<String, String> placeholders) {
        displays.stream()
                .filter(display -> display instanceof TextDisplay)
                .map(display -> (TextDisplay) display)
                .forEach(display -> display.addPlaceholders(placeholders));
    }

    public static void sendDisplays(Collection<Display> displays, Player player) {
        displays.forEach(display -> display.displayTo(player));
    }

    public static void sendDisplays(Collection<Display> displays, Collection<? extends Player> players) {
        players.forEach(player ->
                displays.forEach(display -> display.displayTo(player)));
    }
}
