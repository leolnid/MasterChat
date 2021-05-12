package ru.leocraft.masterchat.masterchat.settings.properties;

import ch.jalu.configme.Comment;
import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.properties.MapProperty;
import ch.jalu.configme.properties.PropertyBuilder;
import ch.jalu.configme.properties.types.BeanPropertyType;
import net.kyori.adventure.text.event.ClickEvent;
import ru.leocraft.masterchat.masterchat.messages.SimpleTag;

public class TagsSettings implements SettingsHolder {
    @Comment({
            "",
            "",
            "",
            "#########################################################################################################",
            "#",
            "# This is tags, that used in messages",
            "# You can change it or add yours",
            "#",
            "#########################################################################################################"
    })
    public static final MapProperty<SimpleTag> TAGS = new PropertyBuilder.MapPropertyBuilder<>(BeanPropertyType.of(SimpleTag.class))
            .path("tags")
            // =========================================================================================================
            // ====================================          Basic massage             =================================
            // =========================================================================================================
            .defaultEntry("prefix", new SimpleTag("§7[§6MasterChat§7]§r", "Welcome to the server"))
            .defaultEntry("global-chat", new SimpleTag("§7[§6G§7]§r", "Name: %player-name%", ClickEvent.openUrl("www.google.ru")))
            .defaultEntry("local-chat", new SimpleTag("§7[§6L§7]§r", "www.google.ru/%player-name%", ClickEvent.openUrl("www.google.ru/%player-name%")))
            .defaultEntry("player", new SimpleTag("§n%player-name%§r", "", ClickEvent.openUrl("www.google.ru/%player-name%")))
            .defaultEntry("space", new SimpleTag(" ", ""))
            .defaultEntry("content", new SimpleTag("%content%", ""))

            // =========================================================================================================
            // ====================================             Actions                =================================
            // =========================================================================================================
            .defaultEntry("action-remove-message", new SimpleTag("§7[§cx§7]§r", "§7Удалить это сообщение\n§7hash: %hash%", ClickEvent.runCommand("/masterchat:remove %hash%"), "masterchat.message.remove"))
            .defaultEntry("action-kick-player", new SimpleTag("§7[§e>§7]§r", "Выгнать игрока: %player-name%", ClickEvent.runCommand("/kick %player-name%"), "masterchat.kick"))
            .defaultEntry("action-ban-player", new SimpleTag("§7[§c-§7]§r", "Забанить игрока: %player-name%", ClickEvent.runCommand("/ban %player-name%"), "masterchat.ban"))

            // =========================================================================================================
            // ====================================             Messages               =================================
            // =========================================================================================================
            .defaultEntry("removed-message-admin", new SimpleTag("§7This message was removed", "\n §7Данное сообщение было удалено. \n§7------------§7\n%content%", ClickEvent.runCommand(""), "masterchat.message.seen"))
            .defaultEntry("removed-message", new SimpleTag("§7This message was removed", "\n §7Данное сообщение было удалено. \n", ClickEvent.runCommand(""), "-masterchat.message.seen"))
            .defaultEntry("help-channels-global", new SimpleTag("§7This message was removed", "\n §7Данное сообщение было удалено. \n", ClickEvent.runCommand(""), "-masterchat.message.seen"))

            .defaultEntry("no-one-seen", new SimpleTag("§7No one seen this message"))
            .defaultEntry("no-permission", new SimpleTag("§7You haven't permission for this"))
            .defaultEntry("no-money", new SimpleTag("§7You haven't money for this"))
            .defaultEntry("unknown-command", new SimpleTag("§7Unknown command"))
            .defaultEntry("invalid-command-usage", new SimpleTag("§7Invalid command usage"))
            .defaultEntry("channel-changed", new SimpleTag("§7You was moved to %content%"))
            .defaultEntry("unknown-channel", new SimpleTag("§7Unknown channel %content%"))
            .defaultEntry("channel-list", new SimpleTag("§7Available channels: \\n§0.............§7%content%\\n"))
            .defaultEntry("player-join-message", new SimpleTag("§7New player %player-name%"))
            .defaultEntry("player-quit-message", new SimpleTag("§7Player quit %player-name%"))
            .build();
}
