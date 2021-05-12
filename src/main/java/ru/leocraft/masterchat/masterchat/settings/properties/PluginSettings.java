package ru.leocraft.masterchat.masterchat.settings.properties;

import ch.jalu.configme.Comment;
import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.properties.Property;
import ch.jalu.configme.properties.PropertyBuilder;
import ch.jalu.configme.properties.types.PrimitivePropertyType;

import java.util.Map;

import static ch.jalu.configme.properties.PropertyInitializer.newProperty;

public class PluginSettings implements SettingsHolder {
    @Comment("Debug includes more messages to console")
    public static final Property<Boolean> DEBUG = newProperty("plugin.debug", false);

    @Comment("Should we use PlaceholderAPI plugin?")
    public static final Property<Boolean> USE_PLACEHOLDER_API = newProperty("plugin.use-placeholder-api", false);

    @Comment({"Address where socket listen to messages and send it", "Example: ws://127.0.0.1:3000", "Example: wss://development.socket.ru/socket"})
    public static final Property<String> SOCKET_URL  = newProperty("plugin.socket-url", "");

    @Comment({"Server name. If empty - server.properties.name will be used", "It will be used as a socket service name"})
    public static final Property<String> SERVER_NAME = newProperty("plugin.server-name", "");

    @Comment("When player change worlds this channel sets as active")
    public static final Property<Map<String, String>> DEFAULT_WORLDS_CHANNELS = new PropertyBuilder.MapPropertyBuilder<>(PrimitivePropertyType.STRING)
            .path("plugin.defaults-channels-per-worlds")
            .defaultEntry("world", "")
            .defaultEntry("world_nether", "")
            .defaultEntry("world_the_end", "")
            .build();

    @Comment("Default channel for joining players")
    public static final Property<String> DEFAULT_CHANNEL = newProperty("plugin.default-channel", "global");
}
