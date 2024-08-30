package bymrshocker.shockerchatdisplays.Listeners;

import bymrshocker.shockerchatdisplays.ShockerChatDisplays;
import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import ru.brikster.chatty.Chatty;
import ru.brikster.chatty.api.ChattyApi;
import ru.brikster.chatty.api.event.ChattyMessageEvent;

import java.util.List;

public class ChattyListener implements Listener {
    ShockerChatDisplays plugin;
    List<String> channels;

    public ChattyListener(ShockerChatDisplays plugin) {
        this.plugin = plugin;
        channels = plugin.getConfig().getStringList("chattyChannels");
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onChattyMessage(ChattyMessageEvent event) {
        if (!channels.contains(event.getChat().getId())) return;
        plugin.createMessage(Component.text(event.getPlainMessage()), event.getSender());
    }

}
