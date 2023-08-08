package fi.natroutter.realchairs.Utilities;

import fi.natroutter.natlibs.objects.BaseItem;
import fi.natroutter.realchairs.files.Lang;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;

public class Items {

    public static BaseItem chairTool() {
        BaseItem item = new BaseItem(Material.FEATHER);
        item.setGlow(true);
        item.addItemFlags(ItemFlag.values());
        item.name(Lang.TOOL_NAME);
        item.lore(Lang.TOOL_LORE);
        return item;
    }

}
