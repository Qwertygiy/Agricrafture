package KBI.agricrafture.core;

import net.minecraft.src.*;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiStove extends GuiContainer
{
    private TileEntityStove furnaceInventory;

    public GuiStove(InventoryPlayer par1InventoryPlayer, TileEntityStove par2TileEntityFurnace)
    {
        super(new ContainerStove(par1InventoryPlayer, par2TileEntityFurnace));
        this.furnaceInventory = par2TileEntityFurnace;
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer()
    {
        this.fontRenderer.drawString(StatCollector.translateToLocal("tile.blockStoveIdle.name"), 60, 6, 4210752);
        this.fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the items)
     */
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        int var4 = this.mc.renderEngine.getTexture("/KBI/agricrafture/resources/stovegui.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(var4);
        int var5 = (this.width - this.xSize) / 2;
        int var6 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var5, var6, 0, 0, this.xSize, this.ySize);
        int var7;

        if (this.furnaceInventory.isBurning1())
        {
            var7 = this.furnaceInventory.getBurnTimeRemainingScaled1(12);
            this.drawTexturedModalRect(var5 + 9, var6 + 30 + 12 - var7, 176, 12 - var7, 14, var7 + 2);
			//draw upside down one once I know this all works anyway
        }
		
		if (this.furnaceInventory.isBurning2())
        {
            var7 = this.furnaceInventory.getBurnTimeRemainingScaled2(12);
            this.drawTexturedModalRect(var5 + 99, var6 + 30 + 12 - var7, 176, 12 - var7, 14, var7 + 2);
			//draw upside down one once I know this all works anyway
        }

        var7 = this.furnaceInventory.getCookProgressScaled1(24);
        this.drawTexturedModalRect(var5 + 27, var6 + 12, 176, 14, var7 + 1, 16);
		
		var7 = this.furnaceInventory.getCookProgressScaled2(24);
        this.drawTexturedModalRect(var5 + 27, var6 + 82, 176, 14, var7 + 1, 16);
		
		var7 = this.furnaceInventory.getCookProgressScaled3(24);
        this.drawTexturedModalRect(var5 + 117, var6 + 12, 176, 14, var7 + 1, 16);
		
		var7 = this.furnaceInventory.getCookProgressScaled4(24);
        this.drawTexturedModalRect(var5 + 117, var6 + 82, 176, 14, var7 + 1, 16);
    }
}