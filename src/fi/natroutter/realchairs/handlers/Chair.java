package fi.natroutter.realchairs.handlers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.io.Serializable;

@AllArgsConstructor @Getter @Setter
public class Chair implements Serializable {

    public static double defaultHeight() {return 0.8;}
    public static double defaultHeight(Block block) {
        if (block == null) return defaultHeight();
        if (block.getType().name().endsWith("_SLAB") || block.getType().name().endsWith("_STAIRS")) {
            return 0.3;
        }
        return 0.8;
    }

    private Location location;
    private BlockFace direction;
    private double height;



}
