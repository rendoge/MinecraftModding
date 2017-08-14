package harry.testmod.gui;

import harry.testmod.container.ContainerMicrowave;
import harry.testmod.tileentity.TileEntityMicrowave;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiMicrowave extends GuiContainer
{
    private static final ResourceLocation background = new ResourceLocation("tm:textures/gui/microwave.png");
    private final InventoryPlayer playerInventory;
    public TileEntityMicrowave tileMicrowave;
 
    public GuiMicrowave(InventoryPlayer playerInv, TileEntityMicrowave entity)
    {
        super(new ContainerMicrowave(playerInv, entity));
        this.playerInventory = playerInv;
        this.tileMicrowave = entity;
        
        xSize = 176;
        ySize = 166;
    }

    
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        String s = this.tileMicrowave.getDisplayName().getUnformattedText();
        this.fontRenderer.drawString(s, this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2, 6, 4210752);
        this.fontRenderer.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 117, this.ySize - 96 + 2, 4210752);
    }

    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(background);
        this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, this.xSize, this.ySize);
        
        if(this.tileMicrowave.isBurning())
        {
        	int k = this.getBurnTimeRemainingScaled(42);
        	int j = 40 - k;
        	drawTexturedModalRect(guiLeft + 29, guiTop + 65, 176, 0, 40 - j, 10);
        }
        
        int i = this.getCookProgressScaled(24);
        drawTexturedModalRect(guiLeft + 79, guiTop + 34, 176, 10, i + 1, 16);
    }
    
    private int getBurnTimeRemainingScaled(int pixels)
    {
		int i  = this.tileMicrowave.getField(1);
		if(i == 0) i = 200;
		return this.tileMicrowave.getField(0) * pixels / i;
	}
	private int getCookProgressScaled(int pixels)
    {
        int i = this.tileMicrowave.getField(2);
        int j = this.tileMicrowave.getField(3);
        return j != 0 && i != 0 ? i * pixels / j : 0;
    }
}