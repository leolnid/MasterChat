package ru.leocraft.masterchat.masterchat.messages;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import ru.leocraft.masterchat.masterchat.settings.Settings;
import ru.leocraft.masterchat.masterchat.settings.properties.TagsSettings;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SimpleMessage {

    private final List<SimpleTag> tags;

    public SimpleMessage(List<String> tagsNames) {
        this.tags = tagsNames
                .stream()
                .map(tag -> Settings.getTagProperty(TagsSettings.TAGS).get(tag))
                .collect(Collectors.toList());
    }

    public Component build(Player sender, String message, Player receiver) {
        return build(sender, message).apply(Type.PLAYER).apply(receiver);
    }

    public Function<Type, Function<Player, Component>> build(Player sender, String message) {
        List<Function<Type, Function<Player, Component>>> preparedTags = tags
                .stream()
                .map(v -> v.prepareForSending(sender, message))
                .collect(Collectors.toList());

        return type -> {
            List<Function<Player, Component>> collect = preparedTags.stream().map(v -> v.apply(type)).collect(Collectors.toList());

            return receiver -> collect
                    .stream()
                    .map(v -> v.apply(receiver))
                    .reduce(Component.empty(), Component::append);
        };
    }
}
