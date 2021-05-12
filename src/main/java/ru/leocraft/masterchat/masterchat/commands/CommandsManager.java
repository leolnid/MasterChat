package ru.leocraft.masterchat.masterchat.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import ru.leocraft.masterchat.masterchat.MasterChat;
import ru.leocraft.masterchat.masterchat.utils.ConsoleLogger;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class CommandsManager {
    public static CommandsManager Instance;
    private final List<Command> commands;

    public CommandsManager() {
        Instance = this;

        commands = new LinkedList<>(List.of(
                new MasterChatCommand(),
                new ChangeChannelCommand(),
                new ClearChatCommand(),
                new ReloadCommand(),
                new RemoveCommand()
        ));

        ConsoleLogger.Instance.debug(this.getClass().getName()  + " module loaded");
    }

    public void registerCommand(Command command) {
        commands.add(command);
    }

    public void unregisterAllCommands() {
        CommandMap map = MasterChat.Instance.getServer().getCommandMap();
        ConsoleLogger.Instance.debug("Start command un-registration: " + commands.stream().map(Command::getName).collect(Collectors.joining(", ")));
        commands.forEach(command -> command.unregister(map));
    }
}
