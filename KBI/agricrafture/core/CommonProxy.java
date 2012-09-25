package KBI.agricrafture.core;

import KBI.agricrafture.api.*;

import java.util.logging.Level;
import cpw.mods.fml.common.FMLLog;
import net.minecraftforge.*;
import cpw.mods.fml.*;
import java.lang.Integer;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.src.*;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;

public class CommonProxy /*implements IGuiHandler*/
{

/**
 * Client side only register stuff...
 */
	public void registerRenderInformation()
	{
	//unused server side.
	}
	
	public World getClientWorld() 
	{
		return null;
	}

	/*
	@Override
        public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) 
		{
				FMLLog.log(Level.FINE, "Agricrafture getting Server GUI element");
                TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
                if(tileEntity instanceof TileEntityCulinary)
				{
                        return new ContainerCulinary(player.inventory, (TileEntityCulinary) tileEntity);
                }
                return null;
        }

        @Override
        public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) 
		{
			return null;
        }
	*/
}