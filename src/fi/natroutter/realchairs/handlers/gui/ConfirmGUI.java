package fi.natroutter.realchairs.handlers.gui;

import fi.natroutter.natlibs.handlers.guibuilder.GUI;
import fi.natroutter.natlibs.handlers.guibuilder.GUIFrame;
import fi.natroutter.natlibs.handlers.guibuilder.Rows;
import fi.natroutter.natlibs.handlers.guibuilder.SoundSettings;
import fi.natroutter.realchairs.files.Config;
import fi.natroutter.realchairs.files.Lang;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.List;

public class ConfirmGUI extends GUIFrame {


    public ConfirmGUI() {
        super(Lang.GUI_CONFIRM_TITLE, Rows.row3);

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
        Block block = (Block)args.get(0);

        gui.setButton(Buttons.Yes(block), Rows.row2, 3);
        gui.setButton(Buttons.No(), Rows.row2, 5);

        return true;
    }
}
