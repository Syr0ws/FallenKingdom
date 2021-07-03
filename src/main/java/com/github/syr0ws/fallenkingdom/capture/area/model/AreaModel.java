package com.github.syr0ws.fallenkingdom.capture.area.model;

import com.github.syr0ws.fallenkingdom.capture.Capturable;
import com.github.syr0ws.universe.tools.Cuboid;

public interface AreaModel extends Capturable {

    Cuboid getArea();
}
