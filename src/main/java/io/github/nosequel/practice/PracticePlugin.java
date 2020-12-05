package io.github.nosequel.practice;

import io.github.nosequel.katakuna.MenuHandler;
import io.github.nosequel.practice.arena.ArenaHandler;
import io.github.nosequel.practice.arena.command.ArenaCommand;
import io.github.nosequel.practice.arena.command.ArenaTemplateCommand;
import io.github.nosequel.practice.arena.schematic.ArenaSchematicImpl;
import io.github.nosequel.practice.handler.HandlerManager;
import io.github.nosequel.practice.kit.KitHandler;
import io.github.nosequel.practice.kit.command.KitCommand;
import io.github.nosequel.practice.listener.EnderpearlListener;
import io.github.nosequel.practice.listener.MatchListener;
import io.github.nosequel.practice.listener.PlayerListener;
import io.github.nosequel.practice.listener.QueueListener;
import io.github.nosequel.practice.match.MatchHandler;
import io.github.nosequel.practice.match.command.MatchHistoryCommand;
import io.github.nosequel.practice.player.ProfileHandler;
import io.github.nosequel.practice.queue.QueueHandler;
import io.github.nosequel.practice.scoreboard.ScoreboardImpl;
import io.github.nosequel.practice.util.command.CommandController;
import io.github.nosequel.practice.util.loadout.LoadoutHandler;
import io.github.nosequel.schematic.SchematicController;
import io.github.nosequel.schematic.saving.impl.BasicSavingType;
import io.github.thatkawaiisam.assemble.Assemble;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

@Getter
public class PracticePlugin extends JavaPlugin {

    @Getter
    private static PracticePlugin plugin;

    private final HandlerManager handler = new HandlerManager(
            new ArenaHandler(),
            new KitHandler(),
            new QueueHandler(),
            new MatchHandler(),
            new ProfileHandler(),
            new QueueHandler()
    );

    private final SchematicController schematicController = new SchematicController(new BasicSavingType("schematics"), ArenaSchematicImpl.class);

    @Override
    public void onEnable() {
        plugin = JavaPlugin.getPlugin(PracticePlugin.class);
        this.handler.load();

        // setup command controller
        this.handler.register(new CommandController("practice").registerCommand(new ArenaCommand(), new ArenaTemplateCommand(), new KitCommand(), new MatchHistoryCommand()));

        // setup loadout handler
        this.handler.register(new LoadoutHandler());

        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        Bukkit.getPluginManager().registerEvents(new MatchListener(), this);
        Bukkit.getPluginManager().registerEvents(new QueueListener(), this);
        Bukkit.getPluginManager().registerEvents(new EnderpearlListener(), this);

        // setup scoreboard
        new Assemble(this, new ScoreboardImpl()).setTicks(2);

        // setup menu api
        new MenuHandler(this);

        // kick all players
        Bukkit.getOnlinePlayers().forEach(player -> player.kickPlayer("The server has reloaded"));

        // unload all loaded chunks
        Bukkit.getWorlds().forEach(world -> Arrays.stream(world.getLoadedChunks()).forEach(Chunk::unload));
    }

    @Override
    public void onDisable() {
        this.handler.unload();
        this.schematicController.save();
    }

}
