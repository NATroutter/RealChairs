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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MainCommand extends Command {

    private final ChairHandler chairHandler = RealChairs.getChairHandler();

    private final ConcurrentHashMap<UUID, String> wipeConfirm = new ConcurrentHashMap<>();

    public MainCommand() {
        super("RealChairs");
        this.setAliases(Config.CMD_ALIASES.asStringList());
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
            if (args[0].equalsIgnoreCase(Config.CMD_HELP_ARG.asString())) {
                if (!p.hasPermission(Config.CMD_HELP_PERM.asString())) {
                    p.sendMessage(Lang.NO_PERM.prefixed());
                }
                p.sendMessage(Lang.HELP_MESSAGE.asSingleComponent(
                        Placeholder.parsed("command", label),
                        Placeholder.parsed("selected_height", String.valueOf(ChairHandler.getHeight(p,null))),
                        Placeholder.parsed("arg_add", Config.CMD_ADD_ARG.asString()),
                        Placeholder.parsed("arg_sit", Config.CMD_SIT_ARG.asString()),
                        Placeholder.parsed("arg_list", Config.CMD_LIST_ARG.asString()),
                        Placeholder.parsed("arg_remove", Config.CMD_REMOVE_ARG.asString()),
                        Placeholder.parsed("arg_reload", Config.CMD_RELOAD_ARG.asString()),
                        Placeholder.parsed("arg_show", Config.CMD_SHOW_ARG.asString()),
                        Placeholder.parsed("arg_height", Config.CMD_HEIGHT_ARG.asString()),
                        Placeholder.parsed("arg_tool", Config.CMD_TOOL_ARG.asString()),
                        Placeholder.parsed("arg_disable", Config.CMD_HEIGHT_DISABLE.asString())
                ));
            } else if (args[0].equalsIgnoreCase(Config.CMD_WIPE_ARG.asString())) {
                if (!p.hasPermission(Config.CMD_WIPE_PERM.asString())) {
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
            } else if (args[0].equalsIgnoreCase(Config.CMD_CLEAN_ARG.asString())) {
                if (!p.hasPermission(Config.CMD_CLEAN_PERM.asString())) {
                    p.sendMessage(Lang.NO_PERM.prefixed());
                }
                chairHandler.cleanInvalid(p);

            } else if (args[0].equalsIgnoreCase(Config.CMD_TOOL_ARG.asString())) {
                if (!p.hasPermission(Config.CMD_TOOL_PERM.asString())) {
                    p.sendMessage(Lang.NO_PERM.prefixed());
                }
                p.getInventory().addItem(Items.chairTool());
                p.sendMessage(Lang.TOOL_ADDED_INV.prefixed());

            } else if (args[0].equalsIgnoreCase(Config.CMD_RELOAD_ARG.asString())) {
                if (!p.hasPermission(Config.CMD_RELOAD_PERM.asString())) {
                    p.sendMessage(Lang.NO_PERM.prefixed());
                }
                Config.LANGUAGE.reloadFile();
                Lang.PREFIX.reloadFile();
                p.sendMessage(Lang.RELOADED.prefixed());

            } else if (args[0].equalsIgnoreCase(Config.CMD_ADD_ARG.asString())) {
                if (!p.hasPermission(Config.CMD_ADD_PERM.asString())) {
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

            } else if (args[0].equalsIgnoreCase(Config.CMD_SHOW_ARG.asString())) {
                if (!p.hasPermission(Config.CMD_SHOW_PERM.asString())) {
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

            } else if (args[0].equalsIgnoreCase(Config.CMD_SIT_ARG.asString())) {
                if (!p.hasPermission(Config.CMD_SIT_PERM.asString())) {
                    p.sendMessage(Lang.NO_PERM.prefixed());
                }
                Block target = p.getTargetBlock(3, TargetBlockInfo.FluidMode.NEVER);
                BlockFace face = p.getTargetBlockFace(3, TargetBlockInfo.FluidMode.NEVER);
                if (target == null || face == null) {
                    p.sendMessage(Lang.INVALID_TARGET.prefixed());
                    return false;
                }
                chairHandler.sit(p, target, face, Chair.defaultHeight(target));

            } else if (args[0].equalsIgnoreCase(Config.CMD_REMOVE_ARG.asString())) {
                if (!p.hasPermission(Config.CMD_REMOVE_PERM.asString())) {
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
            } else if (args[0].equalsIgnoreCase(Config.CMD_LIST_ARG.asString())) {

                if (!p.hasPermission(Config.CMD_LIST_PERM.asString())) {
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

            if (args[0].equalsIgnoreCase(Config.CMD_SIT_ARG.asString())) {
                if (!p.hasPermission(Config.CMD_SIT_PERM_SELECT_HEIGHT.asString())) {
                    p.sendMessage(Lang.NO_PERM.prefixed());
                }

                double height = Parser.doublee(args[1], Chair.defaultHeight());
                Block target = p.getTargetBlock(3, TargetBlockInfo.FluidMode.NEVER);
                BlockFace face = p.getTargetBlockFace(3, TargetBlockInfo.FluidMode.NEVER);
                if (target == null || face == null) {
                    p.sendMessage(Lang.INVALID_TARGET.prefixed());
                    return false;
                }
                chairHandler.sit(p, target, face, height);

            } else if (args[0].equalsIgnoreCase(Config.CMD_WIPE_ARG.asString())) {
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

            } else if (args[0].equalsIgnoreCase(Config.CMD_HEIGHT_ARG.asString())) {
                if (!p.hasPermission(Config.CMD_HEIGHT_PERM.asString())) {
                    p.sendMessage(Lang.NO_PERM.prefixed());
                }
                if (args[1].equalsIgnoreCase(Config.CMD_HEIGHT_DISABLE.asString())) {
                    ChairHandler.disableHeight(p);
                    p.sendMessage(Lang.CUSTOM_HEIGHT_DISABLED.prefixed());
                    return false;
                }
                double height = Parser.doublee(args[1], Chair.defaultHeight());
                ChairHandler.setHeight(p, height);
                p.sendMessage(Lang.CHAIR_HEIGHT_SET.prefixed(
                        Placeholder.parsed("height", String.valueOf(height))
                ));

            } else if (args[0].equalsIgnoreCase(Config.CMD_ADD_ARG.asString())) {
                if (!p.hasPermission(Config.CMD_ADD_PERM.asString())) {
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
                double height = Parser.doublee(args[1], Chair.defaultHeight(target));

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
            List<Complete> list = new ArrayList<>(List.of(
                    new Complete(Config.CMD_HELP_ARG, Config.CMD_HELP_PERM),
                    new Complete(Config.CMD_ADD_ARG, Config.CMD_ADD_PERM),
                    new Complete(Config.CMD_REMOVE_ARG, Config.CMD_REMOVE_PERM),
                    new Complete(Config.CMD_LIST_ARG, Config.CMD_LIST_PERM),
                    new Complete(Config.CMD_RELOAD_ARG, Config.CMD_RELOAD_PERM),
                    new Complete(Config.CMD_SHOW_ARG, Config.CMD_SHOW_PERM),
                    new Complete(Config.CMD_TOOL_ARG, Config.CMD_TOOL_PERM),
                    new Complete(Config.CMD_HEIGHT_ARG, Config.CMD_HEIGHT_PERM),
                    new Complete(Config.CMD_CLEAN_ARG, Config.CMD_CLEAN_PERM),
                    new Complete(Config.CMD_WIPE_ARG, Config.CMD_WIPE_PERM)
            ));
            if (sender.hasPermission(Config.CMD_SIT_PERM.asString()) || sender.hasPermission(Config.CMD_SIT_PERM_SELECT_HEIGHT.asString())) {
                if (sender.hasPermission(Config.CMD_SIT_PERM.asString())) {
                    list.add(new Complete(Config.CMD_SIT_ARG, Config.CMD_SIT_PERM));

                } else if (sender.hasPermission(Config.CMD_SIT_PERM_SELECT_HEIGHT.asString())) {
                    list.add(new Complete(Config.CMD_SIT_ARG, Config.CMD_SIT_PERM_SELECT_HEIGHT));
                }
            }

            return Utilities.getCompletesWithPerms(sender,args[0],list);
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase(Config.CMD_ADD_ARG.asString())) {
                if (sender instanceof Player p) {
                    return Utilities.getCompletes(sender, args[1], List.of(
                            String.valueOf(ChairHandler.getHeight(p,null)),
                            Config.CMD_HEIGHT_DISABLE.asString()
                    ));
                }
            }
        }
        return Utilities.emptyTab();
    }

}

