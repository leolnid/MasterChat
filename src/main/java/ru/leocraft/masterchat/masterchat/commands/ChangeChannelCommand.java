package ru.leocraft.masterchat.masterchat.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.leocraft.masterchat.masterchat.MasterChat;
import ru.leocraft.masterchat.masterchat.messages.MessageSender;
import ru.leocraft.masterchat.masterchat.messages.TemplateMessage;
import ru.leocraft.masterchat.masterchat.utils.ConsoleLogger;

import java.util.*;
import java.util.stream.Collectors;

public class ChangeChannelCommand extends Command {
    public static ChangeChannelCommand Instance;
    private final Map<String, ChannelCommand> subCommands;

    public ChangeChannelCommand() {
        super("channel");
        this.setAliases(List.of("ch"));

        Instance = this;

        subCommands = new HashMap<>();

        MasterChatCommand.Instance.addSubCommand(this);
        MasterChat.registerCommand(this);

        ConsoleLogger.Instance.debug("Command registered: " + this.getName());
    }

    public void addChannelCommand(ChannelCommand command) {
        ConsoleLogger.Instance.debug("Added sub command '" + command.getName() + "' to '" + this.getName() + "' command");

        subCommands.put(command.getName(), command);
        command.getAliases().forEach(alias -> subCommands.put(alias, command));
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        ConsoleLogger.Instance.debug("Command executed: " + alias + " " + Arrays.toString(args));

        if (args.length == 0) {
            if (sender instanceof Player) {
                MessageSender.sendSystemMessage((Player) sender, TemplateMessage.CHANNELS_LIST, String.join(", ", subCommands.keySet()));
            } else {
                // Message to console sender
                sender.sendMessage(String.join(", ", subCommands.keySet()));
            }
            return true;
        }

        if (!subCommands.containsKey(args[0])) {
            if (sender instanceof Player) {
                MessageSender.sendSystemMessage((Player) sender, TemplateMessage.UNKNOWN_CHANNEL, "Unknown channel name");
            } else {
                // Message to console sender
                sender.sendMessage("Unknown channel name");
            }
            return false;
        }

        return subCommands.get(args[0]).execute(sender, args[0], Arrays.copyOfRange(args, 1, args.length));
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        ConsoleLogger.Instance.debug("Command complete: " + alias + " " + Arrays.toString(args));

        if (args.length == 0 || args[0].isEmpty())
            return new ArrayList<>(subCommands.keySet());

        if (subCommands.containsKey(args[0]))
            return subCommands.get(args[0]).tabComplete(sender, alias, Arrays.copyOfRange(args, 1, args.length));

        if (args.length == 1)
            return subCommands.keySet()
                    .stream()
                    .filter(v -> v.startsWith(args[0]))
                    .collect(Collectors.toList());

        return new ArrayList<>();
    }
}
