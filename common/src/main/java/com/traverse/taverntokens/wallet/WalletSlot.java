package com.traverse.taverntokens.wallet;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.Slot;

public class WalletSlot extends Slot {
    public WalletSlot(WalletInventory container, int index, int x, int y) {
        super(container, index, x, y);
    }

    @Override
    public int getMaxStackSize() {
        return ((WalletInventory) this.container).getMaxItemCount();
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return ((WalletInventory) this.container).isValidItem(stack) && this.isHighlightable();
    }

    @Override
    public boolean isHighlightable() {
        return ((WalletInventory) this.container).canBeHighlighted(this.index);
    }
}
