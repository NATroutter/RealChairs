package fi.natroutter.realchairs.handlers.gui;

import fi.natroutter.natlibs.handlers.guibuilder.Button;
import fi.natroutter.natlibs.utilities.Theme;
import fi.natroutter.natlibs.utilities.Utilities;
import fi.natroutter.realchairs.RealChairs;
import fi.natroutter.realchairs.Utilities.Utils;
import fi.natroutter.realchairs.files.Lang;
import fi.natroutter.realchairs.handlers.ChairHandler;
import fi.natroutter.realchairs.handlers.ColorTable;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.util.List;

public class Buttons {

    private static final ChairHandler chairHandler = RealChairs.getChairHandler();

    public static Button Chair(Block block, double height, BlockFace face, Location location) {
        Button btn = new Button(block.getType(), (e, gui) -> {
            if (e.getClickType().isLeftClick()) {
                Location loc = location.clone().add(0.5, 1, 0.5);
                loc.setPitch(90);
                e.getPlayer().teleport(loc);
                Utils.drawBlock(e.getPlayer(), block.getLocation(), ColorTable.DISPLAY);
            } else if (e.getClickType().isRightClick()) {
                gui.switchGUI(e.getPlayer(), RealChairs.getConfirmGUI(), List.of(block));
            }
        });
        btn.name(Lang.GUI_CHAIR_NAME);
        btn.lore(Lang.GUI_CHAIR_LORE.asComponentList(
                Placeholder.parsed("block", Utilities.toTitleCase(block.getType().name())),
                Placeholder.parsed("height", String.valueOf(height)),
                Placeholder.parsed("face", Utilities.toTitleCase(face.name())),
                Placeholder.parsed("world", Utilities.toTitleCase(location.getWorld().getName())),
                Placeholder.parsed("x", String.valueOf(location.getBlockX())),
                Placeholder.parsed("y", String.valueOf(location.getBlockY())),
                Placeholder.parsed("z", String.valueOf(location.getBlockZ()))
        ));
        return btn;
    }

    public static Button Yes(Block block) {
        Button btn = new Button(Material.GREEN_CONCRETE, (e, gui) -> {
            chairHandler.removeChair(e.getPlayer(), block);
            gui.switchGUI(e.getPlayer(), RealChairs.getMainGUI());
        });
        btn.name(Lang.GUI_YES_NAME);
        btn.lore(Lang.GUI_YES_LORE);
        return btn;
    }

    public static Button No() {
        Button btn = new Button(Material.RED_CONCRETE, (e, gui) -> gui.switchGUI(e.getPlayer(), RealChairs.getMainGUI()));
        btn.name(Lang.GUI_NO_NAME);
        btn.lore(Lang.GUI_NO_LORE);
        return btn;
    }

    public static Button next() {
        Button btn = new Button(Material.ARROW, (e, gui) -> gui.nextPage());
        btn.name(Lang.GUI_NEXT_NAME);
        btn.lore(Lang.GUI_NEXT_LORE);
        return btn;
    }

    public static Button close() {
        Button btn = new Button(Material.BARRIER, (e, gui) -> gui.close(e.getPlayer()));
        btn.name(Lang.GUI_CLOSE_NAME);
        btn.lore(Lang.GUI_CLOSE_LORE);
        return btn;
    }

    public static Button previous() {
        Button btn = new Button(Material.ARROW, (e, gui) -> gui.previousPage());
        btn.name(Lang.GUI_BACK_NAME);
        btn.lore(Lang.GUI_BACK_LORE);
        return btn;
    }

}
