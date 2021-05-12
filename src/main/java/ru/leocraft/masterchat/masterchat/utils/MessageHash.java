package ru.leocraft.masterchat.masterchat.utils;

import org.bukkit.entity.Player;

public class MessageHash {
    public static int getHash(Player player, String message) {
        return getHash(player.getName(), message);
    }

    public static String getStrHash(Player player, String message) {
        return getStrHash(player.getName(), message);
    }

    public static int getHash(String playerName, String message) {
        return (playerName + message).hashCode();
    }

    public static String getStrHash(String playerName, String message) {
        return String.valueOf(getHash(playerName, message));
    }
}
