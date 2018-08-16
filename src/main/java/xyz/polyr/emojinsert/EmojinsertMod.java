package xyz.polyr.emojinsert;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import xyz.polyr.emojinsert.listeners.ClientChatListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Mod(modid = EmojinsertMod.MODID, useMetadata = true, clientSideOnly = true)
public class EmojinsertMod {
    static final String MODID = "emojinsert";
    private static final String EMOJI_MAP_CATEGORY = "emoji_map";
    private static Configuration config;
    private final Map<String, String> emojiMap = new HashMap<>();

    @EventHandler
    public void onPreInit(final FMLPreInitializationEvent event) {
        if (!Optional.ofNullable(config).isPresent()) {
            config = new Configuration(event.getSuggestedConfigurationFile());
            config.load();
            config.get(EMOJI_MAP_CATEGORY, "<3", "\u2764");
            config.get(EMOJI_MAP_CATEGORY, ":star:", "\u272e");
            config.get(EMOJI_MAP_CATEGORY, ":shrug:", "\u00af\\_(\u30c4)_/\u00af");
            config.get(EMOJI_MAP_CATEGORY, ":tableflip:", "(\u256f\u00b0\u25a1\u00b0\uff09\u256f\ufe35 \u253b\u2501\u253b");
            config.get(EMOJI_MAP_CATEGORY, "o/", "( \uff9f\u25e1\uff9f)/");

            if (config.hasChanged()) {
                config.save();
            }
        }

        config.getCategory(EMOJI_MAP_CATEGORY).getValues().forEach((key, prop) -> emojiMap.put(key, prop.getString()));
    }

    @EventHandler
    public void onInit(final FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new ClientChatListener(emojiMap));
    }
}
