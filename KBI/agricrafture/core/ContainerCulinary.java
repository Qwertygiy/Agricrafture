package KBI.agricrafture.core;

import net.minecraft.src.*;
import KBI.agricrafture.api.*;
import java.util.logging.Level;
import cpw.mods.fml.common.FMLLog;

public class ContainerCulinary extends Container 
{

        protected TileEntityCulinary tileEntity;
       
        public ContainerCulinary (InventoryPlayer inventoryPlayer, TileEntityCulinary te)
		{
				FMLLog.log(Level.FINE, "ContainerCulinary intitiated");
                tileEntity = te;

                //the Slot constructor takes the IInventory and the slot number in that it binds to
                //and the x-y coordinates it resides on-screen
				//0-5 are ingredients
                addSlotToContainer(new Slot(tileEntity, 0, 7, 15));
				addSlotToContainer(new Slot(tileEntity, 1, 25, 15));
				addSlotToContainer(new Slot(tileEntity, 2, 7, 33));
				addSlotToContainer(new Slot(tileEntity, 3, 25, 33));
				//addSlotToContainer(new Slot(tileEntity, 4, 7, 51));
				//addSlotToContainer(new Slot(tileEntity, 5, 25, 51));
				//tool slot, for knife, spoon, etc.
				addSlotToContainer(new Slot(tileEntity, 6 - 2, 60, 7));
				//dish slot, for plate, bowl, jar, etc.
				addSlotToContainer(new Slot(tileEntity, 7 - 2, 60, 61));
				//main output
				addSlotToContainer(new Slot(tileEntity, 8 - 2, 147, 35));
				//secondary output 1
				addSlotToContainer(new Slot(tileEntity, 9 - 2, 147, 7));
				//secondary output 2
				addSlotToContainer(new Slot(tileEntity, 10 - 2, 147, 61));

                //commonly used vanilla code that adds the player's inventory
                bindPlayerInventory(inventoryPlayer);
				FMLLog.log(Level.FINE, "ContainerCulinary initiation complete.");
				FMLLog.log(Level.FINE, new StringBuilder().append("Number of slots: ").append(this.inventoryItemStacks.size()).toString());
        }

        @Override
        public boolean canInteractWith(EntityPlayer player) 
		{
                return tileEntity.isUseableByPlayer(player);
        }


        protected void bindPlayerInventory(InventoryPlayer inventoryPlayer) 
		{
                for (int i = 0; i < 3; i++) 
				{
                        for (int j = 0; j < 9; j++) 
						{
                                addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
                        }
                }

                for (int i = 0; i < 9; i++) 
				{
                        addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 142));
                }
        }
		
		@Override
		public Slot getSlot(int par1)
		{
			if(par1 < this.inventorySlots.size())
			{
				return (Slot)this.inventorySlots.get(par1);
			}
			else
			{
				return (Slot)null;
			}
		}
		
		@Override
        public ItemStack transferStackInSlot(int slot) 
		{
                ItemStack stack = null;
                Slot slotObject = (Slot) inventorySlots.get(slot);

                //null checks and checks if the item can be stacked (maxStackSize > 1)
                if (slotObject != null && slotObject.getHasStack()) 
				{
                        ItemStack stackInSlot = slotObject.getStack();
                        stack = stackInSlot.copy();

                        //merges the item into player inventory since its in the tileEntity
                        if (slot >= 0 && slot <= 10 - 2)
						{
                                if (!mergeItemStack(stackInSlot, 11 - 2, inventorySlots.size(), true)) 
								{
                                        return null;
                                }
                        //places it into the tileEntity is possible since its in the player inventory
                        } 
						else if (!mergeItemStack(stackInSlot, 0, 7 - 2, false)) 
						{
                                return null;
                        }

                        if (stackInSlot.stackSize == 0) 
						{
                                slotObject.putStack(null);
                        } 
						else {
                                slotObject.onSlotChanged();
                        }
                }

                return stack;
        }
}