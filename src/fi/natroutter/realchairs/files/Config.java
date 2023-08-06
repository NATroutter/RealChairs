package fi.natroutter.realchairs.files;

import fi.natroutter.natlibs.handlers.Particles;
import fi.natroutter.realchairs.RealChairs;
import fi.natroutter.natlibs.config.IConfig;
import jdk.jfr.Enabled;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Ambient;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;
import org.checkerframework.checker.units.qual.Volume;

import javax.swing.*;
import javax.tools.Tool;

@AllArgsConstructor
public enum Config implements IConfig {


    LANGUAGE("General.Language"),
    USE_MESSAGES("General.UseMessages"),
    REQUIRE_EMPTY_HAND("General.RequireEmptyHand"),

    SOUND_ENABLED("Sounds.Enabled"),

    SOUND_SIT("Sounds.Sit.Sound"),
    SOUND_SIT_PITCH("Sounds.Sit.Pitch"),
    SOUND_SIT_VOLUME("Sounds.Sit.Volume"),

    SOUND_STAND("Sounds.Stand.Sound"),
    SOUND_STAND_PITCH("Sounds.Stand.Pitch"),
    SOUND_STAND_VOLUME("Sounds.Stand.Volume"),

    EFFECT_SIT_ENABLED("Effects.WhenSit.Enabled"),
    EFFECT_SIT_LIST("Effects.WhenSit.List"),

    EFFECT_STAND_ENABLED("Effects.WhenStand.Enabled"),
    EFFECT_STAND_LIST("Effects.WhenStand.List"),

    PERM_ENABLED("Permissions.Enabled"),
    PERM_USE_CHAIRS("Permissions.UsingChairs"),

    CMD_ALIASES("Commands.Aliases"),

    CMD_HELP_ARG("Commands.Help.Argument"),
    CMD_HELP_PERM("Commands.Help.Permission"),

    CMD_ADD_ARG("Commands.Add.Argument"),
    CMD_ADD_PERM("Commands.Add.Permission"),

    CMD_REMOVE_ARG("Commands.Remove.Argument"),
    CMD_REMOVE_PERM("Commands.Remove.Permission"),

    CMD_LIST_ARG("Commands.List.Argument"),
    CMD_LIST_PERM("Commands.List.Permission"),

    CMD_RELOAD_ARG("Commands.Reload.Argument"),
    CMD_RELOAD_PERM("Commands.Reload.Permission"),

    CMD_SHOW_ARG("Commands.Show.Argument"),
    CMD_SHOW_PERM("Commands.Show.Permission"),

    CMD_TOOL_ARG("Commands.Tool.Argument"),
    CMD_TOOL_PERM("Commands.Tool.Permission"),

    CMD_HEIGHT_ARG("Commands.Height.Argument"),
    CMD_HEIGHT_PERM("Commands.Height.Permission"),
    CMD_HEIGHT_DISABLE("Commands.Height.Disable"),

    CMD_CLEAN_ARG("Commands.Clean.Argument"),
    CMD_CLEAN_PERM("Commands.Clean.Permission"),

    CMD_WIPE_ARG("Commands.Wipe.Argument"),
    CMD_WIPE_PERM("Commands.Wipe.Permission"),

    CMD_SIT_ARG("Commands.Sit.Argument"),
    CMD_SIT_PERM("Commands.Sit.Permission"),
    CMD_SIT_PERM_SELECT_HEIGHT("Commands.Sit.PermissionSelectHeight"),

    ;

    @Getter
    String path;

    @Override
    public JavaPlugin getPlugin() {
        return RealChairs.getInstance();
    }
}
