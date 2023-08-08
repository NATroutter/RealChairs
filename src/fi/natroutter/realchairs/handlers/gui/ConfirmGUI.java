package fi.natroutter.realchairs.handlers.gui;

import fi.natroutter.natlibs.handlers.guibuilder.GUI;
import fi.natroutter.natlibs.handlers.guibuilder.GUIFrame;
import fi.natroutter.natlibs.handlers.guibuilder.Rows;
import fi.natroutter.realchairs.files.Lang;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.List;

public class ConfirmGUI extends GUIFrame {


    public ConfirmGUI() {
        super(Lang.GUI_CONFIRM_TITLE, Rows.row3);
    }

    @Override
    protected boolean onShow(Player player, GUI gui, List<Object> args) {
        Block block = (Block)args.get(0);

        gui.setButton(Buttons.Yes(block), Rows.row2, 3);
        gui.setButton(Buttons.No(), Rows.row2, 5);

        return true;
    }
}
