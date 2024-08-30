package bymrshocker.shockerchatdisplays.Listeners;

import bymrshocker.shockerchatdisplays.ShockerChatDisplays;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class VanillaChatListener implements Listener {
    ShockerChatDisplays plugin;

    public VanillaChatListener(ShockerChatDisplays plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void AsyncChatEvent(AsyncChatEvent event) {
        if (event.isAsynchronous()) {
            plugin.createMessage(event.message(), event.getPlayer());
        }
    }
}
