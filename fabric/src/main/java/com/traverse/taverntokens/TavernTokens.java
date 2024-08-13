package com.traverse.taverntokens;

import com.traverse.taverntokens.networking.PacketHandlerImpl;
import com.traverse.taverntokens.registry.ModItemsImpl;

import net.fabricmc.api.ModInitializer;

public class TavernTokens implements ModInitializer {

	@Override
	public void onInitialize() {
		References.LOGGER.info("Insert Coins to Continue");
		ModItemsImpl.registerItems();
		PacketHandlerImpl.registerClient();
		PacketHandlerImpl.registerServer();
	}
}