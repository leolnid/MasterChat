package ru.leocraft.masterchat.masterchat;

import com.google.gson.JsonObject;
import io.socket.client.IO;
import io.socket.client.Socket;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import ru.leocraft.masterchat.masterchat.channels.Channel;
import ru.leocraft.masterchat.masterchat.channels.ChannelManager;
import ru.leocraft.masterchat.masterchat.commands.*;
import ru.leocraft.masterchat.masterchat.messages.TemplateMessage;
import ru.leocraft.masterchat.masterchat.settings.Settings;
import ru.leocraft.masterchat.masterchat.settings.properties.PluginSettings;
import ru.leocraft.masterchat.masterchat.utils.ConsoleLogger;
import ru.leocraft.masterchat.masterchat.utils.History;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class MasterChat extends JavaPlugin {
    public static final String COMMAND_PREFIX = "MasterChat";
    public static MasterChat Instance;
    public static History History;

    private static Economy economy = null;
    private static Permission permission = null;

    public List<String> chats;
    private String serverName;
    private Socket socket;
    private Function<Player, String> getRole = player -> "default";

    public static Economy getEconomy() {
        return economy;
    }

    public static Permission getPermission() {
        return permission;
    }

    public static void registerCommand(Command command) {
        Instance.getServer().getCommandMap().register(MasterChat.COMMAND_PREFIX, command);
    }

    @Override
    public void onEnable() {
        Instance = this;
        // Basic staff
        new Settings();
        new ConsoleLogger();

        if (!setupEconomy()) {
            ConsoleLogger.Instance.log(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        setupPermissions();
        if (permission.hasGroupSupport())
            getRole = permission::getPrimaryGroup;

        // Modules
        History = new History();
        new TemplateMessage();
        new CommandsManager();
        new ChannelManager(chats.stream().map(Channel::new).collect(Collectors.toList()));
        getServer().getPluginManager().registerEvents(new Listener(), MasterChat.Instance);

        // Socket setup
        serverName = Settings.getProperty(PluginSettings.SERVER_NAME);
        if (serverName.isEmpty()) serverName = getServer().getName();

        if (!Settings.getProperty(PluginSettings.SOCKET_URL).isEmpty()) {
            ConsoleLogger.Instance.debug("Create new socket client. " + Settings.getProperty(PluginSettings.SOCKET_URL));

            URI socketUri = URI.create(Settings.getProperty(PluginSettings.SOCKET_URL));
            IO.Options options = IO.Options
                    .builder()
                    .setQuery("service_name=minecraft." + serverName)
                    .build();

            socket = IO.socket(socketUri, options);
            socket.on(Socket.EVENT_CONNECT, value -> MasterChat.Instance.getServer().getConsoleSender().sendMessage("§7[§a" + MasterChat.Instance.getDescription().getPrefix() + "§7] Socket connection opened."));
            socket.on(Socket.EVENT_DISCONNECT, value -> MasterChat.Instance.getServer().getConsoleSender().sendMessage("§7[§a" + MasterChat.Instance.getDescription().getPrefix() + "§7] Socket connection closed."));
            socket.on(Socket.EVENT_CONNECT_ERROR, value -> ConsoleLogger.Instance.debug("Socket connection error.\n...........| " + value[0] + "\n...........| xhr poll error = invalid socket address"));
            socket.on("event", value -> ConsoleLogger.Instance.log(Arrays.toString(value)));
            socket.connect();
        }
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(Listener.Instance);
        CommandsManager.Instance.unregisterAllCommands();
        History = null;
        socket.close();
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return true;
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        if (rsp == null) {
            return false;
        }
        permission = rsp.getProvider();
        return true;
    }

    public void sendToSocket(Player sender, Channel channel, Component message, Integer hash) {
        JsonObject json = new JsonObject();
        json.addProperty("time", System.currentTimeMillis());

        JsonObject jsonSender = new JsonObject();
        jsonSender.addProperty("server", serverName);
        jsonSender.addProperty("channel", channel.getName());
        jsonSender.addProperty("role", getRole.apply(sender));
        jsonSender.addProperty("name", sender.getName());
        json.add("sender", jsonSender);

        JsonObject jsonMessage = new JsonObject();
        jsonMessage.addProperty("legacy", LegacyComponentSerializer.legacyAmpersand().serialize(message));
        jsonMessage.addProperty("gson", GsonComponentSerializer.gson().serialize(message));
        jsonMessage.addProperty("hash", hash);
        json.add("message", jsonMessage);

        sendToSocket("message", json);
    }

    public void sendToSocket(String sender, String action) {
        JsonObject json = new JsonObject();
        json.addProperty("action", action);
        json.addProperty("sender", sender);
        json.addProperty("time", System.currentTimeMillis());

        sendToSocket("event", json);
    }

    public void sendToSocket(String event, JsonObject json) {
        socket.emit(event, json);
    }
}
