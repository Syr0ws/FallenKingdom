package com.github.syr0ws.fallenkingdom.game.model.v2.teams.base.capture;

import com.github.syr0ws.universe.tools.Cuboid;

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
}
