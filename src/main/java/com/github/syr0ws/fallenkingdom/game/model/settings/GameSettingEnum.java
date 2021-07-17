package com.github.syr0ws.fallenkingdom.game.model.settings;

import com.github.syr0ws.fallenkingdom.capture.CaptureType;
import com.github.syr0ws.fallenkingdom.game.model.placeholders.FKPlaceholder;
import com.github.syr0ws.universe.placeholders.PlaceholderEnum;
import com.github.syr0ws.universe.settings.Setting;
import com.github.syr0ws.universe.settings.SettingType;
import com.github.syr0ws.universe.settings.types.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.ArrayList;

public enum GameSettingEnum implements SettingType {

    MAX_PLAYERS {
        @Override
        public Setting<?> getSetting() {
            return new SimpleConfigSetting
                    .Builder<>("maxPlayers", 16, "max-players", Integer.class)
                    .withFilter(value -> value >= 2)
                    .build();
        }
    },

    MAX_SPECTATORS {
        @Override
        public Setting<?> getSetting() {
            return new SimpleConfigSetting
                    .Builder<>("maxSpectators", 8, "max-spectators", Integer.class)
                    .withFilter(value -> value >= 0)
                    .build();
        }
    },

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

    MAX_GAME_DURATION {
        @Override
        public Setting<?> getSetting() {
            return new SimpleConfigSetting
                    .Builder<>("maxGameDuration", 10800, "max-game-duration", Integer.class)
                    .withFilter(value -> value > 0)
                    .build();
        }
    },

    STARTING_CYCLE_DURATION {
        @Override
        public Setting<?> getSetting() {
            return new SimpleConfigSetting
                    .Builder<>("startingCycleDuration", 10, "starting-cycle-duration", Integer.class)
                    .withFilter(value -> value >= 0)
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

    ALLOW_WAITING_CHAT {
        @Override
        public Setting<?> getSetting() {
            return new SimpleConfigSetting
                    .Builder<>("allowWaitingChat", true, "waiting-cycle-chat.enabled", Boolean.class)
                    .build();
        }
    },

    WAITING_CHAT_FORMAT {
        @Override
        public Setting<?> getSetting() {

            String defaultFormat = String.format("%s &r: %s", PlaceholderEnum.PLAYER_NAME.get(), PlaceholderEnum.MESSAGE.get());

            return new SimpleConfigSetting
                    .Builder<>("waitingChatFormat", defaultFormat, "waiting-cycle-chat.format", String.class)
                    .withFilter(value -> value != null && !value.isEmpty())
                    .build();
        }
    },

    ALLOW_GAME_CHAT {
        @Override
        public Setting<?> getSetting() {
            return new SimpleConfigSetting
                    .Builder<>("allowGameChat", true, "game-chat.enabled", Boolean.class)
                    .build();
        }
    },

    GAME_CHAT_FORMAT {
        @Override
        public Setting<?> getSetting() {

            String defaultFormat = String.format("%s %s &r: %s", FKPlaceholder.TEAM_NAME.get(),
                    PlaceholderEnum.PLAYER_NAME.get(), PlaceholderEnum.MESSAGE.get());

            return new SimpleConfigSetting
                    .Builder<>("gameChatFormat", defaultFormat, "game-chat.format", String.class)
                    .withFilter(value -> value != null && !value.isEmpty())
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

    ALLOW_SPECTATOR_CHAT {
        @Override
        public Setting<?> getSetting() {
            return new SimpleConfigSetting
                    .Builder<>("allowSpectatorChat", true, "spectator-chat.enabled", Boolean.class)
                    .build();
        }
    },

    SPECTATOR_CHAT_FORMAT {
        @Override
        public Setting<?> getSetting() {

            String defaultFormat = String.format("&7%s &r: %s", PlaceholderEnum.PLAYER_NAME.get(), PlaceholderEnum.MESSAGE.get());

            return new SimpleConfigSetting
                    .Builder<>("spectatorChatFormat", defaultFormat, "spectator-chat.format", String.class)
                    .withFilter(value -> value != null && !value.isEmpty())
                    .build();
        }
    },

    GAME_SPAWN {
        @Override
        public Setting<?> getSetting() {
            return new LocationSetting
                    .Builder("gameSpawn", new Location(Bukkit.getWorld("world"), 0, 60, 0), "game-spawn")
                    .withFilter(location -> location.getWorld() != null)
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
