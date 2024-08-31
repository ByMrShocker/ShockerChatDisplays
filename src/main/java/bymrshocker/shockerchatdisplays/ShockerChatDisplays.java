package bymrshocker.shockerchatdisplays;

import bymrshocker.shockerchatdisplays.Listeners.ChattyListener;
import bymrshocker.shockerchatdisplays.Listeners.EventListener;
import bymrshocker.shockerchatdisplays.Listeners.VanillaChatListener;
import bymrshocker.shockerchatdisplays.data.ChatDisplay;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class ShockerChatDisplays extends JavaPlugin {

    private HashMap<String, ChatDisplay> displayHashMap;
    public int displayTime;
    public int addTicksByLength;
    public int addTicksLengthThreshold;
    public int addTicksLengthPerCharacters;
    public List<String> ignoreMessagesWithStartSymbol;
    private List<Vector3f> displayOffsetScale;
    public int textAnimationSpeed;
    public int typingAnimationSymbolsPerTick;
    Scheduler_Main schedulerMain;
    private VanillaChatListener vanillaChatListener;

    private String listeningMethod = "minecraftEvent";
    ChattyListener chattyListener;

    @Override
    public void onEnable() {
        // Plugin startup logic
        displayHashMap = new HashMap<>();
        saveDefaultConfig();
        loadTextDisplayOffsets();
        displayTime = getConfig().getInt("displayTime");
        addTicksByLength = getConfig().getInt("addTicksByLength");
        addTicksLengthThreshold = getConfig().getInt("addTicksLengthThreshold");
        addTicksLengthPerCharacters = getConfig().getInt("addTicksLengthPerCharacters");
        ignoreMessagesWithStartSymbol = getConfig().getStringList("globalChatSymbol");
        textAnimationSpeed = getConfig().getInt("typingAnimationTick");
        typingAnimationSymbolsPerTick = getConfig().getInt("typingAnimationSymbolsPerTick");

        Bukkit.getPluginManager().registerEvents(new EventListener(this), this);

        vanillaChatListener = new VanillaChatListener(this);
        Bukkit.getPluginManager().registerEvents(vanillaChatListener, this);

        schedulerMain = new Scheduler_Main(this);


        if (!searchForDependPlugins()) {
            Bukkit.getScheduler().runTaskLater(this, bukkitTask -> {
                searchForDependPlugins();
            }, 10);
        }

        deathstrixRemnant();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }



    private boolean searchForDependPlugins() {
        Plugin chattyPlugin = getServer().getPluginManager().getPlugin("Chatty");
        if (chattyPlugin != null && chattyPlugin.isEnabled()) {
            try  {
                getLogger().info("Found Chatty. Unregistering vanilla event");
                disableVanillaMethod();
                listeningMethod = "chattyHandle";
                chattyListener = new ChattyListener(this);
                Bukkit.getPluginManager().registerEvents(chattyListener, this);
                return true;
            } catch (NoClassDefFoundError error) {
                getLogger().info("WARNING! Found chatty, but it is not instance of ChattyAPI!");
                return false;
            }

        }


        return false;
    }


    private void disableVanillaMethod() {
        ignoreMessagesWithStartSymbol.clear();
        HandlerList.unregisterAll(vanillaChatListener);
    }


    private void loadTextDisplayOffsets() {
        displayOffsetScale = new ArrayList<>();
        String[] offsetString = getConfig().getString("displayOffset").split(",");
        String[] scaleString = getConfig().getString("displayScale").split(",");

        displayOffsetScale.add(new Vector3f(Float.parseFloat(offsetString[0]), Float.parseFloat(offsetString[1]), Float.parseFloat(offsetString[2])));
        displayOffsetScale.add(new Vector3f(Float.parseFloat(scaleString[0]), Float.parseFloat(scaleString[1]), Float.parseFloat(scaleString[2])));


    }


    public void removeMessageForPlayer(Player player) {
        String playerName = player.getName();
        if (getDisplayHashMap().containsKey(playerName)) {
            getDisplayHashMap().get(playerName).destroy();
            getDisplayHashMap().remove(playerName);
        }
    }



    public void createMessage(Component newMessage, Player player) {

        if (player.isInvisible()) return;

        String message = PlainTextComponentSerializer.plainText().serialize(newMessage);
        if (!ignoreMessagesWithStartSymbol.isEmpty()) {
            if (ignoreMessagesWithStartSymbol.contains(String.valueOf(message.charAt(0)))) return;
        }


        String playerName = player.getName();
        if (getDisplayHashMap().containsKey(playerName)) {
            ChatDisplay chatDisplay = getDisplayHashMap().get(playerName);
            chatDisplay.setNewText(newMessage, this, message, textAnimationSpeed);
        } else {
            if (Bukkit.isPrimaryThread()) {
                createTextDisplay(newMessage, player, message);
            } else {
                Bukkit.getScheduler().runTask(this, bukkitTask -> {createTextDisplay(newMessage, player, message);});
            }
        }
    }

    private void createTextDisplay(Component newMessage, Player player, String serializedMessage) {
        String playerName = player.getName();
        ChatDisplay chatDisplay = new ChatDisplay((TextDisplay) player.getWorld().spawnEntity(
                player.getLocation().add(0,2,0), EntityType.TEXT_DISPLAY), displayOffsetScale.get(0), displayOffsetScale.get(1));
        player.addPassenger(chatDisplay.getTextDisplay());
        chatDisplay.setNewText(newMessage, this, serializedMessage, textAnimationSpeed);
        this.getDisplayHashMap().put(playerName, chatDisplay);
    }


    private void deathstrixRemnant() {
        List<String> remnant = new ArrayList<>();
        remnant.add("Дестрикс наремнантил");
        remnant.add("                                          .................");
        remnant.add("                                         .,,,,,,,,,,,,,,,,,,");
        remnant.add("                                        ,,,,,,,,,,,,,,,,,,,,,");
        remnant.add("                                        ....,,,,......,,,,,,,.");
        remnant.add("                                       *@@&*,,,,,*@@@/.,,,,,,");
        remnant.add("  .,,,,,,,,,,,,,,,.                    .,,,,,,,,,,,,,,,,,,,,,");
        remnant.add("  .,,,,,,,,,,,,,,,,,,,,,,,,,.          .....          ..,,,,,.");
        remnant.add("               .,,,,,,,,,,,,,,,,,,,,,,,,..          ...,,,,,,..........");
        remnant.add("                           .,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,.");
        remnant.add("                           .,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,.");
        remnant.add("                                                                 ,,,,,,,,,,,,,.");
        remnant.add("                              ......................             ,,,,,,,,,,,,,,,,.");
        remnant.add("                              .,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,");
        remnant.add("                                                 .,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,");
        remnant.add("                                                        ,,,,,,,,,,,,,,,,,,,,,,,,,,");
        remnant.add("                                                        ,,,,,,,,,,,,,,,,,,,,,,,,,, ");
        remnant.add("                                                      .,,,,,,,,,,,,,,,,,,,,,,,,,,, ");
        remnant.add("                                                   .,,,,,,,,,,,,,.  .,,,,,,,,,,,,.");
        remnant.add("                                               .,,,,,,,,,,,,        .,,,,,,,,,,,,.");
        remnant.add("                                            .,,,,,,,,,,,.          .,,,,,,,,,,,,.");
        remnant.add("                                        .,,,,,,,,,,,,.         .,,,,,,,,,,,,,,,,");
        remnant.add("                                     .,,,,,,,,,,,...        ,,,,,,,,,,,.");
        remnant.add("                                    ,,,,,,,,,,.     .,,,,,,,,,,,,.");
        remnant.add("                                    ,,,,,,,.    .,,,,,,,");
        remnant.add("                                    ..,,,,,,,,,,,,,,,,,.");
        remnant.add("                                       ..,,,,,,,,,,,,,,.");
        remnant.add("                                         .,,,,,,,,,,,,,,,,.");
        remnant.add("                                        .,,,,,,,,,,,,,,,,,.");
        remnant.add("                             .,,,,,,,,,,,,,,,,,,,,,    .,,,,,.");
        remnant.add("                             .,,,,,,,,,,,,,,,.          .,,,,,,,.");
        remnant.add("                                                          .,,,,,,");
        remnant.add("                                                          .,,,,,,");
        remnant.add("                                          ................,,,,,,,.");
        remnant.add("                                          ,,,,,,,,,,,,,,,,,,,,,,,,");
        remnant.add("                                          ,,,,,,,,,,,,,,,,,,,,,,,,");


        for (String rem : remnant) {
            getLogger().info(rem);
        }


    }




    public HashMap<String, ChatDisplay> getDisplayHashMap() {
        return displayHashMap;
    }
}
