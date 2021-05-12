package ru.leocraft.masterchat.masterchat.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.leocraft.masterchat.masterchat.utils.ConsoleLogger;
import ru.leocraft.masterchat.masterchat.MasterChat;
import ru.leocraft.masterchat.masterchat.messages.MessageSender;
import ru.leocraft.masterchat.masterchat.messages.TemplateMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class RemoveCommand extends Command {
    public static RemoveCommand Instance;

    public RemoveCommand() {
        super("remove");
        Instance = this;

        MasterChatCommand.Instance.addSubCommand(this);
        MasterChat.registerCommand(this);

        ConsoleLogger.Instance.debug("Command registered: " + this.getName());
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        ConsoleLogger.Instance.debug("Command executed: " + alias + " " + Arrays.toString(args));

        if (args.length != 1 || !args[0].matches("-?\\d+")) {
            if (sender instanceof Player) {
                MessageSender.sendSystemMessage((Player) sender, TemplateMessage.INVALID_COMMAND_USAGE, "Invalid command argument: " + args[0]);
            } else {
                sender.sendMessage("Invalid command argument: " + args[0]);
            }
            return false;
        }

        MasterChat.History.removeMessageAndResend(Integer.parseInt(args[0]));
        MasterChat.Instance.sendToSocket(sender.getName(), "/masterchat remove " + args[0]);
        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        ConsoleLogger.Instance.debug("Command complete: " + alias + " " + Arrays.toString(args));

        return new ArrayList<>();
    }
}
