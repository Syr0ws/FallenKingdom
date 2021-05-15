package com.github.syr0ws.fallenkingdom.timer.impl;

import com.github.syr0ws.fallenkingdom.displays.Display;
import com.github.syr0ws.fallenkingdom.timer.TimerAction;

public class DisplayAction implements TimerAction {

    private final Display display;

    public DisplayAction(Display display) {
        this.display = display;
    }

    @Override
    public void execute() {
        this.display.displayAll();
    }
}
