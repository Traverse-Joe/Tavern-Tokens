package com.traverse.taverntokens.registry;

import java.util.ServiceLoader;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

@SuppressWarnings("static-access")
public class ModItems {

    // Items
    public static Item COPPER_COIN;
    public static Item IRON_COIN;
    public static Item GOLD_COIN;
    public static Item NETHERITE_COIN;

    // Creative Tabs
    public static CreativeModeTab TAB;

    // Item Tags
    public static TagKey<Item> VALID_CURRENCY;

    // Load the values
    static {
        ModItems items = ServiceLoader.load(ModItems.class).findFirst()
                .orElseThrow(() -> new RuntimeException("No implementation found for ModItems"));

        COPPER_COIN = items.COPPER_COIN;
        IRON_COIN = items.IRON_COIN;
        GOLD_COIN = items.GOLD_COIN;
        NETHERITE_COIN = items.NETHERITE_COIN;

        TAB = items.TAB;

        VALID_CURRENCY = items.VALID_CURRENCY;
    }
}
