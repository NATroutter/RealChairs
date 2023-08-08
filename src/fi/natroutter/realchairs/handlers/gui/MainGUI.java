package fi.natroutter.realchairs.handlers.gui;

import fi.natroutter.natlibs.handlers.guibuilder.*;
import fi.natroutter.realchairs.RealChairs;
import fi.natroutter.realchairs.Utilities.Sounds;
import fi.natroutter.realchairs.files.Config;
import fi.natroutter.realchairs.files.Lang;
import fi.natroutter.realchairs.handlers.Chair;
import fi.natroutter.realchairs.handlers.ChairHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class MainGUI extends GUIFrame {

    public static final ChairHandler chairHandler = RealChairs.getChairHandler();

    public MainGUI() {
        super(Lang.GUI_TITLE, Rows.row6);

        if (Config.SND_OPEN_ENABLED.asBoolean()) {
            this.setOpenSound(new SoundSettings(
                    Config.SND_OPEN_SOUND.asString(),
                    Config.SND_OPEN_CATEGORY.asSoundCategory(),
                    Config.SND_OPEN_VOLUME.asFloat(),
                    Config.SND_OPEN_PITCH.asFloat()
            ));
        } else {
            this.setOpenSound(null);
        }
        if (Config.SND_CLOSE_ENABLED.asBoolean()) {
            this.setCloseSound(new SoundSettings(
                    Config.SND_CLOSE_SOUND.asString(),
                    Config.SND_CLOSE_CATEGORY.asSoundCategory(),
                    Config.SND_CLOSE_VOLUME.asFloat(),
                    Config.SND_CLOSE_PITCH.asFloat()
            ));
        } else {
            this.setCloseSound(null);
        }
        if (Config.SND_CLICK_ENABLED.asBoolean()) {
            this.setClickSound(new SoundSettings(
                    Config.SND_CLICK_SOUND.asString(),
                    Config.SND_CLICK_CATEGORY.asSoundCategory(),
                    Config.SND_CLICK_VOLUME.asFloat(),
                    Config.SND_CLICK_PITCH.asFloat()
            ));
        } else {
            this.setClickSound(null);
        }
    }

    @Override
    protected boolean onShow(Player player, GUI gui, List<Object> args) {
        if (chairHandler.getChairs().size() > 0) {
            List<Button> buttons = chairHandler.getChairs().values().stream()
                    .filter(c -> !c.getLocation().getBlock().getType().isEmpty() && !c.getLocation().getBlock().getType().isAir())
                    .map(c ->
                            Buttons.Chair(c.getLocation().getBlock(), c.getHeight(), c.getDirection(), c.getLocation())
                    ).toList();

            gui.paginateButtons(buttons);
            gui.setButton(Buttons.close(), Rows.row6, 4);
            gui.addNavigator(
                    new Navigator(Buttons.previous(), Rows.row6, 3),
                    new Navigator(Buttons.next(), Rows.row6, 5)
            );

            gui.setRawTitle(Lang.GUI_TITLE.asString());
            return true;
        }
        player.sendMessage(Lang.NO_CHAIRS.prefixed());
        Sounds.play(player, Sounds.type.ERROR);
        return false;
    }
}
