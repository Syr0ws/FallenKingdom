package com.github.syr0ws.fallenkingdom.game.model.teams.dao;

import com.github.syr0ws.fallenkingdom.game.model.teams.Team;
import com.github.syr0ws.fallenkingdom.game.model.teams.TeamBase;
import com.github.syr0ws.fallenkingdom.game.model.teams.TeamColor;
import com.github.syr0ws.fallenkingdom.game.model.teams.TeamException;
import com.github.syr0ws.fallenkingdom.tools.Cuboid;
import com.github.syr0ws.fallenkingdom.utils.LocationUtils;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class ConfigTeamDAO implements TeamDAO {

    private static final String TEAM_FILE_NAME = "teams.yml";

    private final Plugin plugin;

    public ConfigTeamDAO(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Collection<Team> loadTeams() throws TeamException {

        this.createTeamFile();

        Path path = this.getTeamFile();

        YamlConfiguration config = YamlConfiguration.loadConfiguration(path.toFile());

        // Checking that the 'teams' section exists and is valid.
        if(!config.isSet("teams") || !config.isConfigurationSection("teams"))
            throw new TeamException("Section 'teams' not found or invalid.");

        ConfigurationSection section = config.getConfigurationSection("teams");

        List<Team> teams = new ArrayList<>();

        // Loading teams.
        for(String key : section.getKeys(false)) {

            if(!section.isConfigurationSection(key)) {

                // If we only throw an exception, the loop will be stopped.
                // This try catch allows us to continue the loop iteration even if a key is invalid.
                try { throw new TeamException(String.format("Key '%s' is not a section.", key));
                } catch (TeamException e) { e.printStackTrace(); }
            }

            ConfigurationSection teamSection = section.getConfigurationSection(key);

            try {

                // Loading the current team and adding it to the list.
                Team team = this.loadTeam(teamSection);
                teams.add(team);

            } catch (TeamException e) { e.printStackTrace(); }
        }
        return teams;
    }

    private Team loadTeam(ConfigurationSection section) throws TeamException {

        String name = section.getName();
        String displayName = section.getString("display-name");

        TeamColor color = this.loadTeamColor(section);
        TeamBase base = this.loadTeamBase(section);

        return new Team(name, displayName, base, color);
    }

    private TeamBase loadTeamBase(ConfigurationSection section) throws TeamException {

        ConfigurationSection baseSection = section.getConfigurationSection("base");
        ConfigurationSection vaultSection = section.getConfigurationSection("vault");
        ConfigurationSection spawnSection = section.getConfigurationSection("spawn");

        if(baseSection == null)
            throw new TeamException("Section 'base' not found or invalid.");

        if(vaultSection == null)
            throw new TeamException("Section 'vault' not found or invalid.");

        if(spawnSection == null)
            throw new TeamException("Section 'spawn' not found or invalid.");

        Cuboid base = new Cuboid(section.getConfigurationSection("base"));
        Cuboid vault = new Cuboid(section.getConfigurationSection("vault"));

        Location spawn = LocationUtils.getLocation(section.getConfigurationSection("spawn"));

        return new TeamBase(base, vault, spawn);
    }

    private TeamColor loadTeamColor(ConfigurationSection section) throws TeamException {

        String name = section.getString("color");

        Optional<TeamColor> optional = TeamColor.getByName(name);

        if(!optional.isPresent())
            throw new TeamException(String.format("No team color found in '%s'.", section.getName()));

        return optional.get();
    }

    private void createTeamFile() throws TeamException {

        Path path = this.getTeamFile();

        if(Files.exists(path)) return;

        try { this.plugin.saveResource(TEAM_FILE_NAME, false);
        } catch (IllegalArgumentException e) { throw new TeamException(String.format("Cannot create file '%s'.", TEAM_FILE_NAME), e); }
    }

    private Path getTeamFile() {
        return Paths.get(this.plugin.getDataFolder() + "/" + TEAM_FILE_NAME);
    }
}
