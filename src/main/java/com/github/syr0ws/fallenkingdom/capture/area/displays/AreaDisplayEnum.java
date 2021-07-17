package com.github.syr0ws.fallenkingdom.capture.area.displays;

public enum AreaDisplayEnum {

    CAPTURE_START_SELF("on-base-capture-start.self"),
    CAPTURE_START_CAPTURED_TEAM("on-base-capture-start.captured-team"),
    CAPTURE_STOP_SELF("on-base-capture-stop.self"),
    CAPTURE_STOP_CAPTURED_TEAM("on-base-capture-stop.captured-team"),
    BASE_CAPTURED_SELF("on-base-captured.self"),
    BASE_CAPTURED_OTHER("on-base-captured.other"),
    BASE_CAPTURED_CAPTURED_TEAM("on-base-captured.captured-team");

    private final String path;

    AreaDisplayEnum(String path) {
        this.path = path;
    }

    public String getPath() {
        return this.path;
    }
}
