package ru.leocraft.masterchat.masterchat.channels;

import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import ru.leocraft.masterchat.masterchat.MasterChat;
import ru.leocraft.masterchat.masterchat.commands.ChannelCommand;
import ru.leocraft.masterchat.masterchat.commands.CommandsManager;
import ru.leocraft.masterchat.masterchat.messages.MessageSender;
import ru.leocraft.masterchat.masterchat.messages.SimpleMessage;
import ru.leocraft.masterchat.masterchat.messages.TemplateMessage;
import ru.leocraft.masterchat.masterchat.messages.Type;
import ru.leocraft.masterchat.masterchat.settings.Settings;
import ru.leocraft.masterchat.masterchat.settings.properties.ChannelSettings;
import ru.leocraft.masterchat.masterchat.structures.QuadFunction;
import ru.leocraft.masterchat.masterchat.utils.MessageHash;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Channel {
    private final String name;
    private final SimpleMessage simpleMessage;
    private final Integer distance;
    private final boolean sendToSocket;
    private final Function<Player, Function<Player, Boolean>> filterReceiver;
    private final List<String> alias;
    private QuadFunction<Player, Channel, Component, Integer> sendToSocketAction = (player, channel, component, hash) -> {
    };
    private Function<Player, Boolean> checkWritePermission = p -> true;
    private Function<Player, Boolean> checkAndTakeMoney = p -> true;

    public Channel(String chatSettingsName) {
        this.name = Settings.getChatProperty(chatSettingsName, ChannelSettings.NAME);
        this.simpleMessage = new SimpleMessage(Settings.getChatProperty(chatSettingsName, ChannelSettings.TAGS));

        alias = Settings.getChatProperty(chatSettingsName, ChannelSettings.ALIAS);
        Command command = new ChannelCommand(this);

        String permissionToWrite = Settings.getChatProperty(chatSettingsName, ChannelSettings.PERMISSION_TO_WRITE);
        if (!permissionToWrite.isEmpty()) {
            command.setPermission(permissionToWrite);
            checkWritePermission = sender -> MasterChat.getPermission().has(sender, permissionToWrite);
        }

        CommandsManager.Instance.registerCommand(command);

        Double cost = Settings.getChatProperty(chatSettingsName, ChannelSettings.COST);
        String permissionToRead = Settings.getChatProperty(chatSettingsName, ChannelSettings.PERMISSION_TO_READ);
        distance = Settings.getChatProperty(chatSettingsName, ChannelSettings.DISTANCE);
        sendToSocket = Settings.getChatProperty(chatSettingsName, ChannelSettings.SEND_TO_SOCKET);
        if (sendToSocket)
            sendToSocketAction = MasterChat.Instance::sendToSocket;

        if (cost != 0f)
            checkAndTakeMoney = player -> MasterChat.getEconomy().has(player, cost)
                    && MasterChat.getEconomy().withdrawPlayer(player, cost).transactionSuccess();

        if (!permissionToRead.isEmpty())
            if (distance != 0)
                filterReceiver = sender -> receiver ->
                        MasterChat.getPermission().has(receiver, permissionToRead)
                                && checkDistance(sender, receiver);
            else
                filterReceiver = sender -> receiver -> MasterChat.getPermission().has(receiver, permissionToRead);
        else if (distance != 0)
            filterReceiver = sender -> receiver -> checkDistance(sender, receiver);
        else
            filterReceiver = sender -> receiver -> true;
    }

    private boolean checkDistance(Player sender, Player receiver) {
        if (sender.getWorld() != receiver.getWorld())
            return false;

        return sender.getLocation().distance(receiver.getLocation()) < distance;
    }

    public void sendMessage(Player sender, String message) {
        if (!checkWritePermission.apply(sender)) {
            MessageSender.sendSystemMessage(sender, TemplateMessage.NO_PERMISSION, "");
            return;
        }

        if (!checkAndTakeMoney.apply(sender)) {
            MessageSender.sendSystemMessage(sender, TemplateMessage.NO_MONEY, "");
            return;
        }

        Function<Type, Function<Player, Component>> typePreparedMessage = simpleMessage.build(sender, message);

        // Send message to console and to socket
        Integer hash = MessageHash.getHash(sender, message);
        MasterChat.Instance.getServer().getConsoleSender().sendMessage(typePreparedMessage.apply(Type.CONSOLE).apply(sender));
        if (sendToSocket)
            sendToSocketAction.apply(sender, this, typePreparedMessage.apply(Type.DEFAULT_PLAYER).apply(sender), hash);

        // Prepare receiver filter function
        Function<Player, Component> preparedMessage = typePreparedMessage.apply(Type.PLAYER);
        Function<Player, Boolean> filter = filterReceiver.apply(sender);

        // Get receivers list
        List<Player> list = MasterChat.Instance.getServer().getOnlinePlayers().stream().filter(filter::apply).collect(Collectors.toList());
        if (list.size() == 1 && !sendToSocket)
            MessageSender.sendSystemMessage(sender, TemplateMessage.NO_ONE_SEEN_MESSAGE, "");

        list.forEach(receiver -> MessageSender.send(receiver, hash, sender.getName(), preparedMessage.apply(receiver)));
    }

    public String getName() {
        return name;
    }

    public List<String> getAlias() {
        return alias;
    }
}
