package com.github.syr0ws.fallenkingdom.game.model.modes;

import com.github.syr0ws.fallenkingdom.game.model.chat.Chat;

public interface Mode {

    void set();

    void remove();

    Chat getChat();

    ModeType getType();
}
