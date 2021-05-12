package ru.leocraft.masterchat.masterchat.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import ru.leocraft.masterchat.masterchat.utils.ConsoleLogger;
import ru.leocraft.masterchat.masterchat.MasterChat;

import java.util.*;
import java.util.stream.Collectors;

public class MasterChatCommand extends Command {
    public static MasterChatCommand Instance;
    private final Map<String, Command> subCommands;

    public MasterChatCommand() {
        super("MasterChat", "Main command of MasterChatPlugin", "/MasterChat <sub-command>", new ArrayList<>());
        Instance = this;

        subCommands = new HashMap<>();
        MasterChat.registerCommand(this);

        ConsoleLogger.Instance.debug("Command registered: " + this.getName());
    }

    public void addSubCommand(Command command) {
        ConsoleLogger.Instance.debug("Added sub command '" + command.getName() + "' to '" + this.getName() + "' command");

        subCommands.put(command.getName(), command);
        command.getAliases().forEach(alias -> subCommands.put(alias, command));
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        ConsoleLogger.Instance.debug("Command executed: " + alias + " " + Arrays.toString(args));

        if (args.length == 0 || !subCommands.containsKey(args[0])) {
            sender.sendMessage("Help message");
            return true;
        }

        return subCommands.get(args[0]).execute(sender, args[0], Arrays.copyOfRange(args, 1, args.length));
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
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
