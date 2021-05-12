package ru.leocraft.masterchat.masterchat.messages;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.entity.Player;
import ru.leocraft.masterchat.masterchat.MasterChat;
import ru.leocraft.masterchat.masterchat.utils.Replacer;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class SimpleTag {
    private String result;

    private String content;
    private String permission;
    private ClickEvent.Action clickAction;
    private String clickValue;
    private String hoverText;
    private List<String> worlds;
    private Function<Player, Boolean> fullPlayerCheck;
    private Function<Player, Boolean> consoleCheck;
    private Function<Player, Boolean> defaultPlayerCheck;

    @SuppressWarnings("unused")
    public SimpleTag() {
        this("", "");
    }

    public SimpleTag(String content) {
        this(content, "");
    }

    public SimpleTag(String content, String hoverText) {
        this(content, hoverText, ClickEvent.openUrl(""));
    }

    public SimpleTag(String content, String hoverText, ClickEvent click) {
        this(content, hoverText, click, "");
    }

    public SimpleTag(String content, String hoverText, ClickEvent click, String permission) {
        this(content, hoverText, click, permission, List.of());
    }

    public SimpleTag(
            String content,
            String hoverText,
            ClickEvent click,
            String permission,
            List<String> worlds
    ) {
        this.content = content;
        this.permission = permission;
        this.hoverText = hoverText;
        this.clickAction = click.action();
        this.clickValue = click.value();
        this.worlds = worlds;

        buildChecks();
    }

    private void buildChecks() {
        Function<Player, Boolean> permissionCheck;
        Function<Player, Boolean> defaultPermissionCheck;
        if (!permission.isEmpty()) {
            if (permission.startsWith("-")) {
                permissionCheck = p -> !MasterChat.getPermission().has(p, permission.substring(1));
                defaultPermissionCheck = p -> true;
                consoleCheck = p -> false;
            } else {
                permissionCheck = p -> MasterChat.getPermission().has(p, permission);
                defaultPermissionCheck = p -> false;
                consoleCheck = p -> true;
            }
        } else {
            permissionCheck = p -> true;
            defaultPermissionCheck = p -> true;
            consoleCheck = p -> true;
        }

        Function<Player, Boolean> worldCheck = p -> worlds.isEmpty() || worlds.contains(p.getWorld().getName());
        Function<Player, Boolean> defaultWorldCheck = p -> worlds.isEmpty();

        fullPlayerCheck = p -> permissionCheck.apply(p) && worldCheck.apply(p);
        defaultPlayerCheck = p -> defaultPermissionCheck.apply(p) && defaultWorldCheck.apply(p);
    }

    @SuppressWarnings("unused")
    public String getContent() {
        return content;
    }

    @SuppressWarnings("unused")
    public void setContent(String content) {
        this.content = content;
    }

    @SuppressWarnings("unused")
    public String getPermission() {
        return permission;
    }

    @SuppressWarnings("unused")
    public void setPermission(String permission) {
        this.permission = permission;
        buildChecks();
    }

    @SuppressWarnings("unused")
    public ClickEvent.Action getClickAction() {
        return clickAction;
    }

    @SuppressWarnings("unused")
    public void setClickAction(ClickEvent.Action clickAction) {
        this.clickAction = clickAction;
    }

    @SuppressWarnings("unused")
    public String getClickValue() {
        return clickValue;
    }

    @SuppressWarnings("unused")
    public void setClickValue(String clickValue) {
        this.clickValue = clickValue;
    }

    @SuppressWarnings("unused")
    public List<String> getWorlds() {
        return worlds;
    }

    @SuppressWarnings("unused")
    public void setWorlds(List<String> worlds) {
        this.worlds = worlds;
        buildChecks();
    }

    @SuppressWarnings("unused")
    public String getHoverText() {
        return hoverText;
    }

    @SuppressWarnings("unused")
    public void setHoverText(String hoverText) {
        this.hoverText = hoverText;
    }

    public Component buildComponent() {
        return Component
                .text(content)
                .hoverEvent(HoverEvent.showText(Component.text(hoverText)))
                .clickEvent(ClickEvent.clickEvent(clickAction, clickValue));
    }

    public String buildGsonString() {
        if (result != null) return result;

        return result = GsonComponentSerializer
                .gson()
                .serializeOr(this.buildComponent(), "{\"text\":\"\"}");
    }

    public Function<Type, Function<Player, Component>> prepareForSending(Player sender, String message) {
        String formatted = Replacer.setPlaceholders(buildGsonString(), sender, message);
        Component result = GsonComponentSerializer.gson().deserialize(formatted);

        Map<Type, Function<Player, Component>> map = Map.of(
                Type.DEFAULT_PLAYER, receiver -> defaultPlayerCheck.apply(receiver) ? result : Component.empty(),
                Type.PLAYER, receiver -> fullPlayerCheck.apply(receiver) ? result : Component.empty(),
                Type.CONSOLE, receiver -> consoleCheck.apply(receiver) ? result : Component.empty()
        );

        return map::get;
    }

    public Function<Player, Component> prepareForSending(Player sender, String message, Type type) {
        return prepareForSending(sender, message).apply(type);
    }
}
