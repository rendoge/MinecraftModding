package harry.testmod.tileentity;

import harry.testmod.container.ContainerMicrowave;
import harry.testmod.init.blocks.Microwave;
import net.minecraft.client.renderer.texture.ITickable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.SlotFurnaceFuel;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityMicrowave extends TileEntityLockable implements ISidedInventory
{	
	private static final int[] slots_top = new int[]{0};
	private static final int[] slots_bottom = new int[]{2, 1};
	private static final int[] slots_side = new int[]{1};
	
	private NonNullList<ItemStack> microwave_stacks = NonNullList.<ItemStack>withSize(3, ItemStack.EMPTY);
	
	private int burn_time, current_burn_time, cook_time, total_cook_time;
	private String custom_name;
	
	@Override
	public int getSizeInventory() 
	{
		return this.microwave_stacks.size();
	}
	
	@Override
	public boolean isEmpty() 
	{
		for(ItemStack stack : this.microwave_stacks)
		{
			if(!stack.isEmpty())
			{
				return false;
			}
		}
		
		return true;
	}
	
	@Override
	public ItemStack getStackInSlot(int index) 
	{
		return this.microwave_stacks.get(index);
	}
	
	@Override
	public ItemStack decrStackSize(int index, int count) 
	{
		return ItemStackHelper.getAndSplit(microwave_stacks, index, count);
	}
	
	@Override
	public ItemStack removeStackFromSlot(int index) 
	{
		return ItemStackHelper.getAndRemove(microwave_stacks, index);
	}
	
	@Override
	public void setInventorySlotContents(int index, ItemStack stack)
	{
		ItemStack stack2 = this.microwave_stacks.get(index);
		boolean flag = !stack.isEmpty() && stack.isItemEqual(stack2) && ItemStack.areItemStackTagsEqual(stack, stack2);
		microwave_stacks.set(index, stack);
		if(stack.getCount() > this.getInventoryStackLimit()) stack.setCount(this.getInventoryStackLimit());
		
		if(index == 0 && !flag)
		{
			this.total_cook_time = this.getCookTime(stack);
			this.cook_time = 0;
			this.markDirty();
		}
	}
	
	@Override
	public String getName() 
	{
		return this.hasCustomName() ? this.custom_name : "container.microwave";
	}
	
	@Override
	public boolean hasCustomName() 
	{
		return this.custom_name != null && !this.custom_name.isEmpty();
	}
	
	public void setCustomInventoryName(String name)
	{
		custom_name = name;
	}
	
	public static void registerFixesMicrowave(DataFixer fixer)
	{
		fixer.registerWalker(FixTypes.BLOCK_ENTITY, new ItemStackDataLists(TileEntityMicrowave.class, new String[]{"Items"}));
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) 
	{
		super.writeToNBT(compound);
		compound.setInteger("MicrowaveBurnTime", (short)this.burn_time);
		compound.setInteger("MicrowaveCookTime", (short)this.cook_time);
		compound.setInteger("MicrowaveCookTimeTotal", (short)this.total_cook_time);
		ItemStackHelper.saveAllItems(compound, microwave_stacks);
		
		if(this.hasCustomName())
		{
			compound.setString("MicrowaveCustomName", this.custom_name);
		}
		
		return compound;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		this.microwave_stacks = NonNullList.<ItemStack>withSize(this.getSizeInventory(), ItemStack.EMPTY);
		ItemStackHelper.loadAllItems(compound, microwave_stacks);
		this.burn_time = compound.getInteger("MicrowaveBurnTime");
		this.cook_time = compound.getInteger("MicrowaveCookTime");
		this.total_cook_time = compound.getInteger("MicrowaveCookTimeTotal");
		this.current_burn_time = getItemBurnTime(this.microwave_stacks.get(1));
		
		if(compound.hasKey("CustomName", 8))
		{
			this.custom_name = compound.getString("MicrowaveCustomName");
		}
	}
	
	@Override
	public int getInventoryStackLimit() 
	{
		return 64;
	}
	
	public boolean isBurning()
	{
		return this.burn_time > 0;
	}
	
	@SideOnly(Side.CLIENT)
	public static boolean isBurning(IInventory inventory)
	{
		return inventory.getField(0) > 0;
	}
	
	public void update()
	{
		boolean flag = this.isBurning();
		boolean flag1 = false;
		
		if(this.isBurning()) this.burn_time--;
		
		if(!this.world.isRemote)
		{
			ItemStack stack = this.microwave_stacks.get(1);
			
			if(this.isBurning() || !stack.isEmpty() && !((ItemStack)this.microwave_stacks.get(0)).isEmpty())
			{
				if(!this.isBurning() && this.canSmelt())
				{
					this.burn_time = getItemBurnTime(stack);
					this.current_burn_time = this.burn_time;
					
					if(this.isBurning())
					{
						flag1 = true;
						if(!stack.isEmpty())
						{
							Item item = stack.getItem();
							stack.shrink(1);
							
							if(stack.isEmpty())
							{
								ItemStack stack2 = item.getContainerItem(stack);
								this.microwave_stacks.set(1, stack2);
							}
						}
					}
				}
				
				if(this.isBurning() && this.canSmelt())
				{
					this.cook_time++;
					if(this.cook_time == this.total_cook_time)
					{
						this.cook_time = 0;
						this.total_cook_time = this.getCookTime(this.microwave_stacks.get(0));
						this.smeltItem();
						flag1 = true;
					}
				}
				else
				{
					this.cook_time = 0;
				}
			}
			else if(!this.isBurning() && this.cook_time > 0)
			{
				this.cook_time = MathHelper.clamp(cook_time - 2, 0, this.total_cook_time);
			}
			
			if(flag != this.isBurning())
			{
				flag1 = true;
				Microwave.setState(this.isBurning(), world, pos);
			}
		}
		
		if(flag1)
		{
			this.markDirty();
		}
	}
	
	public int getCookTime(ItemStack stack)
	{
		return 200;
	}
	
	private boolean canSmelt()
	{
		if(((ItemStack)this.microwave_stacks.get(0)).isEmpty()) return false;
		
		else
		{
			ItemStack stack = FurnaceRecipes.instance().getSmeltingResult(this.microwave_stacks.get(0));
			if(stack.isEmpty()) return false;
			
			else
			{
				ItemStack stack1 = this.microwave_stacks.get(2);
				if(stack1.isEmpty()) return true;
				else if(!stack1.isItemEqual(stack)) return false;
				else if(stack1.getCount() + stack.getCount() <= this.getInventoryStackLimit() && stack1.getCount() + stack.getCount() <= stack1.getMaxStackSize()) return true;
				else return stack1.getCount() + stack.getCount() <= stack.getMaxStackSize();
			}
		}
	}
	
	public void smeltItem()
	{
		if(this.canSmelt())
		{
			ItemStack stack0 = this.microwave_stacks.get(0);
			ItemStack stack1 = FurnaceRecipes.instance().getSmeltingResult(stack0);
			ItemStack stack2 = this.microwave_stacks.get(2);
			
			if(stack2.isEmpty()) this.microwave_stacks.set(2, stack1.copy());
			else if(stack2.getItem() == stack1.getItem()) stack2.grow(stack1.getCount());
			stack0.shrink(1);
		}
	}
	
	public static int getItemBurnTime(ItemStack stack)
	{
		if(stack.isEmpty()) return 0;
		else
		{
			int burnTime = net.minecraftforge.event.ForgeEventFactory.getItemBurnTime(stack);
			if(burnTime >= 0) return burnTime;
			Item item = stack.getItem();
			
			if(item == Items.LAVA_BUCKET) return 20000;
			if(item == Items.BLAZE_POWDER) return 1000;
			if(item == Items.BLAZE_ROD) return 2500;
			if(item == Item.getItemFromBlock(Blocks.MAGMA)) return 5000;
			
			else return 300;
		}
	}
	
	public static boolean isItemFuel(ItemStack stack)
	{
		return getItemBurnTime(stack) > 0;
	}
	
	@Override
	public boolean isUsableByPlayer(EntityPlayer player)
	{
		if(this.world.getTileEntity(pos) != this) return false;
		else
		{
			return player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
		}
	}
	
	@Override
	public void openInventory(EntityPlayer player) {}
	@Override
	public void closeInventory(EntityPlayer player) {}
	
	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) 
	{
		if(index == 2) return false;
		else if(index != 1) return true;
		else
		{
			ItemStack stack1 = this.microwave_stacks.get(1);
			return isItemFuel(stack) || SlotFurnaceFuel.isBucket(stack) && stack1.getItem() != Items.BUCKET;
		}
	}
	
	@Override
	public int[] getSlotsForFace(EnumFacing side) 
	{
		if(side == EnumFacing.DOWN) return slots_bottom;
		else return side == EnumFacing.UP ? slots_top : slots_side;
	}
	
	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction)
	{
		return this.isItemValidForSlot(index, itemStackIn);
	}
	
	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) 
	{
		if (direction == EnumFacing.DOWN && index == 1)
        {
            Item item = stack.getItem();

            if (item != Items.WATER_BUCKET && item != Items.BUCKET)
            {
                return false;
            }
        }

        return true;
	}
	
	@Override
	public String getGuiID()
	{
		return "tm:microwave";
	}
	
	@Override
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) 
	{
		return new ContainerMicrowave(playerInventory, this);
	}
	
	@Override
	public int getField(int id) 
	{
		switch(id)
		{
		case 0:
			return this.burn_time;
		case 1:
			return this.current_burn_time;
		case 2:
			return this.cook_time;
		case 3:
			return this.total_cook_time;
		default:
			return 0;
		}
	}
	
	@Override
	public void setField(int id, int value)
	{
		switch(id)
		{
		case 0:
			this.burn_time = value;
			break;
		case 1:
			this.current_burn_time = value;
			break;
		case 2:
			this.cook_time = value;
			break;
		case 3:
			this.total_cook_time = value;
		}
	}
	
	@Override
	public int getFieldCount() 
	{
		return 4;
	}
	
	@Override
	public void clear()
	{
		this.microwave_stacks.clear();
	}
	
	net.minecraftforge.items.IItemHandler handlerTop = new net.minecraftforge.items.wrapper.SidedInvWrapper(this, net.minecraft.util.EnumFacing.UP);
    net.minecraftforge.items.IItemHandler handlerBottom = new net.minecraftforge.items.wrapper.SidedInvWrapper(this, net.minecraft.util.EnumFacing.DOWN);
    net.minecraftforge.items.IItemHandler handlerSide = new net.minecraftforge.items.wrapper.SidedInvWrapper(this, net.minecraft.util.EnumFacing.WEST);

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @javax.annotation.Nullable net.minecraft.util.EnumFacing facing)
    {
        if (facing != null && capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            if (facing == EnumFacing.DOWN)
                return (T) handlerBottom;
            else if (facing == EnumFacing.UP)
                return (T) handlerTop;
            else
                return (T) handlerSide;
        return super.getCapability(capability, facing);
    }
}