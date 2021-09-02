package com.github.syr0ws.fallenkingdom.plugin;

import com.github.syr0ws.fallenkingdom.api.controller.FKController;
import com.github.syr0ws.fallenkingdom.api.model.FKModel;
import com.github.syr0ws.fallenkingdom.plugin.chat.PlayingChat;
import com.github.syr0ws.fallenkingdom.plugin.chat.TeamChat;
import com.github.syr0ws.fallenkingdom.plugin.commands.CommandFK;
import com.github.syr0ws.fallenkingdom.plugin.game.controller.CraftFKController;
import com.github.syr0ws.fallenkingdom.plugin.game.model.CraftFKModel;
import com.github.syr0ws.fallenkingdom.plugin.game.model.GameInitializer;
import com.github.syr0ws.fallenkingdom.plugin.game.view.FKGameViewHandler;
import com.github.syr0ws.fallenkingdom.plugin.modes.FKPlayingMode;
import com.github.syr0ws.fallenkingdom.plugin.modes.FKSpectatorMode;
import com.github.syr0ws.fallenkingdom.plugin.modes.FKWaitingMode;
import com.github.syr0ws.universe.api.game.mode.ModeManager;
import com.github.syr0ws.universe.api.game.model.GameException;
import com.github.syr0ws.universe.api.modules.ModuleService;
import com.github.syr0ws.universe.sdk.Game;
import com.github.syr0ws.universe.sdk.chat.DefaultSpectatorChat;
import com.github.syr0ws.universe.sdk.chat.DefaultWaitingChat;
import com.github.syr0ws.universe.sdk.modules.ModuleEnum;
import com.github.syr0ws.universe.sdk.modules.chat.ChatModel;
import com.github.syr0ws.universe.sdk.modules.chat.ChatModule;
import com.github.syr0ws.universe.sdk.modules.lang.LangModule;
import com.github.syr0ws.universe.sdk.modules.lang.LangService;

import java.util.Optional;

public class FKGame extends Game {

    private CraftFKModel model;
    private CraftFKController controller;
    private FKGameViewHandler handler;

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

            // Model setup.
            this.setupGameModel();

            // Controller setup.
            this.setupGameController();

            // View setup.
            this.setupGameViewHandler();

            // Registering game modes.
            this.registerGameModes();

            // Registering chats.
            this.registerChats();

            // Registering commands.
            this.registerCommands();

        } catch (GameException e) { e.printStackTrace(); }
    }

    @Override
    public void onDisable() {
        super.onDisable();

        this.controller.disable(); // Disabling controller.
        this.handler.disable(); // Disabling game view handler.

        // Avoiding reuse.
        this.model = null;
        this.controller = null;
        this.handler = null;
    }

    @Override
    public FKModel getGameModel() {
        return this.model;
    }

    @Override
    public FKController getGameController() {
        return this.controller;
    }

    @Override
    public FKGameViewHandler getGameViewHandler() {
        return this.handler;
    }

    private void setupGameModel() throws GameException {
        GameInitializer initializer = new GameInitializer(this);
        this.model = initializer.getGame();
    }

    private void setupGameController() {
        this.controller = new CraftFKController(this, this.model);
        this.controller.enable();
    }

    private void setupGameViewHandler() {
        this.handler = new FKGameViewHandler(this, this.model);
        this.handler.enable();
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

        ChatModule module = service.getModule(ModuleEnum.CHAT_MODULE.getName(), ChatModule.class)
                .orElseThrow(() -> new IllegalStateException("ChatModule not enabled."));

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

    public LangService getLangService() {

        ModuleService service = this.getModuleService();

        Optional<LangModule> optional = service.getModule(ModuleEnum.LANG_MODULE.getName(), LangModule.class);

        if(!optional.isPresent())
            throw new NullPointerException("LangModule not enabled.");

        return optional.get().getLangService();
    }
}
