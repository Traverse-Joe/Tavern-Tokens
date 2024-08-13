package com.traverse.taverntokens.wallet;

import com.traverse.taverntokens.References;

import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

// Also known as a ScreenHandlerFactory - Fabric
public class WalletMenuProvider implements MenuProvider {
    @Override
    public Component getDisplayName() {
        return Component.translatable("screen." + References.MODID + ".wallet");
    }

    @Override
    public AbstractContainerMenu createMenu(int syncId, Inventory inv, Player player) {
        return new WalletScreenHandler(syncId, inv);
    }
}