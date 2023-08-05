package fi.natroutter.realchairs.files;

import fi.natroutter.realchairs.RealChairs;
import fi.natroutter.natlibs.config.ILang;
import fi.natroutter.natlibs.config.Language;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@AllArgsConstructor
public enum Lang implements ILang {

    PREFIX("Prefix"),
    ONLY_IN_GAME("OnlyInGame"),
    NO_PERM("NoPerm"),
    INVALID_ARGS("InvalidArgs"),
    TOO_MANY_ARGS("TooManyArgs"),
    INVALID_TARGET("InvalidTarget"),
    STAND_UP("StandUp"),
    SITTING("Sitting"),
    CHAIR_ADDED("ChairAdded"),
    CHAIR_REMOVED("ChairRemoved"),
    NO_CHAIRS("NoChairs"),
    RELOADED("ConfigsReloaded"),

    CFM_HEADER("cfm.Header"),
    CFM_FOOTER("cfm.Footer"),
    CFM_ENTRY("cfm.Entry"),
    CFM_TELEPORT_BTN("cfm.Teleport_btn"),
    CFM_TELEPORT_HINT("cfm.Teleport_hint"),

    HELP_MESSAGE("HelpMessage"),

    ;

    @Getter
    private String path;

    @Override
    public Language lang() {
        return Language.getFromKey(Config.LANGUAGE);
    }

    @Override
    public ILang prefix() {
        return PREFIX;
    }

    @Override
    public JavaPlugin getPlugin() {
        return RealChairs.getInstance();
    }
}
