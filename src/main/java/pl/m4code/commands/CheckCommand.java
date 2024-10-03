package pl.m4code.commands;

import lombok.NonNull;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import pl.m4code.Main;
import pl.m4code.commands.api.CommandAPI;
import pl.m4code.utils.TextUtil;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class CheckCommand extends CommandAPI implements Listener {
    private static final String CHECK_PERMISSION = "m4code.check";
    private static final String RELOAD_PERMISSION = "m4code.reload";
    private static final String EXEMPT_PERMISSION = "m4code.check.bypass";
    private Location checkLocation;
    private Location clearLocation;
    private final Set<UUID> beingChecked;

    public CheckCommand(Set<UUID> beingChecked) {
        super(
                "check",
                "",
                "",
                "/check <reload|sprawdz|set1|set2|czysty|cheater>",
                List.of("")
        );
        this.beingChecked = beingChecked;
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
        loadCheckState();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission(CHECK_PERMISSION)) {
            TextUtil.sendMessage(sender, getConfigMessage("messages.no_permission"));
            return;
        }

        if (args.length == 0) {
            TextUtil.sendMessage(sender, getConfigMessage("messages.usage"));
            return;
        }

        FileConfiguration config = Main.getInstance().getConfig();
        String adminName = sender instanceof Player ? ((Player) sender).getName() : "Console";

        switch (args[0].toLowerCase()) {
            case "reload":
                if (sender.hasPermission(RELOAD_PERMISSION)) {
                    Main.getInstance().reloadConfig();
                    loadCheckState();
                    TextUtil.sendMessage(sender, getConfigMessage("messages.reload"));
                } else {
                    TextUtil.sendMessage(sender, getConfigMessage("messages.no_permission"));
                }
                break;
            case "sprawdz":
                if (args.length < 2) {
                    TextUtil.sendMessage(sender, getConfigMessage("messages.usage_sprawdz"));
                    return;
                }
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    TextUtil.sendMessage(sender, getConfigMessage("messages.player_not_found"));
                    return;
                }
                if (target.equals(sender)) {
                    TextUtil.sendMessage(sender, getConfigMessage("messages.cannot_check_self"));
                    return;
                }
                if (target.hasPermission(EXEMPT_PERMISSION)) {
                    TextUtil.sendMessage(sender, getConfigMessage("messages.cannot_check_exempt"));
                    return;
                }
                if (checkLocation == null) {
                    TextUtil.sendMessage(sender, getConfigMessage("messages.check_location_not_set"));
                    return;
                }
                target.teleport(checkLocation);
                startTitleTask(target);
                TextUtil.sendMessage(sender, getConfigMessage("messages.check_started").replace("%player%", target.getName()));
                Bukkit.broadcastMessage(TextUtil.fixColor(getConfigMessage("messages.check_started_broadcast").replace("%player%", target.getName()).replace("%admin%", adminName)));
                beingChecked.add(target.getUniqueId());
                break;
            case "set1":
                if (!(sender instanceof Player)) {
                    TextUtil.sendMessage(sender, getConfigMessage("messages.console_cannot_set_location"));
                    return;
                }
                Player player = (Player) sender;
                checkLocation = player.getLocation();
                config.set("check.location", checkLocation);
                Main.getInstance().saveConfig();
                TextUtil.sendMessage(sender, getConfigMessage("messages.check_location_set"));
                break;
            case "set2":
                if (!(sender instanceof Player)) {
                    TextUtil.sendMessage(sender, getConfigMessage("messages.console_cannot_set_location"));
                    return;
                }
                player = (Player) sender;
                clearLocation = player.getLocation();
                config.set("clear.location", clearLocation);
                Main.getInstance().saveConfig();
                TextUtil.sendMessage(sender, getConfigMessage("messages.clear_location_set"));
                break;
            case "czysty":
                if (args.length < 2) {
                    TextUtil.sendMessage(sender, getConfigMessage("messages.usage_czysty"));
                    return;
                }
                target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    TextUtil.sendMessage(sender, getConfigMessage("messages.player_not_found"));
                    return;
                }
                if (!beingChecked.contains(target.getUniqueId())) {
                    TextUtil.sendMessage(sender, getConfigMessage("messages.player_not_being_checked").replace("%player%", target.getName()));
                    return;
                }
                if (clearLocation == null) {
                    TextUtil.sendMessage(sender, getConfigMessage("messages.clear_location_not_set"));
                    return;
                }
                target.teleport(clearLocation);
                Bukkit.broadcastMessage(TextUtil.fixColor(getConfigMessage("messages.player_czysty").replace("%player%", target.getName()).replace("%admin%", adminName)));
                beingChecked.remove(target.getUniqueId());
                break;
            case "cheater":
                if (args.length < 2) {
                    TextUtil.sendMessage(sender, getConfigMessage("messages.usage_cheater"));
                    return;
                }
                target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    TextUtil.sendMessage(sender, getConfigMessage("messages.player_not_found"));
                    return;
                }
                if (!beingChecked.contains(target.getUniqueId())) {
                    TextUtil.sendMessage(sender, getConfigMessage("messages.player_not_being_checked").replace("%player%", target.getName()));
                    return;
                }
                String cheaterCommand = config.getString("console_commands.cheater").replace("%player%", target.getName());
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cheaterCommand);
                TextUtil.sendMessage(sender, getConfigMessage("messages.player_cheater").replace("%player%", target.getName()).replace("%admin%", adminName));
                beingChecked.remove(target.getUniqueId());
                break;
            default:
                TextUtil.sendMessage(sender, getConfigMessage("messages.unknown_subcommand"));
                break;
        }
    }

    private void loadCheckState() {
        FileConfiguration config = Main.getInstance().getConfig();
        checkLocation = (Location) config.get("check.location");
        clearLocation = (Location) config.get("clear.location");
    }

    private String getConfigMessage(String path) {
        FileConfiguration config = Main.getInstance().getConfig();
        String message = config.getString(path);
        return message;
    }

    private void startTitleTask(Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!beingChecked.contains(player.getUniqueId())) {
                    this.cancel();
                    return;
                }
                player.sendTitle(TextUtil.fixColor(getConfigMessage("messages.check_title")), TextUtil.fixColor(getConfigMessage("messages.check_subtitle")), 10, 70, 20);
            }
        }.runTaskTimer(Main.getInstance(), 0, 20); // Refresh every 5 seconds (100 ticks)
    }

    @Override
    public List<String> tab(@NonNull Player player, @NotNull @NonNull String[] args) {
        if (!player.hasPermission(CHECK_PERMISSION)) {
            return List.of();
        }

        if (args.length == 1) {
            return List.of("reload", "sprawdz", "set1", "set2", "czysty", "cheater");
        } else if (args.length == 2 && (args[0].equalsIgnoreCase("sprawdz") || args[0].equalsIgnoreCase("czysty") || args[0].equalsIgnoreCase("cheater"))) {
            return Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .collect(Collectors.toList());
        }
        return List.of();
    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if (beingChecked.contains(player.getUniqueId())) {
            List<String> allowedCommands = Main.getInstance().getConfig().getStringList("allowed_commands");
            String command = event.getMessage().split(" ")[0];
            if (!allowedCommands.contains(command)) {
                event.setCancelled(true);
                TextUtil.sendMessage(player, getConfigMessage("messages.command_not_allowed"));
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (beingChecked.contains(player.getUniqueId())) {
            if (!isPlayerBanned(player)) {
                String logoutCommand = Main.getInstance().getConfig().getString("console_commands.logout").replace("%player%", player.getName());
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), logoutCommand);
                TextUtil.sendMessage(Bukkit.getConsoleSender(), getConfigMessage("messages.player_logged_out_during_check").replace("%player%", player.getName()));
            }
            beingChecked.remove(player.getUniqueId());
        }
    }

    private boolean isPlayerBanned(Player player) {
        BanList banList = Bukkit.getBanList(BanList.Type.NAME);
        return banList.isBanned(player.getName());
    }
}