package pl.m4code;

import eu.okaeri.configs.ConfigManager;
import eu.okaeri.configs.yaml.bukkit.YamlBukkitConfigurer;
import eu.okaeri.configs.yaml.bukkit.serdes.SerdesBukkit;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import pl.m4code.commands.*;
import pl.m4code.configuration.Configuration;
import pl.m4code.configuration.MessageConfiguration;
import pl.m4code.notice.NoticeSerializer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Getter
public final class Main extends JavaPlugin {

    @Getter
    private static Main instance;

    private Configuration configuration;
    private MessageConfiguration messageConfiguration;
    public static ArrayList<Player> sprawdzani = new ArrayList<>();
    public boolean whitelist;
    @Getter
    List<String> regions = new ArrayList<>();

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        configuration = ConfigManager.create(Configuration.class, it -> {
            it.withConfigurer(new YamlBukkitConfigurer(), new SerdesBukkit());
            it.withBindFile(this.getDataFolder() + "/configuration.yml");
            it.withRemoveOrphans(true);
            it.saveDefaults();
            it.load(true);
        });
        registerListeners();


        messageConfiguration = ConfigManager.create(MessageConfiguration.class, it -> {
            it.withConfigurer(new YamlBukkitConfigurer(), new SerdesBukkit());
            it.withBindFile(this.getDataFolder() + "/messages.yml");
            it.withSerdesPack(registry -> registry.register(new NoticeSerializer()));
            it.withRemoveOrphans(true);
            it.saveDefaults();
            it.load(true);
        });



        try {
            registerCommands();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    private void registerCommands() throws NoSuchFieldException, IllegalAccessException {
        final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
        bukkitCommandMap.setAccessible(true);
        final CommandMap commandMap = (CommandMap) bukkitCommandMap.get(getServer());
        List.of(
                new CheckCommand()
        ).forEach(commands ->
                commandMap.register("m4code-lobby", commands)
        );
    }

    private void registerListeners() {
    }
}

