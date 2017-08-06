package harry.testmod.gui;

import org.lwjgl.opengl.GL11;

import harry.testmod.Reference;
import harry.testmod.container.ContainerMicrowave;
import harry.testmod.tileentity.TileEntityMicrowave;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class GUIMicrowave extends GuiContainer
{
	private static final ResourceLocation background = new ResourceLocation(Reference.MODID + ":" + "textures/gui/GUIMicrowave.png");
	
	private final IInventory tile_microwave;
	private final InventoryPlayer player_inventory;
	
	public GUIMicrowave(InventoryPlayer playerInv, IInventory furnaceInv) 
	{
		super(new ContainerMicrowave(playerInv, furnaceInv));
		tile_microwave = furnaceInv;
		player_inventory = playerInv;
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) 
	{
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) 
	{
		String name = this.tile_microwave.getDisplayName().getUnformattedText();
		this.fontRenderer.drawString(name, this.xSize / 2 - this.fontRenderer.getStringWidth(name) / 2, 6, 4210752);
		this.fontRenderer.drawString(this.player_inventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) 
	{
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(background);
		int i = (this.width - this.xSize) / 2;
		int j = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
		
		if(TileEntityMicrowave.isBurning(tile_microwave))
		{
			int k = this.getBurnLeftScaled(13);
			this.drawTexturedModalRect(i + 56, j + 36 + 12 - k, 176, 12 - k, 14, k + 1);
		}
		
		int l = this.getCookProgressScaled(24);
		this.drawTexturedModalRect(i + 79, j + 34, 176, 14, l + 1, 16);
	}
	
	private int getCookProgressScaled(int pixels)
	{
		int i = this.tile_microwave.getField(2);
		int j = this.tile_microwave.getField(3);
		return j != 0 && i != 0 ? i * pixels / j : 0;
	}
	
	private int getBurnLeftScaled(int pixels)
	{
		int i = this.tile_microwave.getField(1);
		if(i == 0) i = 200;
		return this.tile_microwave.getField(0) * pixels / i;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}