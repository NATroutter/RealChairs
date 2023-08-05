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
    ALREADY_CHAIR("AlreadyChair"),
    BLOCK_NOT_CHAIR("BlockNotChair"),
    CHAIR_DISPLAY("ChairDisplay"),
    TOOL_ADDED_INV("ToolAddedToInventory"),
    CHAIR_HEIGHT_SET("ChairDefaultHeightSet"),
    INVALID_REMOVED("InvalidChairsRemoved"),
    NO_INVALID("NoInvalidChairs"),
    ALL_CHAIRS_WIPED("AllChairsWiped"),
    CHAIRS_WIPE_CANCELED("ChairsWipedCancelled"),
    INVALID_CONFIRM_ID("InvalidConfirmationID"),
    NO_PENDING_CONFIRM("NoPendingConfirmations"),

    CONFIRM_ACCEPT_BTN("ConfirmMessage.Accpet_button"),
    CONFIRM_ACCEPT_HINT("ConfirmMessage.Accept_hint"),
    CONFIRM_DENY_BTN("ConfirmMessage.Deny_button"),
    CONFIRM_DENY_HINT("ConfirmMessage.Deny_hint"),
    CONFIRM_MESSAGE("ConfirmMessage.Message"),

    TOGGLE_ON("ToggleStates.Enabled"),
    TOGGLE_OFF("ToggleStates.Disabled"),

    TOOL_NAME("Tool.Name"),
    TOOL_LORE("Tool.Lore"),

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
