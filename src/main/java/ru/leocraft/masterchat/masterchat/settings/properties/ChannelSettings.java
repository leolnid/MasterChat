package ru.leocraft.masterchat.masterchat.settings.properties;

import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.properties.Property;

import java.util.List;

import static ch.jalu.configme.properties.PropertyInitializer.newListProperty;
import static ch.jalu.configme.properties.PropertyInitializer.newProperty;

public class ChannelSettings implements SettingsHolder {
    public static final Property<String> NAME = newProperty("name", "global");
    public static final Property<List<String>> TAGS = newListProperty("tags", "global-chat", "action-remove-message", "space", "player", "space", "content");
    public static final Property<String> PERMISSION_TO_READ = newProperty("permission-to-read", "");
    public static final Property<String> PERMISSION_TO_WRITE = newProperty("permission-to-write", "");
    public static final Property<List<String>> ALIAS = newListProperty("alias", "!");
    public static final Property<Double> COST = newProperty("cost", 0f);
    public static final Property<Integer> DISTANCE = newProperty("distance", 0);
    public static final Property<Boolean> SEND_TO_SOCKET = newProperty("send-to-socket", false);

}
