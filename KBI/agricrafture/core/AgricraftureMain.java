package KBI.agricrafture.core;

import KBI.agricrafture.api.*;

import java.util.logging.Level;
import net.minecraft.src.*;
import net.minecraftforge.common.*;
import net.minecraftforge.oredict.*;
import net.minecraftforge.event.*;
import cpw.mods.fml.common.*;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.NetworkMod.SidedPacketHandler;
import cpw.mods.fml.common.registry.*;
import java.lang.Integer;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Mod( modid = "KBIAgricrafture", name="Agricrafture", version="0.0.0.1")
@NetworkMod(clientSideRequired = true, serverSideRequired = false, clientPacketHandlerSpec = @SidedPacketHandler(channels = {"ChargingBench"}, packetHandler = ClientPacketHandler.class), serverPacketHandlerSpec = @SidedPacketHandler(channels = ("ChargingBench"), packetHandler = ServerPacketHandler.class))
public class AgricraftureMain
{

	@SidedProxy(clientSide = "KBI.agricrafture.core.ClientProxy", serverSide = "KBI.agricrafture.core.CommonProxy")
	public static CommonProxy proxy;
	
	@Instance("Agricrafture")
	public static AgricraftureMain instance;
	
	public static Configuration config;
	
	public static Block culinaryTable;
	public static Block stoveIdle;
	public static Block stoveActive;
	
	@Init
    public void load(FMLInitializationEvent event)
    {
		NetworkRegistry.instance().registerGuiHandler(instance, new CulinaryGuiHandler());
		GameRegistry.registerTileEntity(TileEntityCulinary.class, "CulinaryBoard");
		GameRegistry.registerTileEntity(TileEntityStove.class, "Stovetop");
		
		culinaryTable = new BlockCulinary(Configurator.culinaryTableId);
		GameRegistry.registerBlock(culinaryTable);
		LanguageRegistry.addName(culinaryTable, "Culinary Board");
		GameRegistry.addRecipe(new ItemStack(culinaryTable, 2), new Object[]
                {
                    "xx", Character.valueOf('x'), Block.woodSingleSlab
                });
				
		stoveIdle = new BlockStove(Configurator.stoveIdleId, false);
		GameRegistry.registerBlock(stoveIdle);
		LanguageRegistry.addName(stoveIdle, "Stovetop");
		GameRegistry.addRecipe(new ItemStack(stoveIdle, 1), new Object[]
			{
				"xxx", "yzy", "aaa", Character.valueOf('x'), Item.ingotIron, Character.valueOf('y'), Block.stoneOvenIdle, Character.valueOf('z'), Item.redstone, Character.valueOf('a'), Block.cobblestone
			});
			
		stoveActive = new BlockStove(Configurator.stoveActiveId, true);
		GameRegistry.registerBlock(stoveActive);
		LanguageRegistry.addName(stoveActive, "Stovetop");
		
		/* CulinaryRecipe.registerCulinaryRecipe(new CulinaryRecipe(new LinkedList<ItemStack>[]
			{new ItemStack(Item.ingotIron, 1, 0)
			}, new ItemStack(Item.ingotIron, 1, 0), new ItemStack(Item.ingotIron, 1, 0))); */
	}
	
	@PreInit
     public void preLoad(FMLPreInitializationEvent evt)
     {
		instance = this;
		Configurator.init(config, evt.getSuggestedConfigurationFile());
     }
	 
}