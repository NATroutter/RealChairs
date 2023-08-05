package fi.natroutter.realchairs.handlers;

import fi.natroutter.natlibs.handlers.Particles;
import fi.natroutter.natlibs.objects.ParticleSettings;
import fi.natroutter.realchairs.RealChairs;
import fi.natroutter.realchairs.Utilities.Items;
import fi.natroutter.realchairs.Utilities.Utils;
import fi.natroutter.realchairs.files.Config;
import fi.natroutter.realchairs.files.Lang;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.spigotmc.event.entity.EntityDismountEvent;

import java.util.*;

public class ChairListener implements Listener {

    private final ChairHandler chairHandler = RealChairs.getChairHandler();

    @EventHandler
    public void onWandUse(PlayerInteractEvent e) {
        if (!e.hasBlock() || e.getClickedBlock() == null) { return; }
        if (e.getHand() != EquipmentSlot.HAND) return;
        if (!e.hasItem() || e.getItem() == null) return;
        if (!Items.chairTool().isSimilar(e.getItem())) return;
        e.setCancelled(true);

        Player p = e.getPlayer();
        Block block = e.getClickedBlock();
        BlockFace face = e.getBlockFace();
        Action action = e.getAction();

        if (action.equals(Action.LEFT_CLICK_BLOCK)) {
            if (chairHandler.isChair(block)) {
                p.sendMessage(Lang.ALREADY_CHAIR.prefixed());
                return;
            }
            chairHandler.addChair(p, UUID.randomUUID(), block, face);
        } else if (action.equals(Action.RIGHT_CLICK_BLOCK)) {
            if (!chairHandler.isChair(block)) {
                p.sendMessage(Lang.BLOCK_NOT_CHAIR.prefixed());
                return;
            }
            chairHandler.removeChair(p, block);
        }
    }

    @EventHandler
    public void onInteractBlock(PlayerInteractEvent e) {
        if (!e.hasBlock() || e.getClickedBlock() == null) { return; }
        if (!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {return;}
        if (e.getHand() != EquipmentSlot.HAND) return;

        Block block = e.getClickedBlock();
        Player p = e.getPlayer();

        if (e.hasItem() && e.getItem() != null) {
            if (Items.chairTool().isSimilar(e.getItem())) return;
        }

        if (!chairHandler.isChair(block)) {return;}
        if (p.isInsideVehicle()) {return;}
        if (p.isSneaking()) {return;}

        if (p.hasPermission("realchairs.use")) {
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
        double height = chairHandler.getChairHeight(e.getBlock());

        chairHandler.removeChair(p, e.getBlock());

        Location center = e.getBlock().getLocation().clone().add(0.5, 0, 0.5);
        Location chairLoc = center.clone().add(0, height, 0);

        Collection<Entity> ents = p.getWorld().getNearbyEntities(chairLoc, 0.2, 0.2, 0.2);
        for (Entity ent : ents) {
            if (!(ent instanceof ArmorStand chair)) continue;
            if (!chairHandler.isChair(chair)) continue;

            for (Entity pas : chair.getPassengers()) {
                pas.eject();
            }
            Bukkit.getScheduler().scheduleSyncDelayedTask(RealChairs.getInstance(), chair::remove, 3);
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
    public void onQuit(PlayerQuitEvent e) {
        ChairHandler.dismounts.remove(e.getPlayer().getUniqueId());
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
                Location loc = ChairHandler.dismounts.getOrDefault(p.getUniqueId(), p.getLocation().add(0, 2, 0));
                p.teleport(loc);
                ChairHandler.dismounts.remove(p.getUniqueId());
            }, 3);
        }
    }

}
