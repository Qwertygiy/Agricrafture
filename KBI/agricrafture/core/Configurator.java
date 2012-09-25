package KBI.agricrafture.core;

import KBI.agricrafture.api.*;

import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;
import cpw.mods.fml.common.FMLLog;
import java.util.logging.Level;
import java.lang.Integer;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Configurator
{

	public static int culinaryTableId;
	public static int stoveIdleId;
	public static int stoveActiveId;
	public static boolean useFurnaceRecipesInStove;
	
	public static void init(Configuration cfg, File suggestedCfg)
	{
		cfg = new Configuration(suggestedCfg);
		try {
			cfg.load();
			culinaryTableId = cfg.getOrCreateIntProperty("Culinary Table ID", "block", 1420).getInt(1420);
			stoveIdleId = cfg.getOrCreateIntProperty("Inactive Stovetop Block ID", "block", 1421).getInt(1421);
			stoveActiveId = cfg.getOrCreateIntProperty("Active Stovetop Block ID", "block", 1422).getInt(1422);
			Property uFRIS = cfg.getOrCreateBooleanProperty("Use Furnace recipe list for the Stovetop", "general", true);
			uFRIS.comment = "If true, Furnace smelting can be done in the Stovetop and Stovetop cooking can be done in Furnaces";
			useFurnaceRecipesInStove = uFRIS.getBoolean(true);

			/* Save the Configuration file */
			cfg.save();
		} catch (Exception e) {
			FMLLog.log(Level.SEVERE, e, "Agricrafture has a problem loading its configuration");
		} finally {
			cfg.save();
		}
	}
	
}