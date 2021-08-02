package com.github.syr0ws.fallenkingdom;

import com.github.syr0ws.fallenkingdom.chat.PlayingChat;
import com.github.syr0ws.fallenkingdom.chat.TeamChat;
import com.github.syr0ws.fallenkingdom.commands.CommandFK;
import com.github.syr0ws.fallenkingdom.game.controller.CraftFKController;
import com.github.syr0ws.fallenkingdom.game.controller.FKController;
import com.github.syr0ws.fallenkingdom.game.model.CraftFKModel;
import com.github.syr0ws.fallenkingdom.game.model.FKModel;
import com.github.syr0ws.fallenkingdom.game.model.GameInitializer;
import com.github.syr0ws.fallenkingdom.listeners.FKListener;
import com.github.syr0ws.fallenkingdom.modes.FKPlayingMode;
import com.github.syr0ws.fallenkingdom.modes.FKSpectatorMode;
import com.github.syr0ws.fallenkingdom.modes.FKWaitingMode;
import com.github.syr0ws.universe.commons.chat.DefaultSpectatorChat;
import com.github.syr0ws.universe.commons.chat.DefaultWaitingChat;
import com.github.syr0ws.universe.commons.modules.ModuleEnum;
import com.github.syr0ws.universe.commons.modules.ModuleService;
import com.github.syr0ws.universe.commons.modules.chat.ChatModel;
import com.github.syr0ws.universe.commons.modules.chat.ChatModule;
import com.github.syr0ws.universe.commons.modules.lang.LangModule;
import com.github.syr0ws.universe.commons.modules.lang.LangService;
import com.github.syr0ws.universe.sdk.Game;
import com.github.syr0ws.universe.sdk.game.mode.ModeManager;
import com.github.syr0ws.universe.sdk.game.model.GameException;
import com.github.syr0ws.universe.sdk.listeners.ListenerManager;

import java.util.Optional;

public class FKGame extends Game {

    private CraftFKModel model;
    private CraftFKController controller;

    @Override
    public void onEnable() {

        // Loading config.
        super.saveDefaultConfig();

        try {

            // Loading modules.
            this.loadModules();

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
    }

    private void loadModules() {

        ModuleService service = super.getModuleService();
        service.enableModule(ModuleEnum.COMBAT_MODULE.newInstance(this));
        service.enableModule(ModuleEnum.CHAT_MODULE.newInstance(this));
        service.enableModule(ModuleEnum.BORDER_MODULE.newInstance(this));
        service.enableModule(ModuleEnum.VIEW_MODULE.newInstance(this));
        service.enableModule(ModuleEnum.LANG_MODULE.newInstance(this));
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
    }

    public LangService getLangService() {

        ModuleService service = this.getModuleService();

        Optional<LangModule> optional = service.getModule(ModuleEnum.LANG_MODULE.getName(), LangModule.class);

        if(!optional.isPresent())
            throw new NullPointerException("LangModule not enabled.");

        return optional.get().getLangService();
    }
}
