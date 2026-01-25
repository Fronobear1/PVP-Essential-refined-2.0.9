package com.fronobear.pvpessentials.refined.client.anchor;

import com.fronobear.pvpessentials.refined.PvPEssentialsRefined;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class AnchorBlocks {

    public static final Identifier FAKE_ANCHOR_ID = Identifier.of(PvPEssentialsRefined.MOD_ID, "fake_anchor");
    public static final RegistryKey<Block> FAKE_ANCHOR_KEY = RegistryKey.of(RegistryKeys.BLOCK, FAKE_ANCHOR_ID);

    public static final Block FAKE_ANCHOR = registerBlock(
            new Block(AbstractBlock.Settings.create()
                    .registryKey(FAKE_ANCHOR_KEY)
                    .nonOpaque() // block is translucent
                    .strength(-1.0f) // can't Break
                    .dropsNothing() // Does not drop items when broken
                    .replaceable() // Replaceable like a fern
                    .sounds(BlockSoundGroup.POWDER_SNOW))); //cuz uh idk, doesn't really need a sound

    private static Block registerBlock(Block block) {
        registerBlockItem(block);
        return Registry.register(Registries.BLOCK, FAKE_ANCHOR_ID, block);
    }

    private static void registerBlockItem(Block block) {
        RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, FAKE_ANCHOR_ID);
        Registry.register(Registries.ITEM, FAKE_ANCHOR_ID,
                new BlockItem(block, new Item.Settings().registryKey(itemKey)));
    }

    public static void register() {
        // Just to trigger static initializer
    }
}
