package ru.leocraft.masterchat.masterchat.messages;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.leocraft.masterchat.masterchat.MasterChat;
import ru.leocraft.masterchat.masterchat.utils.ConsoleLogger;

import java.util.function.Function;

public class MessageSender {
    private static final Integer SYSTEM_MESSAGE_HASH = 0;
    private static final String SYSTEM_MESSAGE_SENDER = "System";

    public static void send(Player receiver, Integer hash, @NotNull String sender, Component originalMassage) {
        ConsoleLogger.Instance.debug("Send message with hash '" + hash + "' to " + receiver.getName());
        MasterChat.History.registerNewMessage(receiver, hash, sender, originalMassage);
        receiver.sendMessage(originalMassage);
    }

    public static void sendSystemMessage(Player receiver, SimpleMessage simpleMessage, String content) {
        ConsoleLogger.Instance.debug("Send system message to " + receiver.getName());
        send(receiver, SYSTEM_MESSAGE_HASH, SYSTEM_MESSAGE_SENDER, simpleMessage.build(receiver, content, receiver));
    }

    public static void broadcastSystemMessage(Player sender, SimpleMessage simpleMessage, String context) {
        ConsoleLogger.Instance.debug("Start broadcasting message.");
        Function<Player, Component> preparedMessage = simpleMessage.build(sender, context).apply(Type.PLAYER);

        MasterChat.Instance.getServer().getOnlinePlayers().forEach(receiver -> send(
                receiver,
                SYSTEM_MESSAGE_HASH,
                SYSTEM_MESSAGE_SENDER,
                preparedMessage.apply(receiver)));
    }
}
