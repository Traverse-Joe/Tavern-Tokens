package com.traverse.taverntokens.registry;

import com.traverse.taverntokens.References;
import com.traverse.taverntokens.wallet.WalletScreenHandler;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;

public class ModScreensImpl extends ModScreens {

    public static MenuType<WalletScreenHandler> WALLET_SCREEN_HANDLER = new MenuType<>(WalletScreenHandler::new, FeatureFlags.VANILLA_SET);

    public static void register() {
        Registry.register(BuiltInRegistries.MENU, new ResourceLocation(References.MODID, "wallet_screen"), WALLET_SCREEN_HANDLER);
    }
}