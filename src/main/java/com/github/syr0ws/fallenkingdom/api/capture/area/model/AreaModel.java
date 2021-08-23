package com.github.syr0ws.fallenkingdom.api.capture.area.model;

import com.github.syr0ws.fallenkingdom.api.capture.Capturable;
import com.github.syr0ws.universe.sdk.tools.Cuboid;

public interface AreaModel extends Capturable {

    Cuboid getArea();
}
