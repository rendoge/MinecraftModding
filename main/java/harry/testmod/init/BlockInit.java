package harry.testmod.init;

import harry.testmod.TestMod;
import harry.testmod.init.blocks.Microwave;
import harry.testmod.init.blocks.Pedestal;
import harry.testmod.init.blocks.RFGenerator;
import harry.testmod.tileentity.PedestalTileEntity;
import harry.testmod.tileentity.special.PedestalTESR;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockInit 
{
	public static Block microwave_idle, microwave_active;
	public static Block pedestal;
	public static Block rf_generator;
	
	public static void init()
	{
		microwave_active = new Microwave("microwave_active", true).setLightLevel(0.75F);
		microwave_idle = new Microwave("microwave_idle", false);
		pedestal = new Pedestal("pedestal");
		rf_generator = new RFGenerator("rf_generator", 3.0F, 5.0F);
	}
	
	public static void register()
	{
		ForgeRegistries.BLOCKS.register(microwave_active);
		registerBlock(microwave_idle);
		initModel(pedestal);
		registerBlock(rf_generator);
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
	
	@SideOnly(Side.CLIENT)
    public static void initModel(Block block) 
    {
		ForgeRegistries.BLOCKS.register(block);
		block.setCreativeTab(TestMod.testtab);
		ItemBlock item = new ItemBlock(block);
		item.setRegistryName(block.getRegistryName());
		ForgeRegistries.ITEMS.register(item);
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(block.getRegistryName(), "inventory"));
        ClientRegistry.bindTileEntitySpecialRenderer(PedestalTileEntity.class, new PedestalTESR());
    }

}
