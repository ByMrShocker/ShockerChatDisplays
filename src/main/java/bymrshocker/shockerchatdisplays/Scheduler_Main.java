package bymrshocker.shockerchatdisplays;

import bymrshocker.shockerchatdisplays.data.ChatDisplay;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;

import java.util.ArrayList;
import java.util.List;

public class Scheduler_Main {

    public Scheduler_Main(ShockerChatDisplays plugin) {

        int timer = plugin.getConfig().getInt("tickTime");

        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, bukkitTask -> {
            List<String> displaysToRemove = new ArrayList<>();
            for (String name : plugin.getDisplayHashMap().keySet()) {
                ChatDisplay chatDisplay = plugin.getDisplayHashMap().get(name);
                Player player = Bukkit.getPlayer(name);
                if (player == null || !player.getPassengers().contains(chatDisplay.getTextDisplay()) || chatDisplay.getTextDisplay().isDead()) {
                    displaysToRemove.add(name);
                    continue;
                }

                if (chatDisplay.getTimeToRemove() <= 0) {
                    displaysToRemove.add(name);
                    continue;
                }
                if (!chatDisplay.isPlayingAnimation()) {
                    chatDisplay.timeToRemove--;
                }

            }

            Bukkit.getScheduler().runTask(plugin, bukkitTask1 -> {
                for (String name : displaysToRemove) {
                    if (!plugin.getDisplayHashMap().get(name).getTextDisplay().isDead()) {
                        plugin.getDisplayHashMap().get(name).getTextDisplay().remove();
                    }

                    plugin.getDisplayHashMap().remove(name);
                }
            });




        }, timer, timer);

    }

}
