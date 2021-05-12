package ru.leocraft.masterchat.masterchat.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.leocraft.masterchat.masterchat.utils.ConsoleLogger;
import ru.leocraft.masterchat.masterchat.MasterChat;
import ru.leocraft.masterchat.masterchat.channels.Channel;
import ru.leocraft.masterchat.masterchat.channels.ChannelManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChannelCommand extends Command {
    private final Channel channel;

    public ChannelCommand(Channel channel) {
        super(channel.getName());
        this.channel = channel;
        this.setAliases(channel.getAlias());

        MasterChatCommand.Instance.addSubCommand(this);
        ChangeChannelCommand.Instance.addChannelCommand(this);

        MasterChat.registerCommand(this);
        ConsoleLogger.Instance.debug("Command registered: " + this.getName());
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        ConsoleLogger.Instance.debug("Command executed: " + alias + " " + Arrays.toString(args));

        if (!(sender instanceof Player)) {
            sender.sendMessage("Can use only player from game");
            return false;
        }
        Player player = (Player) sender;
        ChannelManager.Instance.setPlayerChannel(player, channel);
        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        ConsoleLogger.Instance.debug("Command complete: " + alias + " " + Arrays.toString(args));
        return new ArrayList<>();
    }
}
