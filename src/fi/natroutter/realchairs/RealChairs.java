package fi.natroutter.realchairs;

import com.sun.tools.javac.Main;
import fi.natroutter.realchairs.handlers.ChairHandler;
import fi.natroutter.natlibs.handlers.database.YamlDatabase;
import fi.natroutter.realchairs.handlers.gui.ConfirmGUI;
import fi.natroutter.realchairs.handlers.gui.MainGUI;
import lombok.Getter;
import fi.natroutter.realchairs.commands.MainCommand;
import fi.natroutter.realchairs.handlers.ChairListener;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.CommandMap;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
public class RealChairs extends JavaPlugin {

    ConsoleCommandSender console = Bukkit.getConsoleSender();

    @Getter private static JavaPlugin instance;
    @Getter private static YamlDatabase database;
    @Getter private static ChairHandler chairHandler;
    @Getter private static ConfirmGUI confirmGUI;
    @Getter private static MainGUI mainGUI;

    //TODO:
    // - [DONE] Sit potion effects (disable/enable setting)
    // - [DONE] Sit & stand sound (support for custom sounds && disable/enable setting)
    // - [DONE] Custom actionbar message
    // - [DONE] API events (SitEvent, StandEvent post and pre also cancelable)
    // - [DONE] remove health bar when sitting
    // - [DONE] Add subCommands to lanuage file
    // - [] GUI where you can see all chairs and remove,teleport to them (list icons as block in the location!)
    // - [DONE] Add fancy startup message like in Motipistekauppa mix with betterRoulette
    // - [DONE] Add to cleanup command to remove all entities from world

    @Override
    public void onEnable() {
        instance = this;

        database = new YamlDatabase(this);
        chairHandler = new ChairHandler(this);

        confirmGUI = new ConfirmGUI();
        mainGUI = new MainGUI();

        CommandMap map = Bukkit.getCommandMap();
        PluginManager pm = Bukkit.getPluginManager();

        map.register("RealChairs", new MainCommand());

        pm.registerEvents(new ChairListener(), this);


        PluginDescriptionFile pdf = instance.getDescription();
        console.sendMessage("§5─────────────────────────────────────────");
        console.sendMessage("§8┌[ §d§5RealChairs Enabled §8]");
        console.sendMessage("§8├ §7Version§8: §d" + pdf.getVersion());
        console.sendMessage("§8├ §7Developed by§8: §d" + String.join("§7, §d", pdf.getAuthors()));
        console.sendMessage("§8└ §7Website§8: §d" + pdf.getWebsite());
        //console.sendMessage("§7Hooks§8:§7 ");
        console.sendMessage("§5─────────────────────────────────────────");

    }

}
