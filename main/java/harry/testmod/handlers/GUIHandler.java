package harry.testmod.handlers;

import harry.testmod.Reference;
import harry.testmod.container.ContainerMicrowave;
import harry.testmod.container.ContainerRFGenerator;
import harry.testmod.gui.GuiMicrowave;
import harry.testmod.gui.GuiRFGenerator;
import harry.testmod.tileentity.TileEntityMicrowave;
import harry.testmod.tileentity.TileEntityRFGenerator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GUIHandler implements IGuiHandler
{
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) 
	{
		TileEntity entity = world.getTileEntity(new BlockPos(x, y, z));
		
		if(entity != null)
		{
			switch(ID)
			{
			case Reference.GUI_MICROWAVE:
				if(entity instanceof TileEntityMicrowave)
				{
					return new ContainerMicrowave(player.inventory, (TileEntityMicrowave)entity);
				}
			case Reference.GUI_RF_GENERATOR:
				if(entity instanceof TileEntityRFGenerator)
				{
					return new ContainerRFGenerator(player.inventory, (TileEntityRFGenerator)entity);
				}
				return null;
			}
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) 
	{
		TileEntity entity = world.getTileEntity(new BlockPos(x, y, z));
		
		if(entity != null)
		{
			switch(ID)
			{
			case Reference.GUI_MICROWAVE:
				if(entity instanceof TileEntityMicrowave)
				{
					return new GuiMicrowave(player.inventory, (TileEntityMicrowave)entity);
				}
			case Reference.GUI_RF_GENERATOR:
				if(entity instanceof TileEntityRFGenerator)
				{
					return new GuiRFGenerator(player.inventory, (TileEntityRFGenerator)entity);
				}
				return null;
			}
		}
		return null;
	}

}
