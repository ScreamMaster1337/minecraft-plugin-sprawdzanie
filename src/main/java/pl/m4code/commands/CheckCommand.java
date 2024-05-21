package pl.m4code.commands;

import com.google.common.collect.ImmutableMultimap;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import pl.m4code.Main;
import pl.m4code.commands.api.CommandAPI;
import pl.m4code.utils.TextUtil;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckCommand extends CommandAPI implements Listener {

    private final Map<Player, Integer> titleTasks = new HashMap<>();

    public CheckCommand() {
        super(
                "check",
                "check",
                "",
                "/check sprawdz <zacznij/czysty/cheater/brakwspolpracy> <nick>",
                List.of("sprawdz", "check")
        );
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            TextUtil.sendMessage(sender, "&cPodana komenda jest dostępna tylko dla graczy!");
            return;
        }

        if (args.length >= 2 && args[0].equalsIgnoreCase("sprawdz")) {
            String action = args[1].toLowerCase();

            switch (action) {
                case "zacznij":
                    if (args.length >= 3) {
                        Player target = Bukkit.getPlayer(args[2]);
                        if (target == null) {
                            getMessageConfiguration()
                                    .getIncorrectPlayer()
                                    .addPlaceholder(ImmutableMultimap.of("[PLAYER]", args[2]))
                                    .send(player);
                            return;
                        }

                        startChecking(player, target);
                    } else {
                        sendUsage(player);
                    }
                    break;
                case "czysty":
                case "cheater":
                case "brakwspolpracy":
                    if (args.length >= 3) {
                        Player target = Bukkit.getPlayer(args[2]);
                        if (target == null) {
                            getMessageConfiguration()
                                    .getIncorrectPlayer()
                                    .addPlaceholder(ImmutableMultimap.of("[PLAYER]", args[2]))
                                    .send(player);
                            return;
                        }

                        if (!Main.sprawdzani.contains(target)) {
                            player.sendMessage(TextUtil.fixColor("&cGracz " + target.getName() + " nie jest aktualnie sprawdzany!"));
                            return;
                        }

                        switch (action) {
                            case "czysty":
                                teleportAndNotify(player, target, "&aGracz jest czysty!");
                                teleportAndNotify(target, target, "&aOkazałeś się czysty! &7Życzymy dalszej miłej gry");
                                break;
                            case "cheater":
                                banAndNotify(player, target, "&cGracz jest cheaterem! Został zbanowany na 3 dni.");
                                break;
                            case "brakwspolpracy":
                                banAndNotify(player, target, "&eGracz odmówił współpracy z administracją. Został zbanowany na 2 dni.");
                                break;
                        }
                    } else {
                        sendUsage(player);
                    }
                    break;
                default:
                    sendUsage(player);
            }
        } else {
            sendUsage(player);
        }
    }

    private void startChecking(Player player, Player target) {
        target.sendTitle(TextUtil.fixColor("&#fb0000&lJ&#fb0909&lE&#fb1313&lS&#fb1c1c&lT&#fc2525&lE&#fc2f2f&lŚ &#fc3838&lS&#fc4141&lP&#fc4b4b&lR&#fc5454&lA&#fc5d5d&lW&#fc6767&lD&#fd7070&lZ&#fd7979&lA&#fd8383&lN&#fd8c8c&lY"), TextUtil.fixColor("&eStosuj się do poleceń administratora!"));
        Main.sprawdzani.add(target);

        double x = 0.5;
        double y = 50.0;
        double z = 78.5;
        float fixedYaw = 180.0f;

        World world = Bukkit.getServer().getWorld("Requlogia");
        Location checkLocation = new Location(world, x, y, z, fixedYaw, 0);
        player.teleport(checkLocation);
        target.teleport(checkLocation);

        int taskId = Bukkit.getScheduler().runTaskTimer(Main.getInstance(), () -> updateTitle(target), 0, 20).getTaskId();
        titleTasks.put(player, taskId);

        player.sendMessage(TextUtil.fixColor("&aRozpocząłeś sprawdzanie gracza " + target.getName() + "."));
    }

    private void updateTitle(Player target) {
        target.sendTitle(TextUtil.fixColor("&#fb0000&lJ&#fb0909&lE&#fb1313&lS&#fb1c1c&lT&#fc2525&lE&#fc2f2f&lŚ &#fc3838&lS&#fc4141&lP&#fc4b4b&lR&#fc5454&lA&#fc5d5d&lW&#fc6767&lD&#fd7070&lZ&#fd7979&lA&#fd8383&lN&#fd8c8c&lY"), TextUtil.fixColor("&eStosuj się do poleceń administratora!"), 0, 20, 0);
    }

    private void teleportAndNotify(Player player, Player target, String message) {

        double x = 0.5;
        double y = 54.0;
        double z = 30.5;
        float fixedYaw = 180.0f;

        World world = Bukkit.getServer().getWorld("Requlogia");
        Location czystyLocation = new Location(world, x, y, z, fixedYaw, 0);

        target.teleport(czystyLocation);
        player.teleport(czystyLocation);
        Main.sprawdzani.remove(target);
        player.sendMessage(TextUtil.fixColor(message));

        cancelTitleTask(player);
    }

    private void banAndNotify(Player player, Player target, String message) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tempban " + target.getName() + " 3d");
        player.sendMessage(TextUtil.fixColor(message));

        cancelTitleTask(player);
    }

    private void cancelTitleTask(Player player) {
        Integer taskId = titleTasks.get(player);
        if (taskId != null) {
            Bukkit.getScheduler().cancelTask(taskId);
            titleTasks.remove(player);
        }
    }

    @Override
    public List<String> tab(Player player, String[] args) {
        if (args.length == 1) {
            return List.of("sprawdz");
        } else if (args.length == 2 && "sprawdz".startsWith(args[1].toLowerCase())) {
            return List.of("zacznij", "czysty", "cheater", "brakwspolpracy");
        } else if (args.length == 3) return Bukkit.getOnlinePlayers().stream().map(Player::getName).toList();

        return Collections.emptyList();
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        handleLogoutDuringCheck(player);
    }

    private void handleLogoutDuringCheck(Player target) {
        if (Main.sprawdzani.contains(target)) {
            Bukkit.broadcastMessage(TextUtil.fixColor("&cGracz " + target.getName() + " wylogował się podczas sprawdzania!"));
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tempban " + target.getName() + " 3d");
            Main.sprawdzani.remove(target);
        }
    }
}