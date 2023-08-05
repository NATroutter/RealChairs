package fi.natroutter.realchairs.Utilities;

import fi.natroutter.natlibs.handlers.Particles;
import fi.natroutter.natlibs.objects.ParticleSettings;
import fi.natroutter.natlibs.utilities.Utilities;
import fi.natroutter.realchairs.handlers.ColorTable;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.UUID;

public class Utils {

    public static void drawBlock(Player p, Location loc, ColorTable color) {
        ParticleSettings settings = new ParticleSettings(Particle.REDSTONE, 1, 0, 0, 0, 0);
        settings.setDustOptions(new Particle.DustOptions(color.getColor(), 1));
        for (Location point : Utilities.getHollowCube(loc, 0.1)) {
            Particles.spawn(p, point, settings);
        }
    }
}
