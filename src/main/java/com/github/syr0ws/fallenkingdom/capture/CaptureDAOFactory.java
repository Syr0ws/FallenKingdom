package com.github.syr0ws.fallenkingdom.capture;

import com.github.syr0ws.fallenkingdom.capture.area.dao.AreaDAO;
import com.github.syr0ws.fallenkingdom.capture.nexus.dao.NexusDAO;

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
