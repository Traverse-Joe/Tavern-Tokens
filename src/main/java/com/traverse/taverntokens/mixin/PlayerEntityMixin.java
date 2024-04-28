package com.traverse.taverntokens.mixin;

import com.traverse.taverntokens.Reference;
import com.traverse.taverntokens.interfaces.PlayerEntityWithBagInventory;
import com.traverse.taverntokens.util.WalletInventory;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin implements PlayerEntityWithBagInventory {

    @Unique
    public WalletInventory walletInventory = new WalletInventory();

    @Override
    public WalletInventory getWalletInventory() {
        return this.walletInventory;
    }

    @Inject(at = @At("TAIL"), method = "writeCustomDataToNbt")
    public void writeDataCustomNBT(NbtCompound nbt, CallbackInfo ci) {
        Reference.LOGGER.info("Coins deposited to the Bank");
    }

    @Inject(at = @At("TAIL"), method = "readCustomDataFromNbt")
    public void readDataCustomNBT(NbtCompound nbt, CallbackInfo ci) {
        Reference.LOGGER.info("Coins withdrawn from the Bank");
    }

    // Drop Method for Coins on Death
    @Inject(at = @At("RETURN"), method = "dropInventory")
    public void droppedInventory(CallbackInfo ci) {
        Reference.LOGGER.info("Coins lost send HALP");
    }
}