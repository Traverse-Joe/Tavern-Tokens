package com.traverse.taverntokens.registry;

import java.util.ServiceLoader;

import com.traverse.taverntokens.wallet.WalletScreenHandler;
import net.minecraft.world.inventory.MenuType;

@SuppressWarnings("static-access")
public class ModScreens {

    public static MenuType<WalletScreenHandler> WALLET_SCREEN_HANDLER;

    // Load the values
    static {
        ModScreens screens = ServiceLoader.load(ModScreens.class).findFirst()
                .orElseThrow(() -> new RuntimeException("No implementation found for ModItems"));

        WALLET_SCREEN_HANDLER = screens.WALLET_SCREEN_HANDLER;
    }
}
