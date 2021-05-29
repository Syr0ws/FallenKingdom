package com.github.syr0ws.fallenkingdom.displays.dao;

import com.github.syr0ws.fallenkingdom.displays.Display;
import com.github.syr0ws.fallenkingdom.displays.DisplayException;

import java.util.Collection;

public interface DisplayDAO {

    Display getDisplay(String path) throws DisplayException;

    Collection<Display> getDisplays(String path) throws DisplayException;
}
