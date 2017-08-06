package harry.tutorialmod.init;

import harry.tutorialmod.TutorialMod;
import harry.tutorialmod.init.blocks.CustomBlockStairs;
import harry.tutorialmod.init.blocks.CustomIngotBlock;
import harry.tutorialmod.init.blocks.CustomOre;
import harry.tutorialmod.init.blocks.slab.CustomBlockDoubleSlab;
import harry.tutorialmod.init.blocks.slab.CustomBlockHalfSlab;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemSlab;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class BlockInit 
{
	public static Block tutorial_ore, tutorial_ore_nether, tutorial_ore_end;
	public static Block tutorial_block;
	public static Block tutorial_stairs;
	public static CustomBlockHalfSlab tutorial_slab_half;
	public static CustomBlockDoubleSlab tutorial_slab_double;
	
	public static void init()
	{
		tutorial_ore = new CustomOre("tutorial_ore", 2.0F, 4.0F, 2);
		tutorial_ore_nether = new CustomOre("tutorial_ore_nether", 2.0F, 4.0F, 2);
		tutorial_ore_end = new CustomOre("tutorial_ore_end", 2.0F, 4.0F, 2);
		tutorial_block = new CustomIngotBlock("tutorial_block", 2.5F, 4.5F, 2);
		tutorial_stairs = new CustomBlockStairs("tutorial_stairs", tutorial_block.getDefaultState());
		tutorial_slab_half = new CustomBlockHalfSlab("tutorial_slab_half", 2.5F, 4.5F);
		tutorial_slab_double = new CustomBlockDoubleSlab("tutorial_slab_double", 2.5F, 4.5F);
	}
	
	public static void register()
	{
		registerBlock(tutorial_ore);
		registerBlock(tutorial_ore_nether);
		registerBlock(tutorial_ore_end);
		registerBlock(tutorial_block);
		registerBlock(tutorial_stairs);
		registerBlock(tutorial_slab_half, new ItemSlab(tutorial_slab_half, tutorial_slab_half, tutorial_slab_double));
		ForgeRegistries.BLOCKS.register(tutorial_slab_double);
	}
	
	public static void registerBlock(Block block)
	{
		ForgeRegistries.BLOCKS.register(block);
		block.setCreativeTab(TutorialMod.tutorialtab);
		ItemBlock item = new ItemBlock(block);
		item.setRegistryName(block.getRegistryName());
		ForgeRegistries.ITEMS.register(item);
		
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(block.getRegistryName(), "inventory"));
	}
	
	public static void registerBlock(Block block, ItemBlock itemblock)
	{
		ForgeRegistries.BLOCKS.register(block);
		block.setCreativeTab(TutorialMod.tutorialtab);
		itemblock.setRegistryName(block.getRegistryName());
		ForgeRegistries.ITEMS.register(itemblock);
		
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(block.getRegistryName(), "inventory"));
	}
}
