package fi.natroutter.realchairs.Utilities;

import fi.natroutter.natlibs.config.IConfig;
import fi.natroutter.realchairs.files.Config;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Sounds {

    @AllArgsConstructor
    public enum type {
        SIT(
                Config.SND_SIT_ENABLED,
                Config.SND_SIT_SOUND,
                Config.SND_SIT_CATEGORY,
                Config.SND_SIT_PITCH,
                Config.SND_SIT_VOLUME
        ),
        STAND(
                Config.SND_STAND_ENABLED,
                Config.SND_STAND_SOUND,
                Config.SND_STAND_CATEGORY,
                Config.SND_STAND_PITCH,
                Config.SND_STAND_VOLUME
        ),
        ADD(
                Config.SND_ADD_CHAIR_ENABLED,
                Config.SND_ADD_CHAIR_SOUND,
                Config.SND_ADD_CHAIR_CATEGORY,
                Config.SND_ADD_CHAIR_PITCH,
                Config.SND_ADD_CHAIR_VOLUME
        ),
        REMOVE(
                Config.SND_REMOVE_CHAIR_ENABLED,
                Config.SND_REMOVE_CHAIR_SOUND,
                Config.SND_REMOVE_CHAIR_CATEGORY,
                Config.SND_REMOVE_CHAIR_PITCH,
                Config.SND_REMOVE_CHAIR_VOLUME
        ),
        ERROR(
                Config.SND_ERROR_ENABLED,
                Config.SND_ERROR_SOUND,
                Config.SND_ERROR_CATEGORY,
                Config.SND_ERROR_PITCH,
                Config.SND_ERROR_VOLUME
        ),

        ;
        @Getter private IConfig enabled;
        @Getter private IConfig sound;
        @Getter private IConfig category;
        @Getter private IConfig pitch;
        @Getter private IConfig volume;
    }

    public static void play(CommandSender sender, type type) {
        if (!(sender instanceof Player p)) {return;}
        if (!type.getEnabled().asBoolean()) {return;}
        p.playSound(p.getPlayer().getLocation(),
                type.getSound().asString(),
                type.getCategory().asSoundCategory(),
                type.getVolume().asFloat(),
                type.getPitch().asFloat()
        );
    }

}
