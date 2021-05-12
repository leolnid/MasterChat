package ru.leocraft.masterchat.masterchat.settings;

import ch.jalu.configme.SettingsManager;
import ch.jalu.configme.SettingsManagerBuilder;
import ch.jalu.configme.properties.Property;
import ru.leocraft.masterchat.masterchat.utils.ConsoleLogger;
import ru.leocraft.masterchat.masterchat.MasterChat;
import ru.leocraft.masterchat.masterchat.settings.properties.*;

import java.io.File;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Settings {
    public static Settings Instance;

    private final SettingsManager settings;
    private final SettingsManager tags;
    private final Map<String, SettingsManager> chats;

    public Settings() {
        Instance = this;

        settings = SettingsManagerBuilder
                .withYamlFile(Paths.get(
                        MasterChat.Instance.getDataFolder()
                                + File.separator
                                + "config.yml"))
                .configurationData(
                        PluginSettings.class,
                        MessageSettings.class)
                .useDefaultMigrationService()
                .create();

        tags = SettingsManagerBuilder
                .withYamlFile(Paths.get(
                        MasterChat.Instance.getDataFolder()
                                + File.separator
                                + "tags.yml"))
                .configurationData(TagsSettings.class)
                .useDefaultMigrationService()
                .create();

        chats = new HashMap<>();
        File chatsFolder = new File(MasterChat.Instance.getDataFolder() + File.separator + "chats");
        String[] chatsNames = chatsFolder.list();
        if (!chatsFolder.exists() || !chatsFolder.isDirectory() || chatsNames == null) {
            if (!chatsFolder.mkdir()) {
                MasterChat.Instance.getLogger().warning("Can't create config folder!");
                MasterChat.Instance.getPluginLoader().disablePlugin(MasterChat.Instance);
            }

            chatsNames = new String[]{"global.yml"};
        }

        for (String chatName : chatsNames) {
            chats.put(chatName.substring(0, chatName.lastIndexOf('.')), SettingsManagerBuilder
                    .withYamlFile(Paths.get(
                            MasterChat.Instance.getDataFolder()
                                    + File.separator
                                    + "chats"
                                    + File.separator
                                    + chatName))
                    .configurationData(ChannelSettings.class)
                    .useDefaultMigrationService()
                    .create());
        }

        MasterChat.Instance.chats = Arrays.stream(chatsNames).map(v -> v.substring(0, v.lastIndexOf('.'))).collect(Collectors.toList());
    }

    public static <T> T getProperty(Property<T> property) {
        return Settings.Instance.settings.getProperty(property);
    }

    public static <T> T getTagProperty(Property<T> property) {
        return Settings.Instance.tags.getProperty(property);
    }

    public static <T> T getChatProperty(String chatName, Property<T> property) {
        return Settings.Instance.getChatSettings(chatName).getProperty(property);
    }

    private SettingsManager getChatSettings(String name) {
        if (!chats.containsKey(name))
            ConsoleLogger.Instance.log("Can't get chat with name: " + name);
        return chats.get(name);
    }
}
