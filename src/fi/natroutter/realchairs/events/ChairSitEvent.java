package fi.natroutter.realchairs.events;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ChairSitEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    @Getter private final Player player;
    @Getter private final Block block;
    @Getter private final BlockFace face;
    @Getter private final ArmorStand chair;
    @Getter private final Double height;

    @Getter @Setter
    private Location dismountLocation;
    private boolean isCancelled;

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public ChairSitEvent(Player player, Block block, BlockFace face, ArmorStand chair, Double height, Location dismountLocation) {
        this.player = player;
        this.block = block;
        this.face = face;
        this.chair = chair;
        this.height = height;
        this.dismountLocation = dismountLocation;
        this.isCancelled = false;
    }

    @Override
    public boolean isCancelled() {
        return this.isCancelled;
    }

    @Override
    public void setCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

}
