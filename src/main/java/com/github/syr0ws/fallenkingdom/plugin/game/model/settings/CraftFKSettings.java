package com.github.syr0ws.fallenkingdom.plugin.game.model.settings;

import com.github.syr0ws.fallenkingdom.api.capture.CaptureType;
import com.github.syr0ws.fallenkingdom.api.model.FKSettings;
import com.github.syr0ws.universe.api.settings.MutableSetting;
import com.github.syr0ws.universe.api.settings.SettingLoader;
import com.github.syr0ws.universe.api.settings.SettingManager;
import com.github.syr0ws.universe.sdk.game.settings.DefaultGameSettings;
import com.github.syr0ws.universe.sdk.game.settings.dao.ConfigSettingLoader;
import com.github.syr0ws.universe.sdk.game.settings.types.CharacterSetting;
import com.github.syr0ws.universe.sdk.game.settings.types.MaterialSetting;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class CraftFKSettings extends DefaultGameSettings implements FKSettings {

    public CraftFKSettings(SettingManager manager) {
        super(manager);
    }

    @Override
    public void init(FileConfiguration config) {
        super.init(config);

        SettingManager manager = super.getManager();

        for (FKSettingEnum value : FKSettingEnum.values()) {
            value.registerSetting(manager);
        }

        SettingLoader loader = new ConfigSettingLoader(config);
        loader.load(super.getManager().getSettings());
    }

    @Override
    public MutableSetting<Boolean> getKickEliminatedSetting() {
        return super.getManager().getGenericSetting(FKSettingEnum.KICK_ELIMINATED, Boolean.class);
    }

    @Override
    public MutableSetting<Integer> getPvPActivationTimeSetting() {
        return super.getManager().getGenericSetting(FKSettingEnum.PVP_ACTIVATION_TIME, Integer.class);
    }

    @Override
    public MutableSetting<Integer> getAssaultsActivationTimeSetting() {
        return super.getManager().getGenericSetting(FKSettingEnum.ASSAULTS_ACTIVATION_TIME, Integer.class);
    }

    @Override
    public MutableSetting<Integer> getNetherActivationTimeSetting() {
        return super.getManager().getGenericSetting(FKSettingEnum.NETHER_ACTIVATION_TIME, Integer.class);
    }

    @Override
    public MutableSetting<Integer> getEndActivationTimeSetting() {
        return super.getManager().getGenericSetting(FKSettingEnum.END_ACTIVATION_TIME, Integer.class);
    }

    @Override
    public MutableSetting<Boolean> getAllowNetherSetting() {
        return super.getManager().getGenericSetting(FKSettingEnum.ALLOW_NETHER, Boolean.class);
    }

    @Override
    public MutableSetting<Boolean> getAllowEndSetting() {
        return super.getManager().getGenericSetting(FKSettingEnum.ALLOW_END, Boolean.class);
    }

    @Override
    public MutableSetting<Boolean> getAllowTeamChatSetting() {
        return super.getManager().getGenericSetting(FKSettingEnum.ALLOW_TEAM_CHAT, Boolean.class);
    }

    @Override
    public MutableSetting<Character> getTeamChatPrefixSetting() {
        return super.getManager().getSetting(FKSettingEnum.TEAM_CHAT_PREFIX, CharacterSetting.class);
    }

    @Override
    public MutableSetting<String> getTeamChatFormatSetting() {
        return super.getManager().getGenericSetting(FKSettingEnum.TEAM_CHAT_FORMAT, String.class);
    }

    @Override
    public MutableSetting<Boolean> getFriendlyFireSetting() {
        return super.getManager().getGenericSetting(FKSettingEnum.FRIENDLY_FIRE, Boolean.class);
    }

    @Override
    public MutableSetting<Boolean> getEliminateOnCaptureSetting() {
        return super.getManager().getGenericSetting(FKSettingEnum.ELIMINATE_ON_CAPTURE, Boolean.class);
    }

    @Override
    public MutableSetting<List<Material>> getAllowedBlocksSetting() {
        return super.getManager().getSetting(FKSettingEnum.ALLOWED_BLOCKS, MaterialSetting.class);
    }

    @Override
    public MutableSetting<List<Material>> getVaultAllowedBlocksSetting() {
        return super.getManager().getSetting(FKSettingEnum.VAULT_ALLOWED_BLOCKS, MaterialSetting.class);
    }

    @Override
    public MutableSetting<Boolean> getAllowRespawnBedSetting() {
        return super.getManager().getGenericSetting(FKSettingEnum.ALLOW_BED_RESPAWN, Boolean.class);
    }

    @Override
    public MutableSetting<CaptureType> getCaptureTypeSetting() {
        return super.getManager().getGenericSetting(FKSettingEnum.CAPTURE_TYPE, CaptureType.class);
    }
}
