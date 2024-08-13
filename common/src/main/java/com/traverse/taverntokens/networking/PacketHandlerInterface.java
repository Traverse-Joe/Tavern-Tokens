package com.traverse.taverntokens.networking;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;

abstract class PacketHandlerInterface {

    static void requestWallet() {}

    static void updateWalletSlot(ServerPlayer serverPlayer, int slot, CompoundTag itemCompound) {}

}