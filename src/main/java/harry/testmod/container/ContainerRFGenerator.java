package harry.testmod.container;

import harry.testmod.tileentity.TileEntityRFGenerator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerRFGenerator extends Container
{
	private TileEntityRFGenerator te;
	
	public ContainerRFGenerator(IInventory playerInv, TileEntityRFGenerator te) 
	{
		this.te = te;
		
		this.addSlotToContainer(new Slot(te, 0, 97, 62));
		
		for (int i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 9; ++j)
            {
                this.addSlotToContainer(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k)
        {
            this.addSlotToContainer(new Slot(playerInv, k, 8 + k * 18, 142));
        }
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn) 
	{
		return this.te.isUsableByPlayer(playerIn);
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) 
	{
		ItemStack previous = ItemStack.EMPTY;
		Slot slot = (Slot) this.inventorySlots.get(index);
		
		if(slot != null && slot.getHasStack())
		{
			ItemStack current = slot.getStack();
			previous = current.copy();
			
			if(index < 1)
			{
				if(!this.mergeItemStack(current, 1, this.inventorySlots.size(), true))
				{
					return ItemStack.EMPTY;	
				}
			}
			else
			{
				if(!this.mergeItemStack(current, 0, 1, false))
				{
					return ItemStack.EMPTY;
				}
			}
			
			if(current.getCount() == 0) slot.putStack((ItemStack) ItemStack.EMPTY);
			else slot.onSlotChanged();
		}
		
		return previous;
	}
}
