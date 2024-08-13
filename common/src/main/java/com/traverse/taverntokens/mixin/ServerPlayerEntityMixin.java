package com.traverse.taverntokens.mixin;

import com.traverse.taverntokens.interfaces.PlayerWithBagInventory;
import com.traverse.taverntokens.networking.PacketHandler;
import com.traverse.taverntokens.wallet.WalletItemStack;
import com.traverse.taverntokens.wallet.WalletScreenHandler;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundContainerSetContentPacket;
import net.minecraft.network.protocol.game.ClientboundContainerSetDataPacket;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerSynchronizer;
import net.minecraft.world.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
abstract class ServerPlayerEntityMixin extends PlayerEntityMixin {

    // copyFrom
    @SuppressWarnings("static-access")
    @Inject(at = @At("RETURN"), method = "restoreFrom")
    protected void onRespawn(ServerPlayer oldPlayer, boolean alive, CallbackInfo ci) {
        this.walletInventory.copy(((PlayerWithBagInventory) oldPlayer).getWalletInventory());
    }

    // onScreenHandlerOpened
    @Inject( method = "initMenu", at = @At("INVOKE"), cancellable = true )
    protected void initMenu(AbstractContainerMenu menu, CallbackInfo ci) {
        if (menu instanceof WalletScreenHandler) {
            ServerPlayer that = (ServerPlayer) (Object) this;

            menu.setSynchronizer(new ContainerSynchronizer() {

                @Override
                public void sendInitialData(AbstractContainerMenu handler, NonNullList<ItemStack> stacks, ItemStack cursorStack, int[] properties) {
                    that.connection.send(new ClientboundContainerSetContentPacket(handler.containerId, handler.incrementStateId(), stacks, cursorStack));
                    for (int i = 0; i < properties.length; ++i) {
                        this.sendPropertyUpdate(handler, i, properties[i]);
                    }
                }

                @Override
                public void sendSlotChange(AbstractContainerMenu handler, int slot, ItemStack stack) {
                    if (stack instanceof WalletItemStack walletItem) {
                        CompoundTag walletItemAsNBT = walletItem.writeNbt(new CompoundTag());
                        PacketHandler.updateWalletSlot(that, slot, walletItemAsNBT);
                    } else {
                        that.connection.send(new ClientboundContainerSetSlotPacket(handler.containerId, handler.incrementStateId(), slot, stack));
                    }
                }

                @Override
                public void sendCarriedChange(AbstractContainerMenu handler, ItemStack stack) {
                    that.connection.send(new ClientboundContainerSetSlotPacket(-1, handler.incrementStateId(), -1, stack));
                }

                @Override
                public void sendDataChange(AbstractContainerMenu handler, int property, int value) {
                    this.sendPropertyUpdate(handler, property, value);
                }

                private void sendPropertyUpdate(AbstractContainerMenu handler, int property, int value) {
                    that.connection.send(new ClientboundContainerSetDataPacket(handler.containerId, property, value));
                }
            });

            ci.cancel();
        }
    }

}
