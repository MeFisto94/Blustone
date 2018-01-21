package com.github.mefisto94.Blustone;

import com.github.mefisto94.Blustone.blocks.FastRedstoneTorchBlock;
import com.github.mefisto94.Blustone.blocks.FastRepeaterBlock;
import com.github.mefisto94.Blustone.blocks.UnlimitedBlockRedstoneWire;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

@Mod(modid = Blustone.MODID, version = Blustone.VERSION)
public class Blustone
{
    public static final String MODID = "blustone";
    public static final String VERSION = "1.0";
    public static final FastRepeaterBlock FAST_REPEATER_BLOCK_UNPOWERED = new FastRepeaterBlock(false);
    public static final FastRepeaterBlock FAST_REPEATER_BLOCK_POWERED = new FastRepeaterBlock(true);
    public static final UnlimitedBlockRedstoneWire UNL_BLOCK_REDSTONE_WIRE = new UnlimitedBlockRedstoneWire();
    public static final FastRedstoneTorchBlock FAST_TORCH_BLOCK_UNLIT = new FastRedstoneTorchBlock(false);
    public static final FastRedstoneTorchBlock FAST_TORCH_BLOCK_LIT = new FastRedstoneTorchBlock(true);
    public static ItemBlock ITEM_BLOCK_REPEATER_OFF = new ItemBlock(FAST_REPEATER_BLOCK_UNPOWERED);
    public static ItemBlock ITEM_BLOCK_REPEATER_ON = new ItemBlock(FAST_REPEATER_BLOCK_POWERED);
    public static ItemBlock ITEM_BLOCK_UNL_WIRE = new ItemBlock(UNL_BLOCK_REDSTONE_WIRE);
    public static final ItemBlock ITEM_BLOCK_TORCH_ON = new ItemBlock(FAST_TORCH_BLOCK_LIT);
    public static final ItemBlock ITEM_BLOCK_TORCH_OFF = new ItemBlock(FAST_TORCH_BLOCK_UNLIT);
    public static final int WEAK_POWER = 999;

    @EventHandler
    public void init(FMLInitializationEvent event) {
    }

    @EventHandler
    public void PreInit(FMLPreInitializationEvent event) {
        GameRegistry.register(FAST_REPEATER_BLOCK_POWERED);
        GameRegistry.register(FAST_REPEATER_BLOCK_UNPOWERED);
        GameRegistry.register(UNL_BLOCK_REDSTONE_WIRE);
        GameRegistry.register(FAST_TORCH_BLOCK_UNLIT);
        GameRegistry.register(FAST_TORCH_BLOCK_LIT);
        ITEM_BLOCK_REPEATER_OFF.setRegistryName(FAST_REPEATER_BLOCK_UNPOWERED.getRegistryName());
        ITEM_BLOCK_REPEATER_ON.setRegistryName(FAST_REPEATER_BLOCK_POWERED.getRegistryName());
        ITEM_BLOCK_UNL_WIRE.setRegistryName(UNL_BLOCK_REDSTONE_WIRE.getRegistryName());
        ITEM_BLOCK_TORCH_ON.setRegistryName(FAST_TORCH_BLOCK_LIT.getRegistryName());
        ITEM_BLOCK_TORCH_OFF.setRegistryName(FAST_TORCH_BLOCK_UNLIT.getRegistryName());
        GameRegistry.register(ITEM_BLOCK_REPEATER_OFF);
        GameRegistry.register(ITEM_BLOCK_UNL_WIRE);
        GameRegistry.register(ITEM_BLOCK_TORCH_ON);

        GameRegistry.register(ITEM_BLOCK_REPEATER_ON);
        GameRegistry.register(ITEM_BLOCK_TORCH_OFF);

    }

    @EventHandler
    @SideOnly(Side.CLIENT)
    public void PreInit_ClientOnly(FMLPreInitializationEvent event) {
        final int DEFAULT_ITEM_SUBTYPE = 0;
        ModelLoader.setCustomModelResourceLocation(ITEM_BLOCK_REPEATER_ON, DEFAULT_ITEM_SUBTYPE, new ModelResourceLocation(FAST_REPEATER_BLOCK_POWERED.getRegistryName(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(ITEM_BLOCK_REPEATER_OFF, DEFAULT_ITEM_SUBTYPE, new ModelResourceLocation(FAST_REPEATER_BLOCK_UNPOWERED.getRegistryName(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(ITEM_BLOCK_UNL_WIRE, DEFAULT_ITEM_SUBTYPE, new ModelResourceLocation(UNL_BLOCK_REDSTONE_WIRE.getRegistryName(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(ITEM_BLOCK_TORCH_ON, DEFAULT_ITEM_SUBTYPE, new ModelResourceLocation(FAST_TORCH_BLOCK_LIT.getRegistryName(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(ITEM_BLOCK_TORCH_OFF, DEFAULT_ITEM_SUBTYPE, new ModelResourceLocation(FAST_TORCH_BLOCK_UNLIT.getRegistryName(), "inventory"));
    }

    @EventHandler
    @SideOnly(Side.CLIENT)
    public void Init_ClientOnly(FMLInitializationEvent event) {
        Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(new IBlockColor() {
            @Override
            public int colorMultiplier(IBlockState state, @Nullable IBlockAccess worldIn, @Nullable BlockPos pos, int tintIndex) {
                //int pwr = ((PropertyInteger)(state.getProperties().get(BlockRedstoneWire.POWER))).
                int pwr = state.getValue(BlockRedstoneWire.POWER).intValue();

                if (pwr == 0) {
                    return 0x000d22;
                } else {
                    return mixColors(0x000d55, 0x000dff, pwr / 15f);
                }

            }
        }, UNL_BLOCK_REDSTONE_WIRE);
    }

    @SubscribeEvent
    public void registerBlocks(RegistryEvent.Register<Block> event) {
        System.out.println("REGISTER BLOCKS");
    }

    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> event) {
        System.out.println("REGISTER ITEMS");
    }

    public static int mixColors(int a, int b, float ratio){
        int mask1 = 0x00ff00ff;
        int mask2 = 0xff00ff00;

        int f2 = (int)(256 * ratio);
        int f1 = 256 - f2;

        return (((((a & mask1) * f1) + ((b & mask1) * f2)) >> 8) & mask1)
                | (((((a & mask2) * f1) + ((b & mask2) * f2)) >> 8) & mask2);
    }
}
