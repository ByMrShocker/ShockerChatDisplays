package bymrshocker.shockerchatdisplays.Listeners;

import bymrshocker.shockerchatdisplays.ShockerChatDisplays;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class EventListener implements Listener {
    ShockerChatDisplays plugin;

    public EventListener(ShockerChatDisplays plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void PlayerQuitEvent(PlayerQuitEvent event) {
        plugin.removeMessageForPlayer(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void PlayerGameModeChangeEvent(PlayerGameModeChangeEvent event) {
        plugin.removeMessageForPlayer(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void PlayerDeathEvent(PlayerDeathEvent event) {
        plugin.removeMessageForPlayer(event.getPlayer());
    }
}
