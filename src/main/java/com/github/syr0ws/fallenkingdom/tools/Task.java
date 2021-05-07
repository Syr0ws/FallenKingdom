package com.github.syr0ws.fallenkingdom.tools;

import org.bukkit.scheduler.BukkitRunnable;

public abstract class Task extends BukkitRunnable {

    private boolean running;

    public void start() {

        if(this.isRunning())
            throw new UnsupportedOperationException("Task already running.");

        this.running = true;
    }

    public void stop() {

        if(!this.isRunning())
            throw new UnsupportedOperationException("Task is not running.");

        this.cancel();
        this.running = false;
    }

    public boolean isRunning() {
        return this.running;
    }
}
