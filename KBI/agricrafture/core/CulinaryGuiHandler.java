package KBI.agricrafture.core;

import net.minecraft.src.*;
import KBI.agricrafture.api.*;
import cpw.mods.fml.common.network.IGuiHandler;
import java.util.logging.Level;
import cpw.mods.fml.common.FMLLog;

public class CulinaryGuiHandler implements IGuiHandler
{

        @Override
        public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) 
		{
				FMLLog.log(Level.FINE, "Agricrafture getting Server GUI element");
				if(id == 0)
				{
					TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
					if(tileEntity instanceof TileEntityCulinary)
					{
							FMLLog.log(Level.FINE, "Returning new ContainerCulinary...");
							return new ContainerCulinary(player.inventory, (TileEntityCulinary) tileEntity);
					}
					return null;
				}
				if(id == 1)
				{
					TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
					if(tileEntity instanceof TileEntityStove)
					{
						FMLLog.log(Level.FINE, "Returning new ContainerStove...");
						return new ContainerStove(player.inventory, (TileEntityStove) tileEntity);
					}
					return null;
				}
				return null;
        }

        @Override
        public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) 
		{
				FMLLog.log(Level.FINE, "Agricrafture getting Client GUI element");
				if(id == 0)
				{
					TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
					if(tileEntity instanceof TileEntityCulinary)
					{
							FMLLog.log(Level.FINE, "Returning new GuiCulinary...");
							return new GuiCulinary(player.inventory, (TileEntityCulinary) tileEntity);
					}
					return null;
				}
				if(id == 1)
				{
					TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
					if(tileEntity instanceof TileEntityCulinary)
					{
						FMLLog.log(Level.FINE, "Returning new GuiStove...");
						return new GuiStove(player.inventory, (TileEntityStove) tileEntity);
					}
					return null;
				}
				return null;
        }
}