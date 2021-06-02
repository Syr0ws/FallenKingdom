package com.github.syr0ws.fallenkingdom.game.model.settings;

import com.github.syr0ws.fallenkingdom.settings.dao.ConfigSettingLoader;
import com.github.syr0ws.fallenkingdom.settings.dao.SettingLoader;
import com.github.syr0ws.fallenkingdom.settings.manager.CacheSettingManager;
import com.github.syr0ws.fallenkingdom.settings.manager.SettingManager;
import com.github.syr0ws.fallenkingdom.settings.types.CharacterSetting;
import com.github.syr0ws.fallenkingdom.settings.types.LocationSetting;
import com.github.syr0ws.fallenkingdom.settings.types.MaterialSetting;
import com.github.syr0ws.fallenkingdom.settings.types.MutableSetting;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class GameSettingAccessor implements SettingAccessor {

    private final SettingManager manager;

    public GameSettingAccessor() {
        this.manager = new CacheSettingManager();
    }

    public void init(FileConfiguration config) {

        SettingManager manager = new CacheSettingManager();

        for (GameSettingEnum value : GameSettingEnum.values()) {
            manager.addSetting(value, value.getSetting());
        }

        SettingLoader loader = new ConfigSettingLoader(config);
        loader.load(this.manager.getSettings());
    }

    @Override
    public MutableSetting<Integer> getMaxPlayersSetting() {
        return this.manager.getGenericSetting(GameSettingEnum.MAX_PLAYERS, Integer.class);
    }

    @Override
    public MutableSetting<Integer> getMaxSpectatorsSetting() {
        return this.manager.getGenericSetting(GameSettingEnum.MAX_SPECTATORS, Integer.class);
    }

    @Override
    public MutableSetting<Boolean> getKickEliminatedSetting() {
        return this.manager.getGenericSetting(GameSettingEnum.KICK_ELIMINATED, Boolean.class);
    }

    @Override
    public MutableSetting<Integer> getPvPActivationTimeSetting() {
        return this.manager.getGenericSetting(GameSettingEnum.PVP_ACTIVATION_TIME, Integer.class);
    }

    @Override
    public MutableSetting<Integer> getAssaultsActivationTimeSetting() {
        return this.manager.getGenericSetting(GameSettingEnum.ASSAULTS_ACTIVATION_TIME, Integer.class);
    }

    @Override
    public MutableSetting<Integer> getMaxGameDurationSetting() {
        return this.manager.getGenericSetting(GameSettingEnum.MAX_GAME_DURATION, Integer.class);
    }

    @Override
    public MutableSetting<Integer> getStartingCycleDurationSetting() {
        return this.manager.getGenericSetting(GameSettingEnum.STARTING_CYCLE_DURATION, Integer.class);
    }

    @Override
    public MutableSetting<Integer> getCatcherPercentageSetting() {
        return this.manager.getGenericSetting(GameSettingEnum.CATCHER_PERCENTAGE, Integer.class);
    }

    @Override
    public MutableSetting<Boolean> getAllowWaitingChatSetting() {
        return this.manager.getGenericSetting(GameSettingEnum.ALLOW_WAITING_CHAT, Boolean.class);
    }

    @Override
    public MutableSetting<String> getWaitingChatFormatSetting() {
        return this.manager.getGenericSetting(GameSettingEnum.WAITING_CHAT_FORMAT, String.class);
    }

    @Override
    public MutableSetting<Boolean> getAllowGameChatSetting() {
        return this.manager.getGenericSetting(GameSettingEnum.ALLOW_GAME_CHAT, Boolean.class);
    }

    @Override
    public MutableSetting<String> getGameChatFormatSetting() {
        return this.manager.getGenericSetting(GameSettingEnum.GAME_CHAT_FORMAT, String.class);
    }

    @Override
    public MutableSetting<Boolean> getAllowTeamChatSetting() {
        return this.manager.getGenericSetting(GameSettingEnum.ALLOW_TEAM_CHAT, Boolean.class);
    }

    @Override
    public MutableSetting<Character> getTeamChatPrefixSetting() {
        return this.manager.getSetting(GameSettingEnum.TEAM_CHAT_PREFIX, CharacterSetting.class);
    }

    @Override
    public MutableSetting<String> getTeamChatFormatSetting() {
        return this.manager.getGenericSetting(GameSettingEnum.TEAM_CHAT_FORMAT, String.class);
    }

    @Override
    public MutableSetting<Boolean> getAllowSpectatorChatSetting() {
        return this.manager.getGenericSetting(GameSettingEnum.ALLOW_SPECTATOR_CHAT, Boolean.class);
    }

    @Override
    public MutableSetting<String> getSpectatorChatFormatSetting() {
        return this.manager.getGenericSetting(GameSettingEnum.SPECTATOR_CHAT_FORMAT, String.class);
    }

    @Override
    public MutableSetting<Boolean> getFriendlyFireSetting() {
        return this.manager.getGenericSetting(GameSettingEnum.FRIENDLY_FIRE, Boolean.class);
    }

    @Override
    public MutableSetting<Location> getGameSpawnSetting() {
        return this.manager.getSetting(GameSettingEnum.GAME_SPAWN, LocationSetting.class);
    }

    @Override
    public MutableSetting<List<Material>> getAllowedBlocksSetting() {
        return this.manager.getSetting(GameSettingEnum.ALLOWED_BLOCKS, MaterialSetting.class);
    }

    @Override
    public MutableSetting<List<Material>> getVaultAllowedBlocksSetting() {
        return this.manager.getSetting(GameSettingEnum.VAULT_ALLOWED_BLOCKS, MaterialSetting.class);
    }
}
