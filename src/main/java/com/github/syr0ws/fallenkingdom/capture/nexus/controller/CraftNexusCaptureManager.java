package com.github.syr0ws.fallenkingdom.capture.nexus.controller;

import com.github.syr0ws.fallenkingdom.capture.CaptureManager;
import com.github.syr0ws.fallenkingdom.capture.CaptureType;

public class CraftNexusCaptureManager implements CaptureManager {

    @Override
    public CaptureType getCaptureType() {
        return CaptureType.NEXUS;
    }
}
