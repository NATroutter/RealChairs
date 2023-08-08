package fi.natroutter.realchairs.files;

import fi.natroutter.realchairs.RealChairs;
import fi.natroutter.natlibs.config.IConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@AllArgsConstructor
public enum Config implements IConfig {


    LANGUAGE("General.Language"),
    USE_MESSAGES("General.UseMessages"),
    REQUIRE_EMPTY_HAND("General.RequireEmptyHand"),

    SND_SIT_ENABLED("Sounds.Sit.Enabled"),
    SND_SIT_SOUND("Sounds.Sit.Sound"),
    SND_SIT_CATEGORY("Sounds.Sit.Category"),
    SND_SIT_PITCH("Sounds.Sit.Pitch"),
    SND_SIT_VOLUME("Sounds.Sit.Volume"),

    SND_STAND_ENABLED("Sounds.Stand.Enabled"),
    SND_STAND_SOUND("Sounds.Stand.Sound"),
    SND_STAND_CATEGORY("Sounds.Stand.Category"),
    SND_STAND_PITCH("Sounds.Stand.Pitch"),
    SND_STAND_VOLUME("Sounds.Stand.Volume"),

    SND_REMOVE_CHAIR_ENABLED("Sounds.RemoveChair.Enabled"),
    SND_REMOVE_CHAIR_SOUND("Sounds.RemoveChair.Sound"),
    SND_REMOVE_CHAIR_CATEGORY("RemoveChair.Stand.Category"),
    SND_REMOVE_CHAIR_PITCH("Sounds.RemoveChair.Pitch"),
    SND_REMOVE_CHAIR_VOLUME("Sounds.RemoveChair.Volume"),

    SND_ADD_CHAIR_ENABLED("Sounds.AddChair.Enabled"),
    SND_ADD_CHAIR_SOUND("Sounds.AddChair.Sound"),
    SND_ADD_CHAIR_CATEGORY("Sounds.AddChair.Category"),
    SND_ADD_CHAIR_PITCH("Sounds.AddChair.Pitch"),
    SND_ADD_CHAIR_VOLUME("Sounds.AddChair.Volume"),

    SND_ERROR_ENABLED("Sounds.Error.Enabled"),
    SND_ERROR_SOUND("Sounds.Error.Sound"),
    SND_ERROR_CATEGORY("Sounds.Error.Category"),
    SND_ERROR_PITCH("Sounds.Error.Pitch"),
    SND_ERROR_VOLUME("Sounds.Error.Volume"),

    SND_OPEN_ENABLED("Sounds.Open.Enabled"),
    SND_OPEN_SOUND("Sounds.Open.Sound"),
    SND_OPEN_CATEGORY("Sounds.Open.Category"),
    SND_OPEN_PITCH("Sounds.Open.Pitch"),
    SND_OPEN_VOLUME("Sounds.Open.Volume"),

    SND_CLOSE_ENABLED("Sounds.Close.Enabled"),
    SND_CLOSE_SOUND("Sounds.Close.Sound"),
    SND_CLOSE_CATEGORY("Sounds.Close.Category"),
    SND_CLOSE_PITCH("Sounds.Close.Pitch"),
    SND_CLOSE_VOLUME("Sounds.Close.Volume"),

    SND_CLICK_ENABLED("Sounds.Click.Enabled"),
    SND_CLICK_SOUND("Sounds.Click.Sound"),
    SND_CLICK_CATEGORY("Sounds.Click.Category"),
    SND_CLICK_PITCH("Sounds.Click.Pitch"),
    SND_CLICK_VOLUME("Sounds.Click.Volume"),

    EFFECT_SIT_ENABLED("Effects.WhenSit.Enabled"),
    EFFECT_SIT_LIST("Effects.WhenSit.List"),

    EFFECT_STAND_ENABLED("Effects.WhenStand.Enabled"),
    EFFECT_STAND_LIST("Effects.WhenStand.List"),

    PERM_ENABLED("Permissions.Enabled"),
    PERM_USE_CHAIRS("Permissions.UsingChairs"),

    CMD_ALIASES("Commands.Aliases"),

    CMD_HELP_ARG("Commands.Help.Argument"),
    CMD_HELP_PERM("Commands.Help.Permission"),

    CMD_MENU_ARG("Commands.Menu.Argument"),
    CMD_MENU_PERM("Commands.Menu.Permission"),

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
