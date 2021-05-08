package com.github.syr0ws.fallenkingdom.game.model;

import com.github.syr0ws.fallenkingdom.attributes.Attribute;
import com.github.syr0ws.fallenkingdom.attributes.AttributeObservable;
import com.github.syr0ws.fallenkingdom.attributes.AttributeObserver;
import com.github.syr0ws.fallenkingdom.game.cycle.GameCycle;
import com.github.syr0ws.fallenkingdom.game.cycle.GameCycleAttribute;
import com.github.syr0ws.fallenkingdom.game.cycle.GameCycleFactory;
import com.github.syr0ws.fallenkingdom.teams.Team;
import com.github.syr0ws.fallenkingdom.tools.Location;

import java.util.*;

public class Game implements GameModel, AttributeObserver, AttributeObservable {

    private boolean pvp, assaults;

    private GameState state;
    private GameCycle cycle;

    private final Location spawn;
    private final List<Team> teams;
    private final List<AttributeObserver> observers;

    public Game(Location spawn, List<Team> teams) {
        this.spawn = spawn;
        this.teams = teams;
        this.observers = new ArrayList<>();
        this.setState(GameState.WAITING);
    }

    public void setState(GameState state) {
        this.cycle.stop();
        this.state = state;
        this.cycle = GameCycleFactory.getCycle(state);
        this.cycle.start(); // Starting the new cycle.
    }

    @Override
    public GameCycle getCycle() {
        return this.cycle;
    }

    @Override
    public GameState getState() {
        return this.state;
    }

    @Override
    public Location getSpawn() {
        return this.spawn;
    }

    @Override
    public boolean isPvPEnabled() {
        return this.pvp;
    }

    @Override
    public boolean areAssaultsEnabled() {
        return this.assaults;
    }

    @Override
    public List<Team> getTeams() {
        return Collections.unmodifiableList(this.teams);
    }

    @Override
    public void setPvPEnabled(boolean enabled) {
        this.pvp = enabled;
    }

    @Override
    public void setAssaultsEnabled(boolean enabled) {
        this.assaults = enabled;
    }

    @Override
    public void onUpdate(Attribute attribute) {

        Optional<GameState> optional = this.state.getNext();
        optional.ifPresent(this::setState);
    }

    @Override
    public Collection<Attribute> observed() {
        return Collections.singleton(GameCycleAttribute.FINISH_STATE);
    }

    @Override
    public void notifyChange(Attribute attribute) {
        this.observers.stream()
                .filter(observer -> observer.observed().contains(attribute))
                .forEach(observer -> observer.onUpdate(attribute));
    }

    @Override
    public void addObserver(AttributeObserver observer) {
        this.observers.add(observer);
    }

    @Override
    public void removeObserver(AttributeObserver observer) {
        this.observers.remove(observer);
    }

    @Override
    public Collection<AttributeObserver> getObservers() {
        return Collections.unmodifiableList(this.observers);
    }
}
