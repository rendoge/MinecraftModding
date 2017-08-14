package harry.testmod.gui;

import java.util.ArrayList;
import java.util.List;

import harry.testmod.container.ContainerRFGenerator;
import harry.testmod.tileentity.TileEntityRFGenerator;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.ProgressManager.ProgressBar;

public class GuiRFGenerator extends GuiContainer
{
	
	private static final ResourceLocation background2 = new ResourceLocation("tm:textures/gui/rfgenerator.png");
	private TileEntityRFGenerator te;
	private IInventory playerInv;
	
	public GuiRFGenerator(IInventory playerInv, TileEntityRFGenerator te)
	{
		super(new ContainerRFGenerator(playerInv, te));
		
		this.te = te;
		this.playerInv = playerInv;
		
		this.xSize = 176;
		this.ySize = 166;
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) 
	{
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		this.mc.getTextureManager().bindTexture(background2);
		this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) 
	{
		String s = this.te.getDisplayName().getUnformattedText();
		this.fontRenderer.drawString(s, 88 - this.fontRenderer.getStringWidth(s) / 2, 6, 4210752);
		this.fontRenderer.drawString(this.playerInv.getDisplayName().getUnformattedText(), 8, 75, 4210752);
		this.fontRenderer.drawString("RF/t: " + this.te.getField(3), 36, 25, 900000);
		this.fontRenderer.drawString("RF: " + this.te.getField(0), 36, 36, 500000);
		this.fontRenderer.drawString("Cooldown: " + (this.te.getField(2)/20) + "s", 36, 47, 600000);
		
		this.mc.getTextureManager().bindTexture(background2);
		this.drawTexturedModalRect(152, 9, 177, 0, 19, 70 - getProgressLevel(70));
	}
	
	private int getProgressLevel(int progressIndicatorPixelHeight)
	{
		int rf = this.te.getField(0);
		int maxRF = this.te.getField(1);
		return maxRF != 0 && rf != 0 ? (rf * progressIndicatorPixelHeight) / maxRF : 0;
	}
}

