package ru.leocraft.masterchat.masterchat.utils;

import ru.leocraft.masterchat.masterchat.MasterChat;
import ru.leocraft.masterchat.masterchat.settings.Settings;
import ru.leocraft.masterchat.masterchat.settings.properties.PluginSettings;

public class ConsoleLogger {
    public static ConsoleLogger Instance;

    public ConsoleLogger() {
        Instance = this;
        ConsoleLogger.Instance.debug("Logger module loaded");
    }

    public void debug(String message) {
        if (Settings.getProperty(PluginSettings.DEBUG))
            this.log("MC|DEBUG", message);
    }

    public void log(String message) {
        this.log(MasterChat.Instance.getDescription().getPrefix(), message);
    }

    public void log(String prefix, String message) {
        MasterChat.Instance.getServer().getConsoleSender().sendMessage(
                "[§a" + prefix + "§r] " + message
        );
    }
}
