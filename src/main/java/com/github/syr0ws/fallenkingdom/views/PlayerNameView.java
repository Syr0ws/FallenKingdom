package com.github.syr0ws.fallenkingdom.views;

import com.github.syr0ws.fallenkingdom.game.model.teams.FKTeam;
import com.github.syr0ws.fallenkingdom.game.model.teams.FKTeamPlayer;
import com.github.syr0ws.universe.sdk.modules.view.views.NameView;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

public class PlayerNameView extends NameView {

    public static final String ID = "PlayerNameView";

    private final FKTeamPlayer teamPlayer;
    private Team team;

    public PlayerNameView(FKTeamPlayer teamPlayer) {

        if(teamPlayer == null)
            throw new IllegalArgumentException("FKTeamPlayer cannot be null.");

        this.teamPlayer = teamPlayer;
    }

    @Override
    public void set() {

        FKTeam team = this.teamPlayer.getTeam();

        Player player = this.teamPlayer.getPlayer();

        this.team = player.getScoreboard().registerNewTeam(player.getName());
        this.team.setPrefix(team.getDisplayName() + " ");
        this.team.addEntry(player.getName());
    }

    @Override
    public void update() {

    }

    @Override
    public void remove() {
        this.team.removeEntry(this.teamPlayer.getName());
        this.team.unregister();
        this.team = null;
    }

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public int getPriority() {
        return NORMAL_PRIORITY;
    }

    @Override
    public boolean isUpdatable() {
        return false;
    }
}
