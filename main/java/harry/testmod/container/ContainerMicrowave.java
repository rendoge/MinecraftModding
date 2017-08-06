package harry.testmod.container;

import harry.testmod.tileentity.TileEntityMicrowave;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.inventory.SlotFurnaceFuel;
import net.minecraft.inventory.SlotFurnaceOutput;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerMicrowave extends Container
{	
	public static IInventory tile_microwave;
    private int cook_time;
    private int total_cook_time;
    private int burn_time;
    private int current_burn_time;

	public ContainerMicrowave(InventoryPlayer inventory, IInventory microwave)
	{
		tile_microwave = microwave;
		
		this.addSlotToContainer(new Slot(microwave, 0, 56, 35));
		this.addSlotToContainer(new SlotFurnaceFuel(microwave, 1, 8, 63));
		this.addSlotToContainer(new SlotFurnaceOutput(inventory.player, microwave, 2, 117, 35));
		
		for(int i = 0; i < 3; i++)
		{
			for(int j = 0; j < 9; j++)
			{ 
				this.addSlotToContainer(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i*18));
			}
		}
		
		for(int k = 0; k < 9; k++)
		{
			this.addSlotToContainer(new Slot(inventory, k, 8 + k * 18, 142));
		}
	}
	
	 public void addListener(IContainerListener listener)
	 {
		super.addListener(listener);
		listener.sendAllWindowProperties(this, this.tile_microwave);
	 }
	 
	 @Override
	 public void detectAndSendChanges() 
	 {
		 super.detectAndSendChanges();
		 
		for(int i = 0; i < this.listeners.size(); i++)
		{
            IContainerListener icontainerlistener = this.listeners.get(i);
            
            if (this.cook_time != this.tile_microwave.getField(2))
            {
                icontainerlistener.sendWindowProperty(this, 2, this.tile_microwave.getField(2));
            }

            if (this.burn_time != this.tile_microwave.getField(0))
            {
                icontainerlistener.sendWindowProperty(this, 0, this.tile_microwave.getField(0));
            }

            if (this.current_burn_time != this.tile_microwave.getField(1))
            {
                icontainerlistener.sendWindowProperty(this, 1, this.tile_microwave.getField(1));
            }
            
            if(this.total_cook_time != this.tile_microwave.getField(3))
            {
            	icontainerlistener.sendWindowProperty(this, 3, this.tile_microwave.getField(3));
            }
		}
		
		this.cook_time = this.tile_microwave.getField(2);
		this.burn_time = this.tile_microwave.getField(0);
		this.current_burn_time = this.tile_microwave.getField(1);
		this.total_cook_time = this.tile_microwave.getField(3);
	}
	
	 @SideOnly(Side.CLIENT)
	 public void updateProgressBar(int id, int data)
	 {
		 this.tile_microwave.setField(id, data);
	 }

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) 
	{
		return this.tile_microwave.isUsableByPlayer(playerIn);
	}
	
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index == 2)
            {
                if (!this.mergeItemStack(itemstack1, 3, 35, true))
                {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(itemstack1, itemstack);
            }
            else if (index != 1 && index != 0)
            {
                if (!FurnaceRecipes.instance().getSmeltingResult(itemstack1).isEmpty())
                {
                    if (!this.mergeItemStack(itemstack1, 0, 1, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (TileEntityFurnace.isItemFuel(itemstack1))
                {
                    if (!this.mergeItemStack(itemstack1, 1, 2, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (index >= 3 && index < 30)
                {
                    if (!this.mergeItemStack(itemstack1, 30, 35, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (index >= 30 && index < 36 && !this.mergeItemStack(itemstack1, 3, 30, false))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 3, 35, false))
            {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty())
            {
                slot.putStack(ItemStack.EMPTY);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount())
            {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, itemstack1);
        }

        return itemstack;
    } 
}
