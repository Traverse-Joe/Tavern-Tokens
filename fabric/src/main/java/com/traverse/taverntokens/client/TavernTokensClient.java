package com.traverse.taverntokens.client;

import org.lwjgl.glfw.GLFW;

import com.mojang.blaze3d.platform.InputConstants;
import com.traverse.taverntokens.References;
import com.traverse.taverntokens.client.screens.WalletScreen;
import com.traverse.taverntokens.networking.PacketHandler;
import com.traverse.taverntokens.registry.ModScreens;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.MenuScreens;

public class TavernTokensClient implements ClientModInitializer {
	
	private static KeyMapping keyBinding;

	@Override
	public void onInitializeClient() {
		References.LOGGER.info("Client Loaded");
		MenuScreens.register(ModScreens.WALLET_SCREEN_HANDLER, WalletScreen::new);

		keyBinding = KeyBindingHelper.registerKeyBinding(new KeyMapping(
				"key." + References.MODID + ".wallet", // The translation key of the keybinding's name
				InputConstants.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
				GLFW.GLFW_KEY_R, // The keycode of the key
				"key.category." + References.MODID // The translation key of the keybinding's category.
		));

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (keyBinding.isDown())
				PacketHandler.requestWallet();
		}); 
	} 
}