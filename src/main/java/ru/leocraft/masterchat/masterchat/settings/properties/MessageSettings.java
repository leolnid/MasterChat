package ru.leocraft.masterchat.masterchat.settings.properties;

import ch.jalu.configme.Comment;
import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.properties.Property;

import java.util.List;

import static ch.jalu.configme.properties.PropertyInitializer.newListProperty;
import static ch.jalu.configme.properties.PropertyInitializer.newProperty;

public class MessageSettings implements SettingsHolder {
    @Comment({
            "",
            "",
            "",
            "#########################################################################################################",
            "#",
            "# This is system messages",
            "# Don't remove it",
            "# You can change it, if you want",
            "#",
            "#########################################################################################################"
    })
    public static final Property<List<String>> REMOVED_MESSAGE = newListProperty("message.removed-message", "prefix", "action-kick-player", "action-ban-player", "space", "removed-message", "removed-message-admin");
    public static final Property<List<String>> NO_ONE_SEEN_MESSAGE = newListProperty("message.no-one-seen", "prefix", "space", "no-one-seen");
    public static final Property<List<String>> NO_PERMISSION = newListProperty("message.no-permission", "prefix", "space", "no-permission");
    public static final Property<List<String>> NO_MONEY = newListProperty("message.no-money", "prefix", "space", "no-money");
    public static final Property<List<String>> UNKNOWN_COMMAND = newListProperty("message.unknown-command", "prefix", "space", "unknown-command");
    public static final Property<List<String>> INVALID_COMMAND_USAGE = newListProperty("message.invalid-command-usage", "prefix", "space", "invalid-command-usage");
    public static final Property<List<String>> CHANNEL_CHANGED = newListProperty("message.channel-changed", "prefix", "space", "channel-changed");
    public static final Property<List<String>> UNKNOWN_CHANNEL = newListProperty("message.unknown-channel", "prefix", "space", "unknown-channel");;
    public static final Property<List<String>> CHANNELS_LIST = newListProperty("message.channel-list", "prefix", "space", "channel-list");;
    public static final Property<List<String>> PLAYER_JOIN_MESSAGE = newListProperty("message.player-join-message", "prefix", "action-kick-player", "action-ban-player", "space", "player-join-message");;
    public static final Property<List<String>> PLAYER_QUIT_MESSAGE = newListProperty("message.player-quit-message", "prefix", "action-ban-player", "space", "player-quit-message");;
}
