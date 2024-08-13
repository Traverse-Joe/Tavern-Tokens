package com.traverse.taverntokens.mixin;

import com.traverse.taverntokens.References;
import com.traverse.taverntokens.interfaces.PlayerWithBagInventory;
import com.traverse.taverntokens.wallet.WalletInventory;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

@Mixin(Player.class)
abstract class PlayerEntityMixin implements PlayerWithBagInventory {

    // PlayerEntityMixin() { super(null, null, 0, null); }

    @Unique
    public WalletInventory walletInventory = new WalletInventory();

    @Override
    public WalletInventory getWalletInventory() {
        return this.walletInventory;
    }

    // writeDataCustomNBT
    @Inject(at = @At("TAIL"), method = "addAdditionalSaveData")
    protected void addAdditionalSaveData(CompoundTag nbt, CallbackInfo ci) {
        nbt.put("WalletItems", this.walletInventory.toNbtList());
    }

    // readDataCustomNBT
    @Inject(at = @At("TAIL"), method = "readAdditionalSaveData")
    protected void readAdditionalSaveData(CompoundTag nbt, CallbackInfo ci) {
        if (nbt.contains("WalletItems", 9)) {
            this.walletInventory.readNbtList(nbt.getList("WalletItems", 10));
        }
    }

    // Drop Method for Coins on Death
    @Inject(at = @At("RETURN"), method = "dropEquipment")
    protected void dropEquipment(CallbackInfo ci) {
        References.LOGGER.info("Coins lost send HALP");
    }
}
