package KBI.agricrafture.core;

import KBI.agricrafture.api.*;

import java.util.logging.Level;
import cpw.mods.fml.common.FMLLog;
import net.minecraftforge.common.*;
import net.minecraftforge.oredict.*;
import cpw.mods.fml.*;
import net.minecraft.src.*;
import net.minecraftforge.client.MinecraftForgeClient;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.client.FMLClientHandler;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.lang.Number;
import java.io.*;
import java.net.URI;
import java.lang.Integer;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import net.minecraft.client.Minecraft;
import cpw.mods.fml.common.FMLLog;

public class ClientProxy extends CommonProxy
{


	@Override
	public void registerRenderInformation()
	{  
		MinecraftForgeClient.preloadTexture("/KBI/agricrafture/resources/1.png");
	}
	
	@Override
	public World getClientWorld() 
	{
		return FMLClientHandler.instance().getClient().theWorld;
	}
	
	/*
	@Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) 
	{
		FMLLog.log(Level.FINE, "Agricrafture getting Client GUI element");
        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
        if(tileEntity instanceof TileEntityCulinary)
		{
            return new GuiCulinary(player.inventory, (TileEntityCulinary) tileEntity);
        }
        return null;
    }*/
	
}