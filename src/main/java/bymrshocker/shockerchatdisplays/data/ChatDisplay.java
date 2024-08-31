package bymrshocker.shockerchatdisplays.data;

import bymrshocker.shockerchatdisplays.ShockerChatDisplays;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.entity.Display;
import org.bukkit.entity.TextDisplay;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Transformation;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

public class ChatDisplay {
    TextDisplay textDisplay;
    public int timeToRemove;
    private boolean playingAnimation = false;
    private BukkitTask bukkitTask;
    private int currentAnimationIndex;

    public ChatDisplay(TextDisplay text, Vector3f offset, Vector3f scale) {
        this.textDisplay = text;
        textDisplay.setShadowed(true);
        textDisplay.customName(Component.text("scd_messageDisplay"));
        textDisplay.setCustomNameVisible(false);
        textDisplay.setDefaultBackground(false);
        textDisplay.setSeeThrough(false);
        textDisplay.setAlignment(TextDisplay.TextAlignment.CENTER);
        textDisplay.setBillboard(Display.Billboard.CENTER);
        textDisplay.setDefaultBackground(false);
        textDisplay.setBackgroundColor(Color.fromARGB(0, 0, 0, 0));
        textDisplay.setTransformation(new Transformation(
                offset,
                new AxisAngle4f(0f, 0f, 0f, 1f),
                scale,
                new AxisAngle4f(0f, 0f, 0f, 1f)));

    }

    public void destroy() {
        if (bukkitTask != null && !bukkitTask.isCancelled()) {
            bukkitTask.cancel();
        }
        if (!textDisplay.isDead()) {
            textDisplay.remove();
        }

    }

    public int getTimeToRemove() {
        return timeToRemove;
    }

    public TextDisplay getTextDisplay() {
        return textDisplay;
    }

    public boolean isPlayingAnimation() {
        return playingAnimation;
    }

    public void setNewText(Component text, ShockerChatDisplays plugin, String string, int speed) {
        timeToRemove = plugin.displayTime;
        if (string.length() >= plugin.addTicksLengthThreshold) {
            int addLength = (string.length() - plugin.addTicksLengthThreshold);
            timeToRemove = timeToRemove + ((addLength / plugin.addTicksLengthPerCharacters) * plugin.addTicksByLength);
        }


        if (speed != -1) {
            runTypingAnimation(plugin, string);
        } else {
            textDisplay.text(text);
        }

    }


    private void runTypingAnimation(ShockerChatDisplays plugin, String message) {
        if (bukkitTask != null) {
            if (!bukkitTask.isCancelled()) {
                bukkitTask.cancel();
            }
        }
        currentAnimationIndex = 0;
        playingAnimation = true;
        bukkitTask = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            if (currentAnimationIndex < message.length() && !textDisplay.isDead()) {
                for (int i = 0; i < plugin.typingAnimationSymbolsPerTick; i++) {
                    textDisplay.text(Component.text(message.substring(0, currentAnimationIndex + i)));
                }

                currentAnimationIndex = currentAnimationIndex + plugin.typingAnimationSymbolsPerTick;
            } else {
                textDisplay.text(Component.text(message));
                playingAnimation = false;
                bukkitTask.cancel();
            }
        }, 0, plugin.textAnimationSpeed);
    }


}
