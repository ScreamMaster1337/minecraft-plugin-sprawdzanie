package pl.m4code.commands.api;

import com.google.common.collect.ImmutableMultimap;
import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.m4code.Main;
import pl.m4code.configuration.Configuration;
import pl.m4code.configuration.MessageConfiguration;
import pl.m4code.utils.TextUtil;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class CommandAPI extends BukkitCommand {
    private final String permission;

    private final Main instance = Main.getInstance();
    private final Configuration configuration = instance.getConfiguration();
    private final MessageConfiguration messageConfiguration = instance.getMessageConfiguration();

    @SneakyThrows
    public CommandAPI(@NotNull String name, String permission, @NotNull String description, @NotNull String usageMessage, @NotNull List<String> aliases) {
        super(name, description, usageMessage, aliases);
        this.permission = permission;
        this.setPermission(permission);
    }

    @Override
    public boolean execute(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] strings) {
        if(permission == null || permission.isEmpty() || commandSender.hasPermission(permission)) {
            try {
                execute(commandSender, strings);
            } catch (Exception e) {
                var message = e.getMessage();

                if(commandSender.hasPermission("clash")) TextUtil.sendMessage(commandSender, "&4" + message);
                else TextUtil.sendMessage(commandSender, "&cWystąpił błąd poczas wykonywania polecenia!");
                e.printStackTrace();
                return false;
            }
        } else {
            messageConfiguration
                    .getNoPermission()
                    .send((Player) commandSender);
        }
        return false;
    }

    @Override
    public @NonNull List<String> tabComplete(@NonNull CommandSender sender, @NonNull String label, @NotNull String[] args) {
        List<String> completions = tab((Player) sender, args);
        if (completions == null) return new ArrayList<>();
        else return completions;
    }

    public void sendUsage(Player player) {
        messageConfiguration.getCorrectUsage()
                .addPlaceholder(ImmutableMultimap.of("[USAGE]", getUsage()))
                .send(player);
    }

    public abstract void execute(CommandSender sender, String[] args) throws IOException;
    public abstract List<String> tab(@NonNull Player player, @NonNull String[] args);

}