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
            config.get(EMOJI_MAP_CATEGORY, ":star:", "\u272E");
            config.get(EMOJI_MAP_CATEGORY, ":shrug:", "\u00AF\\_(\u30C4)_/\u00AF");
            config.get(EMOJI_MAP_CATEGORY, ":tableflip:", "(\u256F\u00B0\u25A1\u00B0\uFF09\u256F\uFE35 \u253B\u2501\u253B");
            config.get(EMOJI_MAP_CATEGORY, ":unflip:", "\u252C\u2500\u252C \u30CE( \u309C-\u309C\u30CE)");
            config.get(EMOJI_MAP_CATEGORY, "o/", "( \uFF9F\u25E1\uFF9F)/");
            config.get(EMOJI_MAP_CATEGORY, "\\o", "\\(\uFF9F\u25E1\uFF9F )");

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
