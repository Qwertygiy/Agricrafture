package KBI.agricrafture.core;

import net.minecraft.src.*;
import KBI.agricrafture.api.*;
import java.util.logging.Level;
import cpw.mods.fml.common.FMLLog;

import org.lwjgl.opengl.GL11;

public class GuiCulinary extends GuiContainer 
{

		private TileEntityCulinary te;

        public GuiCulinary (InventoryPlayer inventoryPlayer, TileEntityCulinary tileEntity) 
		{
                //the container is instanciated and passed to the superclass for handling
                super(new ContainerCulinary(inventoryPlayer, tileEntity));
				te = tileEntity;
        }

        @Override
        protected void drawGuiContainerForegroundLayer() 
		{
                //draw text and stuff here
                //the parameters for drawString are: string, x, y, color
                fontRenderer.drawString(StatCollector.translateToLocal("tile.blockCulinary.name"), 8, 6, 4210752);
                //draws "Inventory" or your regional equivalent
                fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
        }

        @Override
        protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) 
		{
                int texture = mc.renderEngine.getTexture("/KBI/agricrafture/resources/culinarygui.png");
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                this.mc.renderEngine.bindTexture(texture);
                int x = (this.width - this.xSize) / 2;
                int y = (this.height - this.ySize) / 2;
                this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
				
				if (this.te.isWorking())
				{
					int var7 = this.te.getWorkProgressScaled(24);
					this.drawTexturedModalRect(x + 101, y + 34, 176, 1, var7 + 1, 16);
				}
        }
		
		@Override
		public void initGui()
		{
			FMLLog.log(Level.FINE, "Initiating GuiCulinary.");
			int x = (this.width - this.xSize) / 2;
            int y = (this.height - this.ySize) / 2;
			this.controlList.add(new GuiButton(1420, x + 52, y + 33, 34, 18, "Work"));
			FMLLog.log(Level.FINE, "GuiCulinary intitiated.");
		}
		
		protected void actionPerformed(GuiButton par1GuiButton)
		{
			if (par1GuiButton.enabled)
			{
				if (par1GuiButton.id == 1420)
				{
					//te.workItem();
					te.shouldWork = true;
					//PacketHandlerCore.getPacketCulinary(te);
				}
			}
		}
}