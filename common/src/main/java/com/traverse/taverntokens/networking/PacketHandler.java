package com.traverse.taverntokens.networking;

import java.util.ServiceLoader;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;

@SuppressWarnings("static-access")
public final class PacketHandler extends PacketHandlerInterface {

    private static final PacketHandlerInterface instance = loadImplementation();

    public static void requestWallet() {
        instance.requestWallet();
    }

    public static void updateWalletSlot(ServerPlayer serverPlayer, int slot, CompoundTag itemCompound) {
        instance.updateWalletSlot(serverPlayer, slot, itemCompound);
    }


    private static PacketHandlerInterface loadImplementation() {
        return ServiceLoader.load(PacketHandlerInterface.class).findFirst()
                .orElseThrow(() -> new RuntimeException("No implementation found for MyInterface"));
    }

}
