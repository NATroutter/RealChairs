package fi.natroutter.realchairs.handlers;

import fi.natroutter.realchairs.Utilities.Utils;
import fi.natroutter.realchairs.files.Config;
import fi.natroutter.realchairs.files.Lang;
import fi.natroutter.natlibs.handlers.database.YamlDatabase;
import fi.natroutter.natlibs.utilities.Utilities;
import fi.natroutter.realchairs.RealChairs;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ChairHandler {

    private final YamlDatabase database = RealChairs.getDatabase();

    private NamespacedKey namespacedKey = new NamespacedKey(RealChairs.getInstance(), "RealChairs");

    public static ConcurrentHashMap<UUID, Location> dismounts = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<UUID, Chair> chairs = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<UUID, Double> heights = new ConcurrentHashMap<>();

    public static ArrayList<UUID> display = new ArrayList<>();

    public static double getHeight(Player p) {return heights.getOrDefault(p.getUniqueId(), Chair.DEFAULT_HEIGHT);}
    public static void setHeight(Player p, double height) {heights.put(p.getUniqueId(), height);}

    public ChairHandler(JavaPlugin plugin) {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            if (display.isEmpty()) return;
            if (chairs.isEmpty()) return;
            for (UUID uuid : display) {
                Player p = Bukkit.getPlayer(uuid);
                if (p == null) continue;
                if (!p.isOnline()) continue;

                for(Map.Entry<UUID, Chair> entry : chairs.entrySet()) {
                    Utils.drawBlock(p ,entry.getValue().getLocation(), ColorTable.DISPLAY);
                }

            }
        }, 0, 5);

        wipe();
        Set<String> keys = database.getKeys("Chairs");
        if (keys == null) return;
        for (String key : keys) {
            Location loc = database.getLocation("Chairs", key);
            BlockFace face = BlockFace.valueOf(database.getString("Chairs", key+".direction"));
            double height = database.getDouble("Chairs", key+".height");
            Chair chair = new Chair(loc, face, height);
            chairs.put(UUID.fromString(key), chair);
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

    public void deleteEveryting(Player p) {
        for (UUID uuid : chairs.keySet()) {
            Chair chair = chairs.get(uuid);
            Utils.drawBlock(p, chair.getLocation(), ColorTable.REMOVED);
            database.save("Chairs", uuid.toString(), null);
        }
        chairs.clear();
        p.sendMessage(Lang.ALL_CHAIRS_WIPED.prefixed());
    }

    public void cleanInvalid(Player p) {
        if (chairs.size() > 0) {
            int countter = 0;
            for (UUID uuid : chairs.keySet()) {
                Chair chair = chairs.get(uuid);
                if (chair.getLocation().getBlock().getType() == Material.AIR) {
                    chairs.remove(uuid);
                    database.save("Chairs", uuid.toString(), null);
                    Utils.drawBlock(p, chair.getLocation(), ColorTable.REMOVED);
                    countter++;
                }
            }
            if (countter > 0) {
                p.sendMessage(Lang.INVALID_REMOVED.prefixed(
                        Placeholder.parsed("amount", String.valueOf(countter))
                ));
            } else {
                p.sendMessage(Lang.NO_INVALID.prefixed());
            }
        } else {
            p.sendMessage(Lang.NO_CHAIRS.prefixed());
        }
    }

    public double getChairHeight(Block block) {
        for (UUID uuid : chairs.keySet()) {
            Chair chair = chairs.get(uuid);
            if (Utilities.locationMatch(chair.getLocation(), block.getLocation())) {
                return chair.getHeight();
            }
        }
        return Chair.DEFAULT_HEIGHT;
    }

    public boolean isChair(Block block) {
        for (UUID uuid : chairs.keySet()) {
            Chair chair = chairs.get(uuid);
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

    public void addChair(Player p, UUID uuid, Block block, BlockFace face) {
        addChair(p, uuid, block, face, getHeight(p));
    }
    public void addChair(Player p, UUID uuid, Block block, BlockFace face, double height) {
        Chair chair = new Chair(block.getLocation(), face, height);
        chairs.put(uuid,chair);
        database.save("Chairs", uuid.toString(), chair.getLocation());
        database.save("Chairs", uuid+".direction", chair.getDirection().name());
        database.save("Chairs", uuid+".height", height);

        Utils.drawBlock(p, block.getLocation(), ColorTable.ADDED);
        p.sendMessage(Lang.CHAIR_ADDED.prefixed());
    }

    public void removeChair(Player p, Block block) {
        for (UUID uuid : chairs.keySet()) {
            Chair chair = chairs.get(uuid);
            if (Utilities.locationMatch(chair.getLocation(), block.getLocation())) {
                chairs.remove(uuid);
                database.save("Chairs", uuid.toString(), null);
            }
        }
        Utils.drawBlock(p, block.getLocation(), ColorTable.REMOVED);
        p.sendMessage(Lang.CHAIR_REMOVED.prefixed());
    }

    public ConcurrentHashMap<UUID, Chair> getChairs() {
        return chairs;
    }

    public Chair getChair(Block block) {
        for (UUID uuid : chairs.keySet()) {
            Chair chair = chairs.get(uuid);
            if (Utilities.locationMatch(chair.getLocation(), block.getLocation())) {
                return chair;
            }
        }
        return null;
    }

    public void sit(Player p, Block block) {
        Chair bChair = getChair(block);
        if (bChair == null) return;
        sit(p, block, bChair.getDirection(), bChair.getHeight());
    }

    public void sit(Player p, Block block, BlockFace face, double height) {
        dismounts.put(p.getUniqueId(), p.getLocation());

        Location loc = block.getLocation();
        loc.setX(loc.getBlockX() + 0.5);
        loc.setZ(loc.getBlockZ() + 0.5);
        loc.setY(loc.getBlockY() + height);

        loc.setYaw(switch (face) {
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

        Location n = p.getLocation().clone();
        n.setPitch(0);
        n.setYaw(loc.getYaw());
        p.teleport(n);

        chair.addPassenger(p);


        if (Config.USE_MESSAGES.asBoolean()) {
            p.sendMessage(Lang.SITTING.prefixed());
        }
    }

}
