package harry.testmod.init;

import harry.testmod.TestMod;
import harry.testmod.init.blocks.Microwave;
import harry.testmod.init.blocks.TestBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class BlockInit 
{
	public static Block microwave_idle, microwave_active;
	
	public static void init()
	{
		microwave_active = new Microwave("microwave_active", true);
		microwave_idle = new Microwave("microwave_idle", false);
	}
	
	public static void register()
	{
		registerBlock(microwave_active);
		registerBlock(microwave_idle);
	}
	
	public static void registerBlock(Block block)
	{
		ForgeRegistries.BLOCKS.register(block);
		block.setCreativeTab(TestMod.testtab);
		ItemBlock item = new ItemBlock(block);
		item.setRegistryName(block.getRegistryName());
		ForgeRegistries.ITEMS.register(item);
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(block.getRegistryName(), "inventory"));
	}
}
