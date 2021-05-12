package ru.leocraft.masterchat.masterchat.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.leocraft.masterchat.masterchat.MasterChat;
import ru.leocraft.masterchat.masterchat.messages.MessageSender;
import ru.leocraft.masterchat.masterchat.messages.TemplateMessage;
import ru.leocraft.masterchat.masterchat.utils.ConsoleLogger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClearChatCommand extends Command {
    public ClearChatCommand() {
        super("clear");

        MasterChatCommand.Instance.addSubCommand(this);
        MasterChat.registerCommand(this);

        ConsoleLogger.Instance.debug("Command registered: " + this.getName());
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        ConsoleLogger.Instance.debug("Command executed: " + alias + " " + Arrays.toString(args));

        MessageSender.broadcastSystemMessage(TemplateMessage.NEW_LINE_100);
        if (sender instanceof Player)
            MessageSender.broadcastSystemMessage((Player) sender, TemplateMessage.CLEAR_CHAT, "");
        else
            // Message to console sender
            sender.sendMessage("Chat was cleared");

        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        ConsoleLogger.Instance.debug("Command complete: " + alias + " " + Arrays.toString(args));

        return new ArrayList<>();
    }
}
