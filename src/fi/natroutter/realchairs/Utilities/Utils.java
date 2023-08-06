package fi.natroutter.realchairs.Utilities;

import fi.natroutter.natlibs.config.IConfig;
import fi.natroutter.natlibs.handlers.Particles;
import fi.natroutter.natlibs.objects.ParticleSettings;
import fi.natroutter.natlibs.utilities.Utilities;
import fi.natroutter.realchairs.files.Config;
import fi.natroutter.realchairs.handlers.ColorTable;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class Utils {

    public static List<PotionEffect> getEffects(IConfig list) {
        List<PotionEffect> effects = new ArrayList<>();

        ConfigurationSection section = list.yml().getConfigurationSection(list.getPath());
        if (section == null) {return effects;}

        Set<String> keys = section.getKeys(false);
        for (String effect : keys) {
            String path = section.getCurrentPath() +"."+ effect;
            String type = list.yml().getString(path + ".Type");
            int duration = list.yml().getInt(path + ".Duration");
            int amplifier = list.yml().getInt(path + ".Amplifier");
            boolean ambient = list.yml().getBoolean(path + ".Ambient");
            boolean particles = list.yml().getBoolean(path + ".Particles");
            boolean icon = list.yml().getBoolean(path + ".Icon");


            if (type == null){
                Bukkit.getConsoleSender().sendMessage("§5[RealChairs] §dEffect type was null!");
                continue;
            }
            PotionEffectType effectType = PotionEffectType.getByName(type);
            if (effectType == null){
                Bukkit.getConsoleSender().sendMessage("§5[RealChairs] §dInvalid effect type: " + type);
                continue;
            }

            PotionEffect potionEffect = new PotionEffect(effectType, duration, amplifier, ambient, particles, icon);
            effects.add(potionEffect);
        }
        return effects;
    }

    public static void drawBlock(Player p, Location loc, ColorTable color) {
        ParticleSettings settings = new ParticleSettings(Particle.REDSTONE, 1, 0, 0, 0, 0);
        settings.setDustOptions(new Particle.DustOptions(color.getColor(), 1));
        for (Location point : Utilities.getHollowCube(loc, 0.1)) {
            Particles.spawn(p, point, settings);
        }
    }
}
