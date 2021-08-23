package com.github.syr0ws.fallenkingdom.plugin.capture;

import com.github.syr0ws.fallenkingdom.api.capture.CapturableDAO;
import com.github.syr0ws.fallenkingdom.api.capture.CaptureType;
import com.github.syr0ws.fallenkingdom.plugin.capture.area.dao.AreaDAO;
import com.github.syr0ws.fallenkingdom.plugin.capture.nexus.dao.NexusDAO;

public class CaptureDAOFactory {

    public static CapturableDAO getDAO(CaptureType type) {

        switch (type) {
            case AREA:
                return new AreaDAO();
            case NEXUS:
                return new NexusDAO();
            default:
                throw new IllegalArgumentException(String.format("No capture dao found for type '%s'.", type.name()));
        }
    }
}
