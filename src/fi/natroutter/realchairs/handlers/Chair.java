package fi.natroutter.realchairs.handlers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;

import java.io.Serializable;

@AllArgsConstructor @Getter @Setter
public class Chair implements Serializable {

    private Location location;
    private BlockFace direction;



}
