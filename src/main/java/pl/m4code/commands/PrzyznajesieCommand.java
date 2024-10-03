package pl.m4code.commands;

import lombok.NonNull;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import pl.m4code.Main;
import pl.m4code.commands.api.CommandAPI;
import pl.m4code.utils.TextUtil;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class PrzyznajesieCommand extends CommandAPI implements Listener {
    private final Set<UUID> beingChecked;

    public PrzyznajesieCommand(Set<UUID> beingChecked) {
        super(
                "przyznajesie",
                "",
                "",
                "/przyznajesie",
                List.of("")
        );
        this.beingChecked = beingChecked;
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            TextUtil.sendMessage(sender, getConfigMessage("messages.console_cannot_use"));
            return;
        }

        Player player = (Player) sender;
        if (!beingChecked.contains(player.getUniqueId())) {
            TextUtil.sendMessage(sender, getConfigMessage("messages.player_not_being_checked"));
            return;
        }

        beingChecked.remove(player.getUniqueId());
        String przyznajesieCommand = Main.getInstance().getConfig().getString("console_commands.przyznajesie").replace("%player%", player.getName());
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), przyznajesieCommand);
        player.kickPlayer(getConfigMessage("messages.przyznajesie_ban_reason"));
        Bukkit.broadcastMessage(TextUtil.fixColor(getConfigMessage("messages.player_przyznajesie").replace("%player%", player.getName())));
    }

    private String getConfigMessage(String path) {
        FileConfiguration config = Main.getInstance().getConfig();
        String message = config.getString(path);
        return message != null ? message : "Missing message for " + path;
    }

    @Override
    public List<String> tab(@NonNull Player player, @NotNull @NonNull String[] args) {
        return List.of();
    }
}