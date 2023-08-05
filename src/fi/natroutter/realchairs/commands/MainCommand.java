package fi.natroutter.realchairs.commands;

import com.destroystokyo.paper.block.TargetBlockInfo;
import fi.natroutter.realchairs.RealChairs;
import fi.natroutter.realchairs.files.Config;
import fi.natroutter.realchairs.files.Lang;
import fi.natroutter.realchairs.handlers.ChairHandler;
import fi.natroutter.natlibs.objects.Complete;
import fi.natroutter.natlibs.utilities.Utilities;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class MainCommand extends Command {

    private ChairHandler chairHandler;

    public MainCommand(RealChairs chairs) {
        super("RealChairs");
        this.setAliases(List.of("rc", "chair", "chairs"));
        this.chairHandler = chairs.getChairHandler();
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Lang.ONLY_IN_GAME.prefixed());
            return false;
        }
        Player p = (Player)sender;

        if (args.length == 0) {
            p.sendMessage(" ");
            p.sendMessage("§8§l§m━━━━━━━━━━━━§8§l|§9§l RealChairs §8§l|§m━━━━━━━━━━━━");
            p.sendMessage("§8§l» §7RealChairs version §b" + RealChairs.getInstance().getDescription().getVersion());
            p.sendMessage("§8§l» §7Made by: §bNATroutter");
            p.sendMessage("§8§l» §7Website: §bhttps://NATroutter.net");
            p.sendMessage("§8§l§m━━━━━━━━━━━━§8§l|§9§l RealChairs §8§l|§m━━━━━━━━━━━━");
            p.sendMessage(" ");
        } else if (args.length == 1) {

            if (args[0].equalsIgnoreCase("help")) {
                if (!p.hasPermission("realchairs.help")) {
                    p.sendMessage(Lang.NO_PERM.prefixed());
                }
                for (Component line : Lang.HELP_MESSAGE.asComponentList()) {
                    p.sendMessage(line);
                }
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
                chairHandler.addChair(UUID.randomUUID(), target, face);
                p.sendMessage(Lang.CHAIR_ADDED.prefixed());
            } else if (args[0].equalsIgnoreCase("remove")) {
                if (!p.hasPermission("realchairs.remove")) {
                    p.sendMessage(Lang.NO_PERM.prefixed());
                }
                Block target = p.getTargetBlock(3, TargetBlockInfo.FluidMode.NEVER);
                if (target == null) {
                    p.sendMessage(Lang.INVALID_TARGET.prefixed());
                    return false;
                }
                chairHandler.removeChair(target);
                p.sendMessage(Lang.CHAIR_REMOVED.prefixed());
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
                chairHandler.getChairs().forEach((id,chair)-> {
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
                    new Complete("reload","realchairs.reload")
            ));
        }
        return Utilities.emptyTab();
    }

}

