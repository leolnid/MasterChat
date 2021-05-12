package ru.leocraft.masterchat.masterchat.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import ru.leocraft.masterchat.masterchat.MasterChat;
import ru.leocraft.masterchat.masterchat.settings.Settings;
import ru.leocraft.masterchat.masterchat.messages.SimpleTag;
import ru.leocraft.masterchat.masterchat.messages.Type;
import ru.leocraft.masterchat.masterchat.settings.properties.MessageSettings;
import ru.leocraft.masterchat.masterchat.settings.properties.TagsSettings;
import ru.leocraft.masterchat.masterchat.structures.LimitedLinkedList;
import ru.leocraft.masterchat.masterchat.structures.Triple;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class History implements Listener {
    public Map<Player, LimitedLinkedList<Triple<Integer, String, Component>>> history;

    public History() {
        history = new HashMap<>();
        ConsoleLogger.Instance.debug("History module loaded");
    }

    private void addPlayer(Player player) {
        ConsoleLogger.Instance.debug("Added new player " + player.getName() + " to history module");
        this.history.put(player, new LimitedLinkedList<>(60));
    }

    private void removePlayer(Player player) {
        ConsoleLogger.Instance.debug("Removed player " + player.getName() + " from history module");
        this.history.remove(player);
    }

    public void onPlayerJoin(Player player) {
        addPlayer(player);
    }

    public void onPlayerQuit(Player player) {
        removePlayer(player);
    }

    public void registerNewMessage(Player receiver, Integer messageHash, String originalMessageSender, Component originalMessage) {
        Triple<Integer, String, Component> historyElement = new Triple<>(messageHash, originalMessageSender, originalMessage);
        this.history.get(receiver).add(historyElement);
    }

    public void removeMessageAndResend(int removedMessageHash) {
        ConsoleLogger.Instance.debug("Start history cleaning. Hash: " + removedMessageHash);
        List<SimpleTag> removeMessageTags = Settings.getProperty(MessageSettings.REMOVED_MESSAGE)
                .stream()
                .map(v -> Settings.getProperty(TagsSettings.TAGS).get(v))
                .collect(Collectors.toList());

        for (Map.Entry<Player, LimitedLinkedList<Triple<Integer, String, Component>>> onePlayerHistory : this.history.entrySet()) {
            Player originalMessageReceiver = onePlayerHistory.getKey();
            ConsoleLogger.Instance.debug("Start history cleaning. Player: " + originalMessageReceiver.getName());
            for (int i = 0; i < 101; i++) {
                originalMessageReceiver.sendMessage(Component.newline());
            }

            for (Triple<Integer, String, Component> message : onePlayerHistory.getValue()) {
                if (message.getV1() != removedMessageHash) {
                    originalMessageReceiver.sendMessage(message.getV3());
                    continue;
                }

                Player originalMessageSender = MasterChat.Instance.getServer().getPlayer(message.getV2());
                Component removedMessage = removeMessageTags
                        .stream()
                        .map(v -> v
                                .prepareForSending(originalMessageSender, LegacyComponentSerializer.legacySection().serialize(message.getV3()))
                                .apply(Type.PLAYER)
                                .apply(originalMessageReceiver))
                        .reduce(Component.empty(), Component::append);
                originalMessageReceiver.sendMessage(removedMessage);
                message.setV3(removedMessage);
            }
        }
        ConsoleLogger.Instance.debug("Success history cleaning");
    }
}
