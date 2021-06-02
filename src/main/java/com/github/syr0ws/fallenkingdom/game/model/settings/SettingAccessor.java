package com.github.syr0ws.fallenkingdom.game.model.settings;

import com.github.syr0ws.fallenkingdom.settings.types.MutableSetting;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.List;

public interface SettingAccessor {

    MutableSetting<Integer> getMaxPlayersSetting();

    MutableSetting<Integer> getMaxSpectatorsSetting();

    MutableSetting<Boolean> getKickEliminatedSetting();

    MutableSetting<Integer> getPvPActivationTimeSetting();

    MutableSetting<Integer> getAssaultsActivationTimeSetting();

    MutableSetting<Integer> getMaxGameDurationSetting();

    MutableSetting<Integer> getStartingCycleDurationSetting();

    MutableSetting<Integer> getCatcherPercentageSetting();

    MutableSetting<Boolean> getAllowWaitingChatSetting();

    MutableSetting<String> getWaitingChatFormatSetting();

    MutableSetting<Boolean> getAllowGameChatSetting();

    MutableSetting<String> getGameChatFormatSetting();

    MutableSetting<Boolean> getAllowTeamChatSetting();

    MutableSetting<Character> getTeamChatPrefixSetting();

    MutableSetting<String> getTeamChatFormatSetting();

    MutableSetting<Boolean> getAllowSpectatorChatSetting();

    MutableSetting<String> getSpectatorChatFormatSetting();

    MutableSetting<Boolean> getFriendlyFireSetting();

    MutableSetting<Location> getGameSpawnSetting();

    MutableSetting<List<Material>> getAllowedBlocksSetting();

    MutableSetting<List<Material>> getVaultAllowedBlocksSetting();
}
