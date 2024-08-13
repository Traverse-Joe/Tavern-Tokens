package com.traverse.taverntokens.client.screens;

import org.joml.Matrix4f;
import org.joml.Quaternionf;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.traverse.taverntokens.References;
import com.traverse.taverntokens.wallet.WalletScreenHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;

public class WalletScreen extends AbstractContainerScreen<WalletScreenHandler>{

    private final ResourceLocation WALLET_GUI = new ResourceLocation(References.MODID, "textures/gui/wallet_inventory.png");

    public WalletScreen(WalletScreenHandler menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    // drawBackground
    @Override
    protected void renderBg(GuiGraphics context, float delta, int mouseX, int mouseY) {
        int slots = this.menu.walletInventory.getContainerSize();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, WALLET_GUI);

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        // context.drawTexture
        context.blit(WALLET_GUI, x, y, 0, 0, 175, 173);

        // Adds Slots Dynamically
        for (int slot = 0; slot < slots; slot++) {
            int _x = 61 + 18 * (slot % 6);
            int _y = 7 + 18 * (Math.floorDiv(slot, 6));
            context.blit(WALLET_GUI, x + _x, y + _y, 176, 0, 18, 18);
        }

        // Renders Player Model
        renderEntityInInventoryFollowsMouse(context, x + 31, y + 70, 30, (float)(x + 31) - mouseX, (float)(y + 70 - 50) - mouseY, this.minecraft.player);
    }

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
        renderTooltip(context, mouseX, mouseY);
    }

    public static void renderEntityInInventoryFollowsMouse(GuiGraphics guiGraphics, int x, int y, int scale, float mouseX, float mouseY, LivingEntity entity) {
        float f = (float) Math.atan((double) (mouseX / 40.0F));
        float g = (float) Math.atan((double) (mouseY / 40.0F));
        Quaternionf quaternionf = (new Quaternionf()).rotateZ(3.1415927F);
        Quaternionf quaternionf2 = (new Quaternionf()).rotateX(g * 20.0F * 0.017453292F);
        quaternionf.mul(quaternionf2);
        float h = entity.yBodyRot;
        float i = entity.getYRot();
        float j = entity.getXRot();
        float k = entity.yHeadRotO;
        float l = entity.yHeadRot;
        entity.yBodyRot = 180.0F + f * 20.0F;
        entity.setYRot(180.0F + f * 40.0F);
        entity.setXRot(-g * 20.0F);
        entity.yHeadRot = entity.getYRot();
        entity.yHeadRotO = entity.getYRot();
        renderEntityInInventory(guiGraphics, x, y, scale, quaternionf, quaternionf2, entity);
        entity.yBodyRot = h;
        entity.setYRot(i);
        entity.setXRot(j);
        entity.yHeadRotO = k;
        entity.yHeadRot = l;
    }

    @SuppressWarnings("deprecation")
    public static void renderEntityInInventory(GuiGraphics context, int x, int y, int size, Quaternionf quaternionf, Quaternionf quaternionf2, LivingEntity entity) {
        // context.getMatrices() [...]
        context.pose().pushPose();
        context.pose().translate((double)x, (double)y, 50.0);
        context.pose().mulPoseMatrix((new Matrix4f()).scaling((float)size, (float)size, (float)(-size)));
        context.pose().mulPose(quaternionf);
        Lighting.setupForEntityInInventory();
        EntityRenderDispatcher entityRenderDispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        if (quaternionf2 != null) {
            quaternionf2.conjugate();
            entityRenderDispatcher.cameraOrientation().set(quaternionf2);
        }

        entityRenderDispatcher.setRenderShadow(false);
        RenderSystem.runAsFancy(() -> {
            entityRenderDispatcher.render(entity, 0.0, 0.0, 0.0, 0.0F, 1.0F, context.pose(), context.bufferSource(), 15728880);
        });
        context.flush();
        entityRenderDispatcher.setRenderShadow(true);
        context.pose().popPose();
        Lighting.setupFor3DItems();
    }

} 