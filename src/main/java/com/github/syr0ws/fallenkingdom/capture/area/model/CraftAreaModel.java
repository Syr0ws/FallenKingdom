package com.github.syr0ws.fallenkingdom.capture.area.model;

import com.github.syr0ws.fallenkingdom.capture.CaptureType;
import com.github.syr0ws.universe.sdk.tools.Cuboid;

public class CraftAreaModel implements AreaModel {

    private final Cuboid area;

    public CraftAreaModel(Cuboid area) {

        if(area == null)
            throw new IllegalArgumentException("Area cannot be null.");

        this.area = area;
    }

    @Override
    public Cuboid getArea() {
        return this.area;
    }

    @Override
    public CaptureType getCaptureType() {
        return CaptureType.AREA;
    }
}
