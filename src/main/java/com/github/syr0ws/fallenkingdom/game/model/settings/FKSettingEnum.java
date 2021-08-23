package com.github.syr0ws.fallenkingdom.game.model.settings;

import com.github.syr0ws.fallenkingdom.capture.CaptureType;
import com.github.syr0ws.fallenkingdom.game.model.placeholders.FKPlaceholder;
import com.github.syr0ws.universe.api.settings.Setting;
import com.github.syr0ws.universe.api.settings.SettingType;
import com.github.syr0ws.universe.sdk.game.settings.types.CharacterSetting;
import com.github.syr0ws.universe.sdk.game.settings.types.EnumSetting;
import com.github.syr0ws.universe.sdk.game.settings.types.MaterialSetting;
import com.github.syr0ws.universe.sdk.game.settings.types.SimpleConfigSetting;
import com.github.syr0ws.universe.sdk.placeholders.PlaceholderEnum;
import org.bukkit.Material;

import java.util.ArrayList;

public enum FKSettingEnum implements SettingType {

    KICK_ELIMINATED {
        @Override
        public Setting<?> getSetting() {
            return new SimpleConfigSetting
                    .Builder<>("kickEliminated", false, "kick-eliminated", Boolean.class)
                    .build();
        }
    },

    PVP_ACTIVATION_TIME {
        @Override
        public Setting<?> getSetting() {
            return new SimpleConfigSetting
                    .Builder<>("pvpActivationTime", 1800, "pvp-activation-time", Integer.class)
                    .withFilter(value -> value >= 0)
                    .build();
        }
    },

    ASSAULTS_ACTIVATION_TIME {
        @Override
        public Setting<?> getSetting() {
            return new SimpleConfigSetting
                    .Builder<>("assaultsActivationTime", 2400, "assaults-activation-time", Integer.class)
                    .withFilter(value -> value >= 0)
                    .build();
        }
    },

    NETHER_ACTIVATION_TIME {
        @Override
        public Setting<?> getSetting() {
            return new SimpleConfigSetting
                    .Builder<>("netherActivationTime", 900, "nether-activation-time", Integer.class)
                    .withFilter(value -> value >= 0)
                    .build();
        }
    },

    END_ACTIVATION_TIME {
        @Override
        public Setting<?> getSetting() {
            return new SimpleConfigSetting
                    .Builder<>("endActivationTime", 1800, "end-activation-time", Integer.class)
                    .withFilter(value -> value >= 0)
                    .build();
        }
    },

    ALLOW_END {
        @Override
        public Setting<?> getSetting() {
            return new SimpleConfigSetting
                    .Builder<>("allowNether", false, "allow-nether", Boolean.class)
                    .build();
        }
    },

    ALLOW_NETHER {
        @Override
        public Setting<?> getSetting() {
            return new SimpleConfigSetting
                    .Builder<>("allowEnd", false, "allow-end", Boolean.class)
                    .build();
        }
    },

    CATCHER_PERCENTAGE {
        @Override
        public Setting<?> getSetting() {
            return new SimpleConfigSetting
                    .Builder<>("catcherPercentage", 50, "catcher-percentage", Integer.class)
                    .withFilter(value -> value > 0 && value <= 100)
                    .build();
        }
    },

    ALLOW_TEAM_CHAT {
        @Override
        public Setting<?> getSetting() {
            return new SimpleConfigSetting
                    .Builder<>("allowTeamChat", true, "team-chat.enabled", Boolean.class)
                    .build();
        }
    },

    TEAM_CHAT_PREFIX {
        @Override
        public Setting<?> getSetting() {
            return new CharacterSetting
                    .Builder("teamChatPrefix", '@', "team-chat.prefix")
                    .build();
        }
    },

    TEAM_CHAT_FORMAT {
        @Override
        public Setting<?> getSetting() {

            String defaultFormat = String.format("%s %s &r: %s", FKPlaceholder.TEAM_NAME.get(),
                    PlaceholderEnum.PLAYER_NAME.get(), PlaceholderEnum.MESSAGE.get());

            return new SimpleConfigSetting
                    .Builder<>("teamChatFormat", defaultFormat, "team-chat.format", String.class)
                    .withFilter(value -> value != null && !value.isEmpty())
                    .build();
        }
    },

    ALLOWED_BLOCKS {
        @Override
        public Setting<?> getSetting() {
            return new MaterialSetting
                    .Builder("allowedBlocks", new ArrayList<>(), "allowed-blocks")
                    .withFilter(list -> list.stream().allMatch(Material::isBlock))
                    .build();
        }
    },

    VAULT_ALLOWED_BLOCKS {
        @Override
        public Setting<?> getSetting() {
            return new MaterialSetting
                    .Builder("vaultBlocks", new ArrayList<>(), "vault-blocks")
                    .withFilter(list -> list.stream().allMatch(Material::isBlock))
                    .build();
        }
    },

    FRIENDLY_FIRE {
        @Override
        public Setting<?> getSetting() {
            return new SimpleConfigSetting
                    .Builder<>("friendlyFire", false, "friendly-fire", Boolean.class)
                    .build();
        }
    },

    ELIMINATE_ON_CAPTURE {
        @Override
        public Setting<?> getSetting() {
            return new SimpleConfigSetting
                    .Builder<>("eliminateOnCapture", true, "eliminate-on-capture", Boolean.class)
                    .build();
        }
    },

    ALLOW_BED_RESPAWN {
        @Override
        public Setting<?> getSetting() {
            return new SimpleConfigSetting
                    .Builder<>("allowBedRespawn", true, "allow-bed-respawn", Boolean.class)
                    .build();
        }
    },

    CAPTURE_TYPE {
        @Override
        public Setting<?> getSetting() {
            return new EnumSetting
                    .Builder<>("captureType", CaptureType.AREA, CaptureType.class, "capture-type")
                    .build();
        }
    };

    public abstract Setting<?> getSetting();
}
