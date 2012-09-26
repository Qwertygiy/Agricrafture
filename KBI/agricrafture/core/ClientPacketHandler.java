package KBI.agricrafture.core;

import KBI.agricrafture.api.*;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.src.*;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;
import java.util.logging.Level;
import cpw.mods.fml.common.FMLLog;

public class ClientPacketHandler implements IPacketHandler 
{
	@Override
	public void onPacketData(NetworkManager network, Packet250CustomPayload packet, Player player) 
	{
		FMLLog.log(Level.FINE, "Packet recieved.");
		ByteArrayDataInput dat = ByteStreams.newDataInput(packet.data);
		int x = dat.readInt();
		int y = dat.readInt();
		int z = dat.readInt();
		int typ = dat.readInt();
		if(typ == 0)
		{
			boolean dowork = dat.readBoolean();
			int[] itemlist = new int[33 - 6];
			for(int w = 0; w < 33 - 6; w++)
			{
				itemlist[w] = dat.readInt();
			}
			
			/*
			World world = AgricraftureMain.proxy.getClientWorld();
			TileEntity te = world.getBlockTileEntity(x, y, z);
			if (te instanceof TileEntityCulinary)
			{
				TileEntityCulinary tec = (TileEntityCulinary) te;
				tec.handlePacketData(itemlist);
				if(dowork == true)
				{
					tec.workItem();
				}
			}*/
		}
		FMLLog.log(Level.FINE, "Client packet reception complete.");
	}

	/*
	public static Packet getPacketCulinary(TileEntityCulinary tec) 
	{
		FMLLog.log(Level.FINE, "Sending packet.");
		ByteArrayOutputStream bos = new ByteArrayOutputStream(140);
		DataOutputStream dos = new DataOutputStream(bos);
		int x = tec.xCoord;
		int y = tec.yCoord;
		int z = tec.zCoord;
		int typ = 0;
		boolean dowork = tec.shouldWork;
		int clicks = tec.workTime;
		try {
			dos.writeInt(x);
			dos.writeInt(y);
			dos.writeInt(z);
			dos.writeInt(typ);
			dos.writeBoolean(dowork);
			dos.writeInt(clicks);
			if(dowork == true)
			{
				tec.shouldWork = false;
				tec.workTime = tec.workTime + 1;
			}
		} catch (IOException e) {
			//IMPOSSIBLE?
		}
		try {
			for (int q = 0; q < 11 - 2; q++)
			{
				ItemStack is = tec.getStackInSlot(q);
				if(is == null || is.stackSize == 0)
				{
					dos.writeInt(0);
					dos.writeInt(0);
					dos.writeInt(0);
				}
				else
				{
					dos.writeInt(is.itemID);
					dos.writeInt(is.getItemDamage());
					dos.writeInt(is.stackSize);
				}
			}
		} catch (IOException e) {
			//IMPOSSIBLE?
		}
		Packet250CustomPayload pkt = new Packet250CustomPayload();
		pkt.channel = "Agricrafture";
		pkt.data = bos.toByteArray();
		pkt.length = bos.size();
		pkt.isChunkDataPacket = true;
		FMLLog.log(Level.FINE, "Packet sent.");
		return pkt;
	}
	*/
}