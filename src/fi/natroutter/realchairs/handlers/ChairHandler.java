package fi.natroutter.realchairs.handlers;

import fi.natroutter.realchairs.files.Config;
import fi.natroutter.realchairs.files.Lang;
import fi.natroutter.natlibs.handlers.database.YamlDatabase;
import fi.natroutter.natlibs.utilities.Utilities;
import fi.natroutter.realchairs.RealChairs;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ChairHandler {

    private NamespacedKey namespacedKey = new NamespacedKey(RealChairs.getInstance(), "RealChairs");

    private final YamlDatabase database = RealChairs.getDatabase();
    private ConcurrentHashMap<UUID,Chair> Chairs = new ConcurrentHashMap<>();

    public void load() {
        wipe();
        Set<String> keys = database.getKeys("Chairs");
        if (keys == null) return;
        for (String key : keys) {
            Chair chair = new Chair(
                    database.getLocation("Chairs", key),
                    BlockFace.valueOf(database.getString("Chairs", key+".direction"))
            );
            Chairs.put(UUID.fromString(key), chair);
        }
    }
    public void unload() {wipe();}

    public void wipe() {
        for (World w : Bukkit.getServer().getWorlds()) {
            for (Entity ent : w.getEntities()) {
                if (ent instanceof ArmorStand chair) {
                    PersistentDataContainer data = chair.getPersistentDataContainer();
                    if (!data.has(namespacedKey, PersistentDataType.INTEGER)) continue;

                    if (!ent.getPassengers().isEmpty()) {
                        for (Entity passenger : ent.getPassengers()) {
                            passenger.leaveVehicle();
                            Bukkit.getScheduler().scheduleSyncDelayedTask(RealChairs.getInstance(), ()->{
                                passenger.teleport(passenger.getLocation().add(0, 1, 0));
                            }, 3);
                        }
                    }

                    ent.remove();
                }
            }
        }
    }

    public boolean isChair(Block block) {
        for (UUID uuid : Chairs.keySet()) {
            Chair chair = Chairs.get(uuid);
            if (Utilities.locationMatch(chair.getLocation(), block.getLocation())) {
                return true;
            }
        }
        return false;
    }

    public boolean isChair(Entity entity) {
        if (entity instanceof ArmorStand chair) {
            PersistentDataContainer data = chair.getPersistentDataContainer();
            return data.has(namespacedKey, PersistentDataType.INTEGER);
        }
        return false;
    }

    public void addChair(UUID uuid, Block block, BlockFace face) {
        Chair chair = new Chair(block.getLocation(), face);
        Chairs.put(uuid,chair);
        database.save("Chairs", uuid.toString(), chair.getLocation());
        database.save("Chairs", uuid+".direction", chair.getDirection().name());
    }

    public void removeChair(Block block) {
        for (UUID uuid : Chairs.keySet()) {
            Chair chair = Chairs.get(uuid);
            if (Utilities.locationMatch(chair.getLocation(), block.getLocation())) {
                Chairs.remove(uuid);
                database.save("Chairs", uuid.toString(), null);
            }
        }
    }

    public ConcurrentHashMap<UUID, Chair> getChairs() {
        return Chairs;
    }

    public Chair getChair(Block block) {
        for (UUID uuid : Chairs.keySet()) {
            Chair chair = Chairs.get(uuid);
            if (Utilities.locationMatch(chair.getLocation(), block.getLocation())) {
                return chair;
            }
        }
        return null;
    }

    public void sit(Player p, Block block) {
        Chair bChair = getChair(block);
        if (bChair == null) return;

        Location loc = block.getLocation();
        loc.setX(loc.getBlockX() + 0.5);
        loc.setZ(loc.getBlockZ() + 0.5);
        loc.setY(loc.getBlockY() - 1.2);

        loc.setYaw(switch (bChair.getDirection()) {
            case EAST,EAST_NORTH_EAST,EAST_SOUTH_EAST -> -90;
            case WEST,WEST_NORTH_WEST,WEST_SOUTH_WEST -> 90;
            case NORTH,NORTH_EAST,NORTH_NORTH_EAST,NORTH_NORTH_WEST,NORTH_WEST -> 180;
            default -> 0;
        });

        ArmorStand chair = p.getWorld().spawn(loc, ArmorStand.class);

        PersistentDataContainer data = chair.getPersistentDataContainer();
        data.set(namespacedKey, PersistentDataType.INTEGER, 1);

        chair.setInvisible(true);
        chair.setGravity(false);
        chair.setInvulnerable(true);
        chair.setCustomNameVisible(false);
        chair.addPassenger(p);

        if (Config.USE_MESSAGES.asBoolean()) {
            p.sendMessage(Lang.SITTING.prefixed());
        }
    }

}
