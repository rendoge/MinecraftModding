package harry.tutorialmod.handlers;

import harry.tutorialmod.gen.TutorialOreGen;
import harry.tutorialmod.init.ArmourInit;
import harry.tutorialmod.init.BlockInit;
import harry.tutorialmod.init.ItemInit;
import harry.tutorialmod.init.ToolInit;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class RegistryHandler 
{
	public static void Client()
	{	
		RecipeHandler.registerCrafting();
		RecipeHandler.registerSmelting();
	}
	
	public static void Common()
	{
		ItemInit.init();
		ItemInit.register();
		
		BlockInit.init();
		BlockInit.register();
		
		ToolInit.init();
		ToolInit.register();
		
		ArmourInit.init();
		ArmourInit.register();
		
		GameRegistry.registerWorldGenerator(new TutorialOreGen(), 0);
	}
}
