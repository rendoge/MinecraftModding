package harry.testmod.tileentity;

import cofh.redstoneflux.api.IEnergyProvider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.I18n;

public class TileEntityRFGenerator extends TileEntity implements IInventory, IEnergyProvider, ITickable
{
	private int increasePerTick = 20;
	private int maxRF = 1000000;
	private int currentRF, cooldown;
	
	private NonNullList<ItemStack> inventory = NonNullList.<ItemStack>withSize(1, ItemStack.EMPTY);
	private String customName;
	
	public String getCustomName() 
	{
		return customName;
	}
	
	public void setCustomName(String customName) 
	{
		this.customName = customName;
	}
	
	@Override
	public String getName() 
	{
		return this.hasCustomName() ? this.customName : "container.rf_generator";
	}
	
	@Override
	public boolean hasCustomName() 
	{
		return this.customName != null && !this.customName.isEmpty();
	}
	
	@Override
	public ITextComponent getDisplayName() 
	{
		return this.hasCustomName() ? new TextComponentString(this.getName()) : new TextComponentTranslation(this.getName(), I18n.translateToLocal(this.getName()));
	}
	
	@Override
	public int getSizeInventory() 
	{
		return 1;
	}
	
	@Override
	public ItemStack getStackInSlot(int index)
	{
		return inventory.get(index);
	}
	
	@Override
	public ItemStack decrStackSize(int index, int count)
	{
		return ItemStackHelper.getAndSplit(inventory, index, count);
	}
	
	@Override
	public ItemStack removeStackFromSlot(int index) 
	{
		return ItemStackHelper.getAndRemove(inventory, index);
	}
	
	@Override
	public void setInventorySlotContents(int index, ItemStack stack)
	{
		if(index < 0 || index >= this.getSizeInventory()) return;
		if(stack != null && stack.getCount() > this.getInventoryStackLimit()) stack.setCount(this.getInventoryStackLimit());
		this.inventory.set(index, stack);
		this.markDirty();
	}
	
	@Override
	public int getInventoryStackLimit() 
	{
		return 64;
	}
	
	@Override
	public boolean isUsableByPlayer(EntityPlayer player) 
	{
		return this.world.getTileEntity(this.getPos()) == this && player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
	}
	
	@Override
	public void openInventory(EntityPlayer player) {}
	@Override
	public void closeInventory(EntityPlayer player) {}
	
	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) 
	{
		return stack.getItem() == Items.LAVA_BUCKET || stack.getItem() == Item.getItemFromBlock(Blocks.MAGMA) || stack.getItem() == Items.BLAZE_POWDER || this.currentRF < this.maxRF;
	}
	
	@Override
	public int getField(int id) 
	{
		switch(id)
		{
		case 0:
			return this.currentRF;
		case 1:
			return this.maxRF;
		case 2:
			return this.cooldown;
		case 3:
			return this.increasePerTick;
		}
		
		return 0;
	}
	
	@Override
	public void setField(int id, int value) 
	{
		switch(id)
		{
		case 0:
			this.currentRF = value;
		case 1:
			this.maxRF  = value;
		case 2:
			this.cooldown  = value;
		case 3:
			this.increasePerTick  = value;
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
		this.inventory.clear();
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) 
	{
		NBTTagList list = new NBTTagList();
		for(int i = 0; i < this.getSizeInventory(); i++)
		{
			if(!this.getStackInSlot(i).isEmpty())
			{
				NBTTagCompound stackTag = new NBTTagCompound();
				stackTag.setByte("Slot", (byte)i);
				this.getStackInSlot(i).writeToNBT(stackTag);
				list.appendTag(stackTag);
			}
		}
		
		nbt.setTag("Items", list);
		nbt.setInteger("currentRF", this.currentRF);
	    nbt.setInteger("cooldown", this.cooldown);
	    nbt.setInteger("ipt", this.increasePerTick);

	    if (this.hasCustomName()) nbt.setString("CustomName", this.getCustomName());
	    return super.writeToNBT(nbt);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) 
	{
		NBTTagList list = nbt.getTagList("Items", 10);
		for(int i = 0; i < list.tagCount(); i++)
		{
			NBTTagCompound stackTag = list.getCompoundTagAt(i);
			int slot = stackTag.getByte("Slot") & 255;
			this.setInventorySlotContents(slot, new ItemStack(stackTag));
		}
		this.currentRF = nbt.getInteger("currentRF");
		this.cooldown = nbt.getInteger("cooldown");
		this.increasePerTick = nbt.getInteger("ipt");
		
		if(nbt.hasKey("CustomName", 8)) this.setCustomName(nbt.getString("CustomName"));
		super.readFromNBT(nbt);
	}
	
	@Override
	public int getEnergyStored(EnumFacing from) 
	{
		return this.currentRF;
	}
	
	@Override
	public int getMaxEnergyStored(EnumFacing from)
	{
		return this.maxRF;
	}
	
	@Override
	public boolean canConnectEnergy(EnumFacing from) 
	{
		return true;
	}

	@Override
	public int extractEnergy(EnumFacing from, int maxExtract, boolean simulate) 
	{
		currentRF -= maxExtract;
		return maxExtract;
	}

	@Override
	public boolean isEmpty() 
	{
		for(ItemStack stack: this.inventory)
		{
			if(!stack.isEmpty())
			{
				return false;
			}
		}
		
		return true;
	}
	
	@Override
	public void update() 
	{
		if(this.world != null)
		{
				if(canUse())
				{
					if(this.cooldown <= 0)
					{
						if(this.inventory.get(0).getItem() == Item.getItemFromBlock(Blocks.MAGMA))
						{
							this.cooldown = 150;
							this.increasePerTick = 500;
						}
						else if(this.inventory.get(0).getItem() == Items.LAVA_BUCKET)
						{
							this.cooldown = 300;
							this.increasePerTick = 1000;
						}
						else if(this.inventory.get(0).getItem() == Items.BLAZE_ROD)
						{
							this.cooldown = 75;
							this.increasePerTick = 200;
						}
						else if(this.inventory.get(0).isEmpty()) 
						{
							this.increasePerTick = 0;
						}
						else if(this.currentRF > this.maxRF)
						{
							this.currentRF = maxRF;
						}
						
						this.inventory.get(0).setCount(this.inventory.get(0).getCount() - 1);
					}
				}
			}
			
			if(this.cooldown > 0)
			{
				this.cooldown--;
				if(this.currentRF < this.maxRF)
				{
					this.currentRF += this.increasePerTick;
				}
			}
			
			this.markDirty();
		}

	 private boolean canUse()
	 {
		 if(this.inventory.get(0).isEmpty()) return false;
		 else
		 {
			 if(this.inventory.get(0).getItem() == Items.LAVA_BUCKET || this.inventory.get(0).getItem() == Items.BLAZE_ROD || this.inventory.get(0).getItem() == Item.getItemFromBlock(Blocks.MAGMA))
			 {
				 if(this.currentRF < this.maxRF)
				 {
					 return true;
				 }
			 }
		 }
		 
		 return false;
	 }
	 
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() 
	{
		NBTTagCompound tag = new NBTTagCompound();
		writeToNBT(tag);
		int metadata = getBlockMetadata();
		return new SPacketUpdateTileEntity(this.pos, metadata, tag);
	}
	 
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) 
	{
		readFromNBT(pkt.getNbtCompound());
	}	
}