package com.github.syr0ws.fallenkingdom;

import com.github.syr0ws.fallenkingdom.chat.PlayingChat;
import com.github.syr0ws.fallenkingdom.chat.SpectatorChat;
import com.github.syr0ws.fallenkingdom.chat.TeamChat;
import com.github.syr0ws.fallenkingdom.chat.WaitingChat;
import com.github.syr0ws.fallenkingdom.commands.CommandFK;
import com.github.syr0ws.fallenkingdom.game.controller.CraftFKController;
import com.github.syr0ws.fallenkingdom.game.controller.FKController;
import com.github.syr0ws.fallenkingdom.game.model.CraftFKModel;
import com.github.syr0ws.fallenkingdom.game.model.FKModel;
import com.github.syr0ws.fallenkingdom.game.model.GameInitializer;
import com.github.syr0ws.fallenkingdom.modes.PlayingMode;
import com.github.syr0ws.fallenkingdom.modes.SpectatorMode;
import com.github.syr0ws.fallenkingdom.modes.WaitingMode;
import com.github.syr0ws.fallenkingdom.notifiers.AssaultsNotifier;
import com.github.syr0ws.fallenkingdom.notifiers.PvPNotifier;
import com.github.syr0ws.universe.Game;
import com.github.syr0ws.universe.game.model.GameException;
import com.github.syr0ws.universe.game.model.mode.ModeFactory;
import com.github.syr0ws.universe.modules.ModuleEnum;
import com.github.syr0ws.universe.modules.ModuleService;
import com.github.syr0ws.universe.modules.chat.ChatModel;
import com.github.syr0ws.universe.modules.chat.ChatModule;

import java.util.Optional;

public class FKGame extends Game {

    private CraftFKModel model;
    private CraftFKController controller;

    @Override
    public void onEnable() {

        // Loading config.
        super.saveDefaultConfig();

        try {

            // Model setup.
            this.setupGameModel();

            // Controller setup.
            this.setupGameController();

            // Loading modules.
            this.loadModules();

            // Registering game modes.
            this.registerGameModes();

            // Registering chats.
            this.registerChats();

            // Adding notifiers.
            this.setupNotifiers();

            // Registering commands.
            this.registerCommands();

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
        service.enableModule(ModuleEnum.SCOREBOARD_MODULE.newInstance(this));
    }

    private void registerGameModes() {
        ModeFactory.registerMode(new WaitingMode(this));
        ModeFactory.registerMode(new PlayingMode(this));
        ModeFactory.registerMode(new SpectatorMode(this));
    }

    private void registerChats() {

        ModuleService service = this.getModuleService();

        Optional<ChatModule> optional = service.getModule(ModuleEnum.CHAT_MODULE.getName(), ChatModule.class);

        if(!optional.isPresent())
            throw new UnsupportedOperationException("ChatModule not registered.");

        ChatModule module = optional.get();
        ChatModel chatService = module.getChatModel();

        chatService.registerChat(new WaitingChat(this.model));
        chatService.registerChat(new PlayingChat(this.model));
        chatService.registerChat(new TeamChat(this.model));
        chatService.registerChat(new SpectatorChat(this.model));
    }

    private void setupNotifiers() {
        this.model.addObserver(new PvPNotifier(this.model, this));
        this.model.addObserver(new AssaultsNotifier(this.model, this));
    }

    private void registerCommands() {
        super.getCommand("fk").setExecutor(new CommandFK(this, this.model, this.controller));
    }
}
