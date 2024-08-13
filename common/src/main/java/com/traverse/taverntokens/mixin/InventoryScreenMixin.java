package com.traverse.taverntokens.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.traverse.taverntokens.References;
import com.traverse.taverntokens.networking.PacketHandler;

import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.resources.ResourceLocation;

@Mixin(InventoryScreen.class)
abstract class InventoryScreenMixin {

    @Shadow
    private <T extends GuiEventListener & Renderable & NarratableEntry> T addRenderableWidget(T widget) {
        return widget;
    }

    @Shadow private int leftPos;
    @Shadow private int height;

    @Unique
    private final ResourceLocation WALLET_IMAGE = new ResourceLocation(References.MODID, "textures/gui/wallet.png");

    @Inject(at = @At("TAIL"), method = "init", cancellable = true)
    private void init(CallbackInfo ci) {

        int offsetChance = Math.floorDiv((int) Math.ceil(Math.random() * 100), 100) * 10;

        addRenderableWidget(
            new ImageButton(this.leftPos + 27, this.height / 2 - 16, 10, 10, offsetChance, 0, 10, WALLET_IMAGE, 20, 20, (button) -> {
                PacketHandler.requestWallet();
            })
        );
    }
}
