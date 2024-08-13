package com.traverse.taverntokens.wallet;

import com.traverse.taverntokens.interfaces.PlayerWithBagInventory;
import com.traverse.taverntokens.registry.ModScreens;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class WalletScreenHandler extends AbstractContainerMenu {

    public WalletInventory walletInventory;
    public Inventory playerInventory;

    private final Player player;

    public WalletScreenHandler(int syncId, Inventory inventory) {
        super(ModScreens.WALLET_SCREEN_HANDLER, syncId);
        this.player = inventory.player;

        this.walletInventory = ((PlayerWithBagInventory) player).getWalletInventory();
        this.playerInventory = inventory;

        // Player Inventory Slots
        for (int rows = 0; rows < 4; rows++) {
            for (int cols = 0; cols < 9; cols++) {
                int x = 8 + cols * 18;
                int y = 74 + rows * 18;
                if (rows == 0) y += 76;
                this.addSlot(new Slot(playerInventory, cols + rows * 9, x, y));
            }
        }

        // Wallet Inventory Slots
        int slotOffset = this.slots.size();
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 6; col++) {
                int x = 62 + col * 18;
                int y = 8 + row * 18;

                addSlot(new WalletSlot(walletInventory, slotOffset + col + row * 6, x, y));
            }
        }
    }

    @Override
    public void clicked(int slot, int button, ClickType actionType, Player player) {
        try {
            // Fuck MCreator ---- MUFFIN TIME
            switch (actionType) {
                case CLONE -> onClone(slot, button, player);
                case PICKUP -> onPickup(slot, button, player);
                case PICKUP_ALL -> onPickupAll(slot, button, player);
                case QUICK_CRAFT -> onQuickCraft(slot, button, player);
                case QUICK_MOVE -> onQuickMove(slot, button, player);
                case SWAP -> onSwap(slot, button, player);
                case THROW -> onThrow(slot, button, player);
            }

            // boolean isWallet = this.slots.get(slot).inventory == walletInventory;
            // if (isWallet) updateToClient();
        } catch (IndexOutOfBoundsException e) { }
    }
    
    public void onClone(int slot, int button, Player player) {
        Slot slots = this.slots.get(slot);
        boolean isPlayerInventory = slots.container == playerInventory;
        if (!isPlayerInventory) {
            ItemStack stack = walletInventory.getCopy(slot).toItemStack();
            setCarried(stack);
        } else super.clicked(slot, button, ClickType.CLONE, player);
    }

    public void onPickup(int slot, int button, Player player) {
        Slot slots = this.slots.get(slot);
        boolean isPlayerInventory = slots.container == playerInventory;
        if (!isPlayerInventory) {
            ItemStack stack = getCarried();
            if (walletInventory.isValidItem(stack) || stack.isEmpty()) {
                if (stack.isEmpty()) {
                    int divider = button + 1;
                    stack = walletInventory.removeItem(slot, 64 / divider);
                    setCarried(stack);
                } else if (slots.mayPlace(stack)) {
                    if (walletInventory.isValidItem(stack)) {
                        walletInventory.addItem(stack);
                    }
                }
            }
        } else super.clicked(slot, button, ClickType.PICKUP, player);
    }

    public void onPickupAll(int slot, int button, Player player) {
        // Automatically ignored
    }

    public void onQuickCraft(int slot, int button, Player player) {
        // Automatically ignored
    }

    public void onQuickMove(int slot, int button, Player player) {
        Slot slots = this.slots.get(slot);
        boolean isPlayerInventory = slots.container == playerInventory;
        if (!isPlayerInventory) {
            if (this.walletInventory.isValidItem(slots.getItem())) {
                for (int index = 0; index < 9 * 4; index++) {
                    Slot tempSlot = this.slots.get(index);
                    if (tempSlot.getItem().isEmpty()) {
                        tempSlot.set(this.walletInventory.removeItemNoUpdate(slot));
                        break;
                    }
                }
            }
        } else {
            if (this.walletInventory.isValidItem(slots.getItem())) {
                this.walletInventory.addItem(slots.getItem());
            } else {
                boolean isHotbar = slot < 9;
                int startIndex = isHotbar ? 9 : 0;
                int endIndex = isHotbar ? 9 * 4 : 8;

                for (int index = startIndex; index < endIndex; index++) {
                    Slot tempSlot = this.slots.get(index);
                    if (tempSlot.getItem().isEmpty()) {
                        tempSlot.set(slots.getItem());
                        slots.set(ItemStack.EMPTY);
                        break;
                    }
                }
            }
        }
    }

    public void onSwap(int slot, int button, Player player) {
        Slot slots = this.slots.get(slot);
        boolean isPlayerInventory = slots.container == playerInventory;
        if (isPlayerInventory) super.clicked(slot, button, ClickType.SWAP, player);
        // TODO: Allow Hotbar Swapping
    }

    public void onThrow(int slot, int button, Player player) {
        // Automatically ignored
    }



    @Override
    public boolean stillValid(Player player) {
        return walletInventory.stillValid(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slot) {
        return ItemStack.EMPTY;
    }

}
