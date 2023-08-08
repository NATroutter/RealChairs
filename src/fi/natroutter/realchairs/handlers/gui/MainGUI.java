package fi.natroutter.realchairs.handlers.gui;

import fi.natroutter.natlibs.handlers.guibuilder.*;
import fi.natroutter.realchairs.RealChairs;
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
    }

    @Override
    protected boolean onShow(Player player, GUI gui, List<Object> args) {

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
}
