package com.traverse.taverntokens.registry;

import com.traverse.taverntokens.References;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.CreativeModeTab.Row;

public class ModItemsImpl extends ModItems {

    public static Item COPPER_COIN = new Item(new Item.Properties());
    public static Item IRON_COIN = new Item(new Item.Properties());
    public static Item GOLD_COIN = new Item(new Item.Properties());
    public static Item NETHERITE_COIN = new Item(new Item.Properties());

    public static CreativeModeTab TAB = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB,
        new ResourceLocation(References.MODID, "taverntokens_item_group"),
        CreativeModeTab.builder(Row.TOP, 1).title(Component.translatable("itemGroup." + References.MODID))
            .icon(() -> new ItemStack(Items.BUNDLE))
            .displayItems((displayParameters, entries) -> {
                entries.accept(COPPER_COIN);
                entries.accept(IRON_COIN);
                entries.accept(GOLD_COIN);
                entries.accept(NETHERITE_COIN);
            }).build());

    public static final TagKey<Item> VALID_CURRENCY = TagKey.create(Registries.ITEM, new ResourceLocation(References.MODID, "valid_currency"));

    public static void registerItems() {
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(References.MODID, "copper_coin"), COPPER_COIN);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(References.MODID, "iron_coin"), IRON_COIN);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(References.MODID, "gold_coin"), GOLD_COIN);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(References.MODID, "netherite_coin"), NETHERITE_COIN);
    }
}