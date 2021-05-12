package ru.leocraft.masterchat.masterchat.messages;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ScopedComponent;
import ru.leocraft.masterchat.masterchat.settings.Settings;
import ru.leocraft.masterchat.masterchat.settings.properties.MessageSettings;
import ru.leocraft.masterchat.masterchat.utils.ConsoleLogger;

import java.util.Collections;

public class TemplateMessage {
    public static SimpleMessage REMOVED_MESSAGE;
    public static SimpleMessage NO_ONE_SEEN_MESSAGE;
    public static SimpleMessage NO_PERMISSION;
    public static SimpleMessage NO_MONEY;
    public static SimpleMessage UNKNOWN_COMMAND;
    public static SimpleMessage INVALID_COMMAND_USAGE;
    public static SimpleMessage CHANNEL_CHANGED;
    public static SimpleMessage UNKNOWN_CHANNEL;
    public static SimpleMessage CHANNELS_LIST;
    public static SimpleMessage PLAYER_JOIN;
    public static SimpleMessage PLAYER_QUIT;
    public static SimpleMessage CLEAR_CHAT;
    public static Component NEW_LINE_100 = Collections.nCopies(100, Component.newline()).stream().reduce(Component.empty(), ScopedComponent::append);


    public TemplateMessage Instance;

    public TemplateMessage() {
        Instance = this;

        REMOVED_MESSAGE = new SimpleMessage(Settings.getProperty(MessageSettings.REMOVED_MESSAGE));
        NO_ONE_SEEN_MESSAGE = new SimpleMessage(Settings.getProperty(MessageSettings.NO_ONE_SEEN_MESSAGE));
        NO_PERMISSION = new SimpleMessage(Settings.getProperty(MessageSettings.NO_PERMISSION));
        NO_MONEY = new SimpleMessage(Settings.getProperty(MessageSettings.NO_MONEY));
        UNKNOWN_COMMAND = new SimpleMessage(Settings.getProperty(MessageSettings.UNKNOWN_COMMAND));
        INVALID_COMMAND_USAGE = new SimpleMessage(Settings.getProperty(MessageSettings.INVALID_COMMAND_USAGE));
        CHANNEL_CHANGED = new SimpleMessage(Settings.getProperty(MessageSettings.CHANNEL_CHANGED));
        UNKNOWN_CHANNEL = new SimpleMessage(Settings.getProperty(MessageSettings.UNKNOWN_CHANNEL));
        CHANNELS_LIST = new SimpleMessage(Settings.getProperty(MessageSettings.CHANNELS_LIST));
        PLAYER_JOIN = new SimpleMessage(Settings.getProperty(MessageSettings.PLAYER_JOIN_MESSAGE));
        PLAYER_QUIT = new SimpleMessage(Settings.getProperty(MessageSettings.PLAYER_QUIT_MESSAGE));
        CLEAR_CHAT = new SimpleMessage(Settings.getProperty(MessageSettings.CLEAR_CHAT));

        ConsoleLogger.Instance.debug(this.getClass().getName()  + " module loaded");
    }
}
