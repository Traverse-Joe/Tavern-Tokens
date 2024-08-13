package com.traverse.taverntokens.networking;

import com.traverse.taverntokens.interfaces.PlayerWithBagInventory;
import com.traverse.taverntokens.wallet.WalletInventory;
import com.traverse.taverntokens.wallet.WalletItemStack;
import com.traverse.taverntokens.wallet.WalletMenuProvider;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

public class PacketHandlerImpl extends PacketHandlerInterface {

    @Environment(EnvType.CLIENT)
    public static void registerClient() {
        ClientPlayNetworking.registerGlobalReceiver(PacketConstants.UPDATE_WALLET_ID, (client, handler, buf, responseSender) -> {
            WalletInventory walletInventory = ((PlayerWithBagInventory) client.player).getWalletInventory();
            
            int slot = buf.readInt();
            WalletItemStack itemStack = WalletItemStack.fromNbt(buf.readNbt());
            walletInventory.updateSlot(slot, itemStack);
        });
    }

    public static void registerServer() {
        ServerPlayNetworking.registerGlobalReceiver(PacketConstants.OPEN_WALLET_ID, (server, player, handler, buf, responseSender) -> {
            player.openMenu(new WalletMenuProvider());
        });
    }

    @Environment(EnvType.CLIENT)
    public static void requestWallet() {
        ClientPlayNetworking.send(PacketConstants.OPEN_WALLET_ID, PacketByteBufs.empty());
    }

    public static void updateWalletSlot(ServerPlayer serverPlayer, int slot, CompoundTag itemCompound) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeInt(slot);
        buf.writeNbt(itemCompound);

        ServerPlayNetworking.send(serverPlayer, PacketConstants.UPDATE_WALLET_ID, buf);
    }

}
