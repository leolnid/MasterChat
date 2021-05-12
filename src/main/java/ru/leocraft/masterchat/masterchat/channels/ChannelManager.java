package ru.leocraft.masterchat.masterchat.channels;

import org.bukkit.World;
import org.bukkit.entity.Player;
import ru.leocraft.masterchat.masterchat.utils.ConsoleLogger;
import ru.leocraft.masterchat.masterchat.messages.MessageSender;
import ru.leocraft.masterchat.masterchat.messages.TemplateMessage;
import ru.leocraft.masterchat.masterchat.settings.Settings;
import ru.leocraft.masterchat.masterchat.settings.properties.PluginSettings;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ChannelManager {
    public static ChannelManager Instance;
    private final Map<String, Channel> playersChannels;
    private final Map<Character, Channel> charactersChannel;
    private final Map<String, Channel> channels;
    private final Channel defaultChannel;

    public ChannelManager(List<Channel> channels) {
        Instance = this;

        this.charactersChannel = new HashMap<>();
        this.channels = channels.stream().collect(Collectors.toMap(Channel::getName, Function.identity()));
        channels.forEach(channel -> channel.getAlias().forEach(alias -> {
            if (alias.length() == 1) {
                this.charactersChannel.put(alias.charAt(0), channel);
            }
            this.channels.put(alias, channel);
        }));
        this.playersChannels = new HashMap<>();

        this.defaultChannel = this.channels.get(Settings.getProperty(PluginSettings.DEFAULT_CHANNEL));
        ConsoleLogger.Instance.debug("ChannelManager module loaded");
    }

    public boolean isPlayerInChannel(Player player, Channel channel) {
        return playersChannels.containsKey(player.getName()) && playersChannels.get(player.getName()) == channel;
    }

    public Channel getPlayerChannel(Player player) {
        return playersChannels.get(player.getName());
    }

    public void setPlayerChannel(Player player, Channel channel) {
        if (isPlayerInChannel(player, channel)) return;

        playersChannels.put(player.getName(), channel);
        ConsoleLogger.Instance.debug("Player " + player.getName() + " connected to channel '" + channel.getName() + "'");
        MessageSender.sendSystemMessage(player, TemplateMessage.CHANNEL_CHANGED, channel.getName());
    }

    public void onPlayerJoin(Player player) {
        setPlayerChannel(player, defaultChannel);
    }

    public void onPlayerChangeWorld(Player player, World world) {
        String channelName = Settings.getProperty(PluginSettings.DEFAULT_WORLDS_CHANNELS).get(world.getName());
        if (channelName.isEmpty()) return;

        Channel channel = channels.get(channelName);
        setPlayerChannel(player, channel);
    }

    public void onPlayerQuit(Player player) {
        playersChannels.remove(player.getName());
    }

    public boolean onPlayerCommand(Player player, String channelName) {
        if (!channels.containsKey(channelName))
            return false;

        Channel channel = channels.get(channelName);
        setPlayerChannel(player, channel);
        return true;
    }

    public void onPlayerChat(Player sender, String message) {
        char start = message.charAt(0);
        if (charactersChannel.containsKey(start)) {
            if (message.length() == 1) {
                setPlayerChannel(sender, charactersChannel.get(start));
                return;
            }
            charactersChannel.get(start).sendMessage(sender, message.substring(1));
            return;
        }

        playersChannels.get(sender.getName()).sendMessage(sender, message);
    }

    public void destroy() {
        channels.values().forEach(Channel::destroy);
    }
}
