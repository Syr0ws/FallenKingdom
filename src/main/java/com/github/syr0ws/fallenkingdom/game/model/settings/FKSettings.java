package com.github.syr0ws.fallenkingdom.game.model.settings;

import com.github.syr0ws.fallenkingdom.capture.CaptureType;
import com.github.syr0ws.universe.commons.settings.GameSettings;
import com.github.syr0ws.universe.sdk.settings.types.MutableSetting;
import org.bukkit.Material;

import java.util.List;

public interface FKSettings extends GameSettings {

    MutableSetting<Boolean> getKickEliminatedSetting();

    MutableSetting<Integer> getPvPActivationTimeSetting();

    MutableSetting<Integer> getAssaultsActivationTimeSetting();

    MutableSetting<Integer> getStartingCycleDurationSetting();

    MutableSetting<Boolean> getAllowTeamChatSetting();

    MutableSetting<Character> getTeamChatPrefixSetting();

    MutableSetting<String> getTeamChatFormatSetting();

    MutableSetting<Boolean> getFriendlyFireSetting();

    MutableSetting<Boolean> getEliminateOnCaptureSetting();

    MutableSetting<List<Material>> getAllowedBlocksSetting();

    MutableSetting<List<Material>> getVaultAllowedBlocksSetting();

    MutableSetting<Boolean> getAllowRespawnBedSetting();

    MutableSetting<CaptureType> getCaptureTypeSetting();
}
