package harry.testmod.handlers;

import harry.testmod.TestMod;
import harry.testmod.init.BlockInit;
import harry.testmod.tileentity.PedestalTileEntity;
import harry.testmod.tileentity.TileEntityMicrowave;
import harry.testmod.tileentity.TileEntityRFGenerator;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class RegistryHandler 
{
	public static void Client()
	{
		
	}
	
	public static void Common()
	{
		BlockInit.init();
		BlockInit.register();
		
		GameRegistry.registerTileEntity(TileEntityMicrowave.class, "microwave");
		GameRegistry.registerTileEntity(PedestalTileEntity.class, "pedestal");
		GameRegistry.registerTileEntity(TileEntityRFGenerator.class, "rfgenerator");
		NetworkRegistry.INSTANCE.registerGuiHandler(TestMod.instance, new GUIHandler());
	}
}
