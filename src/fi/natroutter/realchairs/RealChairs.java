package fi.natroutter.realchairs;

import fi.natroutter.realchairs.handlers.ChairHandler;
import fi.natroutter.natlibs.handlers.database.YamlDatabase;
import lombok.Getter;
import fi.natroutter.realchairs.commands.MainCommand;
import fi.natroutter.realchairs.handlers.SittingHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class RealChairs extends JavaPlugin {

    @Getter private static JavaPlugin instance;
    @Getter private static YamlDatabase database;
    @Getter private static ChairHandler chairHandler;

    @Override
    public void onEnable() {
        instance = this;

        database = new YamlDatabase(this);
        chairHandler = new ChairHandler();

        CommandMap map = Bukkit.getCommandMap();
        PluginManager pm = Bukkit.getPluginManager();

        map.register("RealChairs", new MainCommand(this));

        pm.registerEvents(new SittingHandler(), this);

        chairHandler.load();
    }

    @Override
    public void onDisable() {
        if (chairHandler == null) {return;}
        chairHandler.unload();
    }
}
