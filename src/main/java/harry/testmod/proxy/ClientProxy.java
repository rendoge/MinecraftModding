package harry.testmod.proxy;

import harry.testmod.Reference;
import harry.testmod.handlers.RegistryHandler;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy
{
	public void preInit(FMLPreInitializationEvent event)
	{
		super.preInit(event);
		OBJLoader.INSTANCE.addDomain(Reference.MODID);
	}
	
	public void init(FMLInitializationEvent event)
	{
		super.init(event);
		RegistryHandler.Client();
	}
	
	public void postInit(FMLPostInitializationEvent event)
	{
		super.postInit(event);
	}
}
