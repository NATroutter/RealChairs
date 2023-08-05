package fi.natroutter.realchairs.commands;

import com.destroystokyo.paper.block.TargetBlockInfo;
import fi.natroutter.natlibs.utilities.Parser;
import fi.natroutter.realchairs.RealChairs;
import fi.natroutter.realchairs.Utilities.Items;
import fi.natroutter.realchairs.files.Config;
import fi.natroutter.realchairs.files.Lang;
import fi.natroutter.realchairs.handlers.Chair;
import fi.natroutter.realchairs.handlers.ChairHandler;
import fi.natroutter.natlibs.objects.Complete;
import fi.natroutter.natlibs.utilities.Utilities;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MainCommand extends Command {

    private ChairHandler chairHandler;

    private final ConcurrentHashMap<UUID, String> wipeConfirm = new ConcurrentHashMap<>();

    public MainCommand(RealChairs chairs) {
        super("RealChairs");
        this.setAliases(List.of("rc", "chair", "chairs"));
        this.chairHandler = chairs.getChairHandler();
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage(Lang.ONLY_IN_GAME.prefixed());
            return false;
        }

        if (args.length == 0) {
            p.sendMessage(" ");
            p.sendMessage("§8§l§m━━━━━━━━━━━━§8§l|§5§l RealChairs §8§l|§m━━━━━━━━━━━━");
            p.sendMessage("§8§l» §7RealChairs version §d" + RealChairs.getInstance().getDescription().getVersion());
            p.sendMessage("§8§l» §7Made by: §dNATroutter");
            p.sendMessage("§8§l» §7Website: §dhttps://NATroutter.fi");
            p.sendMessage("§8§l§m━━━━━━━━━━━━§8§l|§5§l RealChairs §8§l|§m━━━━━━━━━━━━");
            p.sendMessage(" ");
        } else if (args.length == 1) {

            if (args[0].equalsIgnoreCase("help")) {
                if (!p.hasPermission("realchairs.help")) {
                    p.sendMessage(Lang.NO_PERM.prefixed());
                }
                p.sendMessage(Lang.HELP_MESSAGE.asSingleComponent(
                    Placeholder.parsed("command", label),
                    Placeholder.parsed("selected_height", String.valueOf(ChairHandler.getHeight(p)))
                ));

            } else if (args[0].equalsIgnoreCase("wipe")) {
                if (!p.hasPermission("realchairs.wipe")) {
                    p.sendMessage(Lang.NO_PERM.prefixed());
                }
                if (chairHandler.getChairs().size() > 0) {
                    String id = UUID.randomUUID().toString();
                    wipeConfirm.put(p.getUniqueId(), id);

                    Component accept = Lang.CONFIRM_ACCEPT_BTN.asComponent();
                    accept = accept.hoverEvent(HoverEvent.showText(Lang.CONFIRM_ACCEPT_HINT.asComponent()));
                    accept = accept.clickEvent(ClickEvent.runCommand("/" + label + " wipe " + id));

                    Component deny = Lang.CONFIRM_DENY_BTN.asComponent();
                    deny = deny.hoverEvent(HoverEvent.showText(Lang.CONFIRM_DENY_HINT.asComponent()));
                    deny = deny.clickEvent(ClickEvent.runCommand("/" + label + " wipe deny-"+ id));

                    p.sendMessage(Lang.CONFIRM_MESSAGE.asSingleComponent(
                            Placeholder.component("accept", accept),
                            Placeholder.component("deny", deny)
                    ));
                } else {
                    p.sendMessage(Lang.NO_CHAIRS.prefixed());
                }
            } else if (args[0].equalsIgnoreCase("clean")) {
                if (!p.hasPermission("realchairs.clean")) {
                    p.sendMessage(Lang.NO_PERM.prefixed());
                }
                chairHandler.cleanInvalid(p);

            } else if (args[0].equalsIgnoreCase("tool")) {
                if (!p.hasPermission("realchairs.tool")) {
                    p.sendMessage(Lang.NO_PERM.prefixed());
                }
                p.getInventory().addItem(Items.chairTool());
                p.sendMessage(Lang.TOOL_ADDED_INV.prefixed());

            } else if (args[0].equalsIgnoreCase("reload")) {
                if (!p.hasPermission("realchairs.reload")) {
                    p.sendMessage(Lang.NO_PERM.prefixed());
                }
                Config.LANGUAGE.reloadFile();
                Lang.PREFIX.reloadFile();
                p.sendMessage(Lang.RELOADED.prefixed());

            } else if (args[0].equalsIgnoreCase("add")) {
                if (!p.hasPermission("realchairs.add")) {
                    p.sendMessage(Lang.NO_PERM.prefixed());
                }
                Block target = p.getTargetBlock(3, TargetBlockInfo.FluidMode.NEVER);
                BlockFace face = p.getTargetBlockFace(3, TargetBlockInfo.FluidMode.NEVER);
                if (target == null || face == null) {
                    p.sendMessage(Lang.INVALID_TARGET.prefixed());
                    return false;
                }
                if (chairHandler.isChair(target)) {
                    p.sendMessage(Lang.ALREADY_CHAIR.prefixed());
                    return false;
                }
                chairHandler.addChair(p, UUID.randomUUID(), target, face);

            } else if (args[0].equalsIgnoreCase("show")) {
                if (!p.hasPermission("realchairs.show")) {
                    p.sendMessage(Lang.NO_PERM.prefixed());
                }
                if (chairHandler.getChairs().size() > 0 ) {
                    if (ChairHandler.display.contains(p.getUniqueId())) {
                        ChairHandler.display.remove(p.getUniqueId());
                        p.sendMessage(Lang.CHAIR_DISPLAY.prefixed(Placeholder.component("state", Lang.TOGGLE_OFF.asComponent())));
                    } else {
                        ChairHandler.display.add(p.getUniqueId());
                        p.sendMessage(Lang.CHAIR_DISPLAY.prefixed(Placeholder.component("state", Lang.TOGGLE_ON.asComponent())));
                    }
                } else {
                    p.sendMessage(Lang.NO_CHAIRS.prefixed());
                }

            } else if (args[0].equalsIgnoreCase("sit")) {
                if (!p.hasPermission("realchairs.sit")) {
                    p.sendMessage(Lang.NO_PERM.prefixed());
                }
                Block target = p.getTargetBlock(3, TargetBlockInfo.FluidMode.NEVER);
                BlockFace face = p.getTargetBlockFace(3, TargetBlockInfo.FluidMode.NEVER);
                if (target == null || face == null) {
                    p.sendMessage(Lang.INVALID_TARGET.prefixed());
                    return false;
                }
                chairHandler.sit(p, target, face, Chair.DEFAULT_HEIGHT);

            } else if (args[0].equalsIgnoreCase("remove")) {
                if (!p.hasPermission("realchairs.remove")) {
                    p.sendMessage(Lang.NO_PERM.prefixed());
                }
                Block target = p.getTargetBlock(3, TargetBlockInfo.FluidMode.NEVER);
                if (target == null) {
                    p.sendMessage(Lang.INVALID_TARGET.prefixed());
                    return false;
                }
                if (!chairHandler.isChair(target)) {
                    p.sendMessage(Lang.BLOCK_NOT_CHAIR.prefixed());
                    return false;
                }
                chairHandler.removeChair(p, target);
            } else if (args[0].equalsIgnoreCase("list")) {

                if (!p.hasPermission("realchairs.list")) {
                    p.sendMessage(Lang.NO_PERM.prefixed());
                }

                if (chairHandler.getChairs() == null || chairHandler.getChairs().isEmpty()) {
                    p.sendMessage(Lang.NO_CHAIRS.prefixed());
                    return false;
                }

                p.sendMessage(Lang.CFM_HEADER.asComponent());
                AtomicInteger countter = new AtomicInteger(1);
                chairHandler.getChairs().forEach((id, chair) -> {
                    Location loc = chair.getLocation();
                    Component line = Lang.CFM_ENTRY.asComponent(
                            Placeholder.parsed("num", String.valueOf(countter.get())),
                            Placeholder.parsed("x", String.valueOf(loc.getBlockX())),
                            Placeholder.parsed("y", String.valueOf(loc.getBlockY())),
                            Placeholder.parsed("z", String.valueOf(loc.getBlockZ()))
                    );

                    Component tp_btn = Utilities.translateColors(Lang.CFM_TELEPORT_BTN);
                    tp_btn = tp_btn.clickEvent(ClickEvent.runCommand("/minecraft:tp " + p.getName() + " " + loc.getBlockX() + " " + (loc.getBlockY() + 1) + " " + loc.getBlockZ()));
                    tp_btn = tp_btn.hoverEvent(Lang.CFM_TELEPORT_HINT.asComponent());
                    p.sendMessage(Component.join(JoinConfiguration.noSeparators(), line, tp_btn));
                    countter.getAndIncrement();
                });
                p.sendMessage(Lang.CFM_FOOTER.asComponent());

            } else {
                p.sendMessage(Lang.INVALID_ARGS.prefixed());
            }

        } else if (args.length == 2) {

            if (args[0].equalsIgnoreCase("sit")) {
                if (!p.hasPermission("realchairs.sit")) {
                    p.sendMessage(Lang.NO_PERM.prefixed());
                }

                double height = Parser.doublee(args[1], Chair.DEFAULT_HEIGHT);
                Block target = p.getTargetBlock(3, TargetBlockInfo.FluidMode.NEVER);
                BlockFace face = p.getTargetBlockFace(3, TargetBlockInfo.FluidMode.NEVER);
                if (target == null || face == null) {
                    p.sendMessage(Lang.INVALID_TARGET.prefixed());
                    return false;
                }
                chairHandler.sit(p, target, face, height);

            } else if (args[0].equalsIgnoreCase("wipe")) {
                String id = wipeConfirm.getOrDefault(p.getUniqueId(), null);
                if (id == null) {
                    p.sendMessage(Lang.NO_PENDING_CONFIRM.prefixed());
                    return false;
                } else {
                    if (args[1].startsWith("deny-")) {
                        String denyId = args[1].replace("deny-", "");
                        if (!id.equalsIgnoreCase(denyId)) {
                            p.sendMessage(Lang.INVALID_CONFIRM_ID.prefixed());
                            return false;
                        }
                        if (denyId.equalsIgnoreCase(id)) {
                            wipeConfirm.remove(p.getUniqueId());
                            p.sendMessage(Lang.CHAIRS_WIPE_CANCELED.prefixed());
                            return false;
                        }
                    }
                    if (!id.equalsIgnoreCase(args[1])) {
                        p.sendMessage(Lang.INVALID_CONFIRM_ID.prefixed());
                        return false;
                    }
                    wipeConfirm.remove(p.getUniqueId());
                    chairHandler.deleteEveryting(p);
                }

            } else if (args[0].equalsIgnoreCase("height")) {
                if (!p.hasPermission("realchairs.height")) {
                    p.sendMessage(Lang.NO_PERM.prefixed());
                }
                double height = Parser.doublee(args[1], Chair.DEFAULT_HEIGHT);
                ChairHandler.setHeight(p, height);
                p.sendMessage(Lang.CHAIR_HEIGHT_SET.prefixed(
                        Placeholder.parsed("height", String.valueOf(height))
                ));

            } else if (args[0].equalsIgnoreCase("add")) {
                if (!p.hasPermission("realchairs.add")) {
                    p.sendMessage(Lang.NO_PERM.prefixed());
                }
                double height = Parser.doublee(args[1], Chair.DEFAULT_HEIGHT);
                Block target = p.getTargetBlock(3, TargetBlockInfo.FluidMode.NEVER);
                BlockFace face = p.getTargetBlockFace(3, TargetBlockInfo.FluidMode.NEVER);
                if (target == null || face == null) {
                    p.sendMessage(Lang.INVALID_TARGET.prefixed());
                    return false;
                }
                if (chairHandler.isChair(target)) {
                    p.sendMessage(Lang.ALREADY_CHAIR.prefixed());
                    return false;
                }
                chairHandler.addChair(p, UUID.randomUUID(), target, face, height);

            } else {
                p.sendMessage(Lang.INVALID_ARGS.prefixed());
            }

        } else {
            p.sendMessage(Lang.TOO_MANY_ARGS.prefixed());
        }

        return false;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {

        if (args.length == 1) {
             return Utilities.getCompletesWithPerms(sender,args[0],List.of(
                     new Complete("help","realchairs.help"),
                     new Complete("add","realchairs.add"),
                     new Complete("remove","realchairs.remove"),
                     new Complete("list","realchairs.list"),
                     new Complete("reload","realchairs.reload"),
                     new Complete("sit","realchairs.sit"),
                     new Complete("show","realchairs.show"),
                     new Complete("tool","realchairs.tool"),
                     new Complete("height","realchairs.height"),
                     new Complete("clean","realchairs.clean"),
                     new Complete("wipe","realchairs.wipe")
            ));
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("height")) {
                if (sender instanceof Player p) {
                    return Utilities.getCompletes(sender, args[1], List.of(
                            String.valueOf(ChairHandler.getHeight(p))
                    ));
                }
            }
        }
        return Utilities.emptyTab();
    }

}

