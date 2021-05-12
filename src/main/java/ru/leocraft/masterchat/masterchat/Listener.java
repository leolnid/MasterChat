package ru.leocraft.masterchat.masterchat;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import ru.leocraft.masterchat.masterchat.channels.ChannelManager;
import ru.leocraft.masterchat.masterchat.messages.MessageSender;
import ru.leocraft.masterchat.masterchat.messages.TemplateMessage;
import ru.leocraft.masterchat.masterchat.utils.ConsoleLogger;

public class Listener implements org.bukkit.event.Listener {
    public static Listener Instance;

    public Listener() {
        Instance = this;
        MasterChat.Instance.getServer().getOnlinePlayers().forEach(player -> {
            MasterChat.History.onPlayerJoin(player);
            ChannelManager.Instance.onPlayerJoin(player);
        });
        ConsoleLogger.Instance.debug("Listener module loaded");
    }

    // =====================================================================================
    private void playerJoin(Player player) {
        MasterChat.History.onPlayerJoin(player);
        ChannelManager.Instance.onPlayerJoin(player);
        MessageSender.broadcastSystemMessage(player, TemplateMessage.PLAYER_JOIN, "");
    }

    private void playerQuit(Player player) {
        MessageSender.broadcastSystemMessage(player, TemplateMessage.PLAYER_QUIT, "");
        ChannelManager.Instance.onPlayerQuit(player);
        MasterChat.History.onPlayerQuit(player);
    }
    // =====================================================================================

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent event) {
        playerJoin(event.getPlayer());
        event.joinMessage(Component.empty());
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerQuit(PlayerQuitEvent event) {
        playerQuit(event.getPlayer());
        event.quitMessage(Component.empty());
    }

    @EventHandler
    public void onPlayerChangeWorld(PlayerChangedWorldEvent event) {
        ChannelManager.Instance.onPlayerChangeWorld(event.getPlayer(), event.getPlayer().getWorld());
    }

    @EventHandler
    public void onPlayerChat(AsyncChatEvent event) {
        long start = System.currentTimeMillis();

        ChannelManager.Instance.onPlayerChat(event.getPlayer(), LegacyComponentSerializer.legacyAmpersand().serialize(event.message()));
        event.setCancelled(true);

        long end = System.currentTimeMillis();
        ConsoleLogger.Instance.debug("ChatEvent takes: " + (end - start) + "ms");
    }

    @EventHandler
    public void onPlayerKicked(PlayerKickEvent event) {
    }
}
