package com.github.syr0ws.fallenkingdom.plugin;

import com.github.syr0ws.fallenkingdom.api.controller.FKController;
import com.github.syr0ws.fallenkingdom.api.model.FKModel;
import com.github.syr0ws.fallenkingdom.plugin.chat.PlayingChat;
import com.github.syr0ws.fallenkingdom.plugin.chat.TeamChat;
import com.github.syr0ws.fallenkingdom.plugin.commands.CommandFK;
import com.github.syr0ws.fallenkingdom.plugin.displays.GameDisplayEnum;
import com.github.syr0ws.fallenkingdom.plugin.game.controller.CraftFKController;
import com.github.syr0ws.fallenkingdom.plugin.game.model.CraftFKModel;
import com.github.syr0ws.fallenkingdom.plugin.game.model.GameInitializer;
import com.github.syr0ws.fallenkingdom.plugin.listeners.FKListener;
import com.github.syr0ws.fallenkingdom.plugin.listeners.TeamListener;
import com.github.syr0ws.fallenkingdom.plugin.modes.FKPlayingMode;
import com.github.syr0ws.fallenkingdom.plugin.modes.FKSpectatorMode;
import com.github.syr0ws.fallenkingdom.plugin.modes.FKWaitingMode;
import com.github.syr0ws.universe.api.displays.DisplayManager;
import com.github.syr0ws.universe.api.game.mode.ModeManager;
import com.github.syr0ws.universe.api.game.model.GameException;
import com.github.syr0ws.universe.api.modules.ModuleService;
import com.github.syr0ws.universe.sdk.Game;
import com.github.syr0ws.universe.sdk.chat.DefaultSpectatorChat;
import com.github.syr0ws.universe.sdk.chat.DefaultWaitingChat;
import com.github.syr0ws.universe.sdk.displays.DisplayUtils;
import com.github.syr0ws.universe.sdk.listeners.ListenerManager;
import com.github.syr0ws.universe.sdk.modules.ModuleEnum;
import com.github.syr0ws.universe.sdk.modules.chat.ChatModel;
import com.github.syr0ws.universe.sdk.modules.chat.ChatModule;
import com.github.syr0ws.universe.sdk.modules.lang.LangModule;
import com.github.syr0ws.universe.sdk.modules.lang.LangService;

import java.util.Optional;

public class FKGame extends Game {

    private CraftFKModel model;
    private CraftFKController controller;
    private DisplayManager displayManager;

    @Override
    public void onLoad() {
        super.onLoad();

        // Loading modules.
        this.registerModules();

        // Loading config.
        super.saveConfig();
    }

    @Override
    public void onEnable() {
        super.onEnable();

        try {

            // DisplayManager setup.
            this.setupDisplayManager();

            // Model setup.
            this.setupGameModel();

            // Controller setup.
            this.setupGameController();

            // Registering game modes.
            this.registerGameModes();

            // Registering chats.
            this.registerChats();

            // Registering commands.
            this.registerCommands();

            // Registering listeners.
            this.registerListeners();

        } catch (GameException e) { e.printStackTrace(); }
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @Override
    public FKModel getGameModel() {
        return this.model;
    }

    @Override
    public FKController getGameController() {
        return this.controller;
    }

    private void setupGameModel() throws GameException {
        GameInitializer initializer = new GameInitializer(this);
        this.model = initializer.getGame();
    }

    private void setupGameController() {
        this.controller = new CraftFKController(this, this.model);
        this.controller.enable();
    }

    private void registerModules() {

        ModuleService service = super.getModuleService();
        service.registerModule(ModuleEnum.COMBAT_MODULE.newInstance(this));
        service.registerModule(ModuleEnum.CHAT_MODULE.newInstance(this));
        service.registerModule(ModuleEnum.BORDER_MODULE.newInstance(this));
        service.registerModule(ModuleEnum.VIEW_MODULE.newInstance(this));
        service.registerModule(ModuleEnum.LANG_MODULE.newInstance(this));
        service.registerModule(ModuleEnum.WEATHER_MODULE.newInstance(this));
    }

    private void registerGameModes() {

        ModeManager manager = this.controller.getModeManager();

        manager.registerMode(new FKWaitingMode(this.model, this));
        manager.registerMode(new FKPlayingMode(this.model, this));
        manager.registerMode(new FKSpectatorMode(this.model, this));
    }

    private void registerChats() {

        ModuleService service = this.getModuleService();

        Optional<ChatModule> optional = service.getModule(ModuleEnum.CHAT_MODULE.getName(), ChatModule.class);

        if(!optional.isPresent())
            throw new UnsupportedOperationException("ChatModule not enabled.");

        ChatModule module = optional.get();
        ChatModel chatService = module.getChatModel();

        chatService.registerChat(new DefaultWaitingChat(this.model));
        chatService.registerChat(new DefaultSpectatorChat(this.model));
        chatService.registerChat(new PlayingChat(this.model));
        chatService.registerChat(new TeamChat(this.model));
    }

    private void registerCommands() {

        LangService service = this.getLangService();

        super.getCommand("fk").setExecutor(new CommandFK(this.model, this.controller, service));
    }

    private void registerListeners() {

        ListenerManager manager = super.getListenerManager();
        LangService service = this.getLangService();

        manager.addListener(new FKListener(service));
        manager.addListener(new TeamListener(this.displayManager));
    }

    private void setupDisplayManager() {

        this.displayManager = DisplayUtils.getDisplayManager(this);

        for(GameDisplayEnum value : GameDisplayEnum.values())
            this.displayManager.loadDisplays(value.getPath());
    }

    public LangService getLangService() {

        ModuleService service = this.getModuleService();

        Optional<LangModule> optional = service.getModule(ModuleEnum.LANG_MODULE.getName(), LangModule.class);

        if(!optional.isPresent())
            throw new NullPointerException("LangModule not enabled.");

        return optional.get().getLangService();
    }

    public DisplayManager getDisplayManager() {
        return this.displayManager;
    }
}
