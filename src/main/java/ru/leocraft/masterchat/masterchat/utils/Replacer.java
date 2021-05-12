package ru.leocraft.masterchat.masterchat.utils;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;
import ru.leocraft.masterchat.masterchat.MasterChat;
import ru.leocraft.masterchat.masterchat.settings.Settings;
import ru.leocraft.masterchat.masterchat.settings.properties.PluginSettings;

import java.util.Map;
import java.util.function.BiFunction;

public class Replacer {
    private static BiFunction<Player, String, String> setPlaceholders = (player, source) -> source;

    public Replacer() {
        boolean isEnabled = MasterChat.Instance.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI");
        boolean isConfig = Settings.getProperty(PluginSettings.USE_PLACEHOLDER_API);

        if (isConfig && isEnabled)
            setPlaceholders = PlaceholderAPI::setPlaceholders;
    }

    public static Map<String, String> get(Player sender, String message) {
        return Map.of(
                "%player-name%", sender.getName(),
                "%content%", message,
                "%hash%", MessageHash.getStrHash(sender, message)
        );
    }

    public static String setPlaceholders(String source, Player sender, String message) {
        String formatted = setPlaceholders.apply(sender, source);

        for (Map.Entry<String, String> e : Replacer.get(sender, message).entrySet()) {
            formatted = formatted.replace(e.getKey(), e.getValue());
        }

        return formatted;
    }
}
