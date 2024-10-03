package pl.m4code;

import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.java.JavaPlugin;
import pl.m4code.commands.CheckCommand;
import pl.m4code.commands.PrzyznajesieCommand;
import pl.m4code.commands.api.CommandAPI;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
public final class Main extends JavaPlugin {
    @Getter private static Main instance;
    private final Set<UUID> beingChecked = new HashSet<>();

    public static Main getInstance() {
        return instance;
    }

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();

        try {
            registerCommands();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        registerListeners();
        registerTasks();
    }

    @SneakyThrows
    private void registerCommands() throws NoSuchFieldException, IllegalAccessException {
        final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
        bukkitCommandMap.setAccessible(true);
        final CommandMap commandMap = (CommandMap) bukkitCommandMap.get(getServer());
        for (CommandAPI commands : List.of(
                new CheckCommand(beingChecked),
                new PrzyznajesieCommand(beingChecked)
        )) {
            commandMap.register(commands.getName(), commands);
        }
    }

    private void registerListeners() {
    }

    private void registerTasks() {
    }
}