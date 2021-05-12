package ru.leocraft.masterchat.masterchat.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import ru.leocraft.masterchat.masterchat.utils.ConsoleLogger;
import ru.leocraft.masterchat.masterchat.MasterChat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class GetHistoryCommand extends Command {
    public GetHistoryCommand() {
        super("history");

        MasterChatCommand.Instance.addSubCommand(this);
        MasterChat.registerCommand(this);

        ConsoleLogger.Instance.debug("Command registered: " + this.getName());
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        ConsoleLogger.Instance.debug("Command executed: " + alias + " " + Arrays.toString(args));

        ConsoleLogger.Instance.log(MasterChat.History.history.toString());
        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        ConsoleLogger.Instance.debug("Command complete: " + alias + " " + Arrays.toString(args));
        return new ArrayList<>();
    }

}
