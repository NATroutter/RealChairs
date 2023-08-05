package fi.natroutter.realchairs.handlers;

import fi.natroutter.realchairs.RealChairs;
import fi.natroutter.realchairs.files.Config;
import fi.natroutter.realchairs.files.Lang;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.spigotmc.event.entity.EntityDismountEvent;

import java.util.*;

public class SittingHandler implements Listener {

    private final ChairHandler chairHandler = RealChairs.getChairHandler();

    @EventHandler
    public void onInteractBlock(PlayerInteractEvent e) {
        if (!e.hasBlock() || e.getClickedBlock() == null) { return; }
        if (!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {return;}
        if (e.getHand() != EquipmentSlot.HAND) return;

        Block block = e.getClickedBlock();
        Player p = e.getPlayer();

        if (!chairHandler.isChair(block)) {return;}
        if (p.isInsideVehicle()) {return;}
        if (p.isSneaking()) {return;}

        if (p.hasPermission("realchairs.sit")) {
            Location loc = block.getLocation();
            loc.setX(loc.getBlockX() + 0.5);
            loc.setZ(loc.getBlockZ() + 0.5);
            Collection<Entity> ents = p.getWorld().getNearbyEntities(loc.add(0, -1, 0), 0.2, 0.2, 0.2);
            for (Entity ent : ents) {
                if (chairHandler.isChair(ent)) return;
            }
            chairHandler.sit(p, block);
        } else {
            if (Config.USE_MESSAGES.asBoolean()) {
                p.sendMessage(Lang.NO_PERM.prefixed());
            }
        }

    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        if (p.isInsideVehicle() && chairHandler.isChair(p.getVehicle())) {
            e.setCancelled(true);
            return;
        }
        if (!chairHandler.isChair(e.getBlock())) return;

        Location loc = e.getBlock().getLocation();
        loc.setX(loc.getBlockX() + 0.5);
        loc.setZ(loc.getBlockZ() + 0.5);
        Collection<Entity> ents = p.getWorld().getNearbyEntities(loc.add(0, -1, 0), 0.2, 0.2, 0.2);
        for (Entity ent : ents) {
            if (!(ent instanceof ArmorStand chair)) continue;
            if (!chairHandler.isChair(chair)) continue;

            for (Entity pas : chair.getPassengers()) {
                pas.eject();
            }
            Bukkit.getScheduler().scheduleSyncDelayedTask(RealChairs.getInstance(), ()->{
                chair.remove();
                chairHandler.removeChair(e.getBlock());
            }, 3);
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        Player p = e.getPlayer();
        if (p.isInsideVehicle() && chairHandler.isChair(p.getVehicle())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onStandup(EntityDismountEvent e) {
        if (e.getEntity() instanceof Player p) {
            if (!chairHandler.isChair(e.getDismounted())) {return;}
            ArmorStand chair = (ArmorStand)e.getDismounted();

            if (Config.USE_MESSAGES.asBoolean()) {
                p.sendMessage(Lang.STAND_UP.prefixed());
            }
            chair.remove();
            Bukkit.getScheduler().scheduleSyncDelayedTask(RealChairs.getInstance(), ()->{
                p.teleport(p.getLocation().add(0, 2, 0));
            }, 3);
        }
    }

}
