package xyz.polyr.autoemoji.listeners;

import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Keyboard;

import java.lang.reflect.Field;
import java.util.Map;

public class ClientChatListener {
    private static final String CHAT_TEXT_FIELD_NAME = "inputField";
    private static final String CHAT_TEXT_FIELD_SRG_NAME = "field_146415_a";
    private static final Field CHAT_TEXT_FIELD = ReflectionHelper.findField(GuiChat.class, CHAT_TEXT_FIELD_NAME, CHAT_TEXT_FIELD_SRG_NAME);
    private final Map<String, String> emojiMap;

    public ClientChatListener(final Map<String, String> emojiMap) {
        this.emojiMap = emojiMap;
    }

    @SubscribeEvent
    public void onClientChat(final GuiScreenEvent.KeyboardInputEvent.Pre event) {
        final GuiScreen gui = event.gui;
        if (gui instanceof GuiChat && Keyboard.getEventKeyState()) {
            final GuiChat chatGui = (GuiChat) gui;
            switch (Keyboard.getEventKey()) {
                case Keyboard.KEY_RETURN:
                case Keyboard.KEY_NUMPADENTER:
                    GuiTextField chatTextField;
                    try {
                        chatTextField = (GuiTextField) CHAT_TEXT_FIELD.get(chatGui);
                    } catch (final IllegalAccessException e) {
                        break;
                    }

                    final String chat = chatTextField.getText();
                    if (chat.trim().isEmpty()) {
                        break;
                    }

                    final String replacedChat = StringUtils.replaceEach(chat, emojiMap.keySet().toArray(new String[0]), emojiMap.values().toArray(new String[0]));
                    chatTextField.setText(""); // Sending an empty message will not send a message at all.
                    gui.mc.thePlayer.sendChatMessage(replacedChat);
                    gui.mc.ingameGUI.getChatGUI().addToSentMessages(chat);
            }
        }
    }
}
