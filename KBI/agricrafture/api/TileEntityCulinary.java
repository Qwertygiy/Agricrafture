package KBI.agricrafture.api;

import KBI.agricrafture.core.*;
import net.minecraft.src.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Iterator;

import net.minecraftforge.common.ISidedInventory;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

public class TileEntityCulinary extends TileEntity implements IInventory, ISidedInventory 
{

        private ItemStack[] inv = new ItemStack[11 - 2]; //CHANGE TO 11 ONCE FIXED FOR 6-SLOT INVENTORY
		
		public int workTime = 0;
		
		public int recipeClicks = 1;
		
		public boolean shouldWork = false;

        public TileEntityCulinary()
		{
                //inv = new ItemStack[11];
        }
		
		public String getInvName()
		{
			return "tile.blockCulinary.name";
		}
       
        @Override
        public int getSizeInventory() 
		{
                return inv.length;
        }

        @Override
        public ItemStack getStackInSlot(int slot) 
		{
                return inv[slot];
        }
       
        @Override
        public void setInventorySlotContents(int slot, ItemStack stack) 
		{
                inv[slot] = stack;
                if (stack != null && stack.stackSize > getInventoryStackLimit()) {
                        stack.stackSize = getInventoryStackLimit();
                }              
        }

        @Override
        public ItemStack decrStackSize(int slot, int amt) 
		{
                ItemStack stack = getStackInSlot(slot);
                if (stack != null) {
                        if (stack.stackSize <= amt) {
                                setInventorySlotContents(slot, null);
                        } else {
                                stack = stack.splitStack(amt);
                                if (stack.stackSize == 0) {
                                        setInventorySlotContents(slot, null);
                                }
                        }
                }
                return stack;
        }

        @Override
        public ItemStack getStackInSlotOnClosing(int slot) 
		{
                ItemStack stack = getStackInSlot(slot);
                if (stack != null) {
                        setInventorySlotContents(slot, null);
                }
                return stack;
        }
       
        @Override
        public int getInventoryStackLimit() 
		{
                return 64;
        }

        @Override
        public boolean isUseableByPlayer(EntityPlayer player) 
		{
                return worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) == this &&
                player.getDistanceSq(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5) < 64;
        }
		
		@Override
		public int getStartInventorySide(ForgeDirection side)
		{
			if (side == ForgeDirection.EAST) return 0;
			if (side == ForgeDirection.UP) return 6 - 2;
			if (side == ForgeDirection.DOWN) return 7 - 2;
			return 8 - 2;
		}

		@Override
		public int getSizeInventorySide(ForgeDirection side)
		{
			if (side == ForgeDirection.EAST) return 6 - 2;
			if (side == ForgeDirection.DOWN) return 1;
			if (side == ForgeDirection.UP) return 1;
			return 3;
		}

        @Override
        public void openChest() {}

        @Override
        public void closeChest() {}
       
        @Override
        public void readFromNBT(NBTTagCompound tagCompound) {
                super.readFromNBT(tagCompound);
               
                NBTTagList tagList = tagCompound.getTagList("Inventory");
                for (int i = 0; i < tagList.tagCount(); i++) {
                        NBTTagCompound tag = (NBTTagCompound) tagList.tagAt(i);
                        byte slot = tag.getByte("Slot");
                        if (slot >= 0 && slot < inv.length) {
                                inv[slot] = ItemStack.loadItemStackFromNBT(tag);
                        }
                }
				
				this.workTime = tagCompound.getShort("ClicksLeft");
        }

        @Override
        public void writeToNBT(NBTTagCompound tagCompound) 
		{
				tagCompound.setShort("ClicksLeft", (short)this.workTime);
				
                NBTTagList itemList = new NBTTagList();
                for (int i = 0; i < inv.length; i++) {
                        ItemStack stack = inv[i];
                        if (stack != null) {
                                NBTTagCompound tag = new NBTTagCompound();
                                tag.setByte("Slot", (byte) i);
                                stack.writeToNBT(tag);
                                itemList.appendTag(tag);
                        }
                }
                tagCompound.setTag("Inventory", itemList);
				
				super.writeToNBT(tagCompound);
        }
		
		@Override
		public Packet getDescriptionPacket() {
			return PacketHandlerCore.getPacketCulinary(this);
		}
		
		public void handlePacketData(int[] itemdata)
		{
			for(int q = 0; q < 11 - 2; q++)
			{
				if(itemdata[(q * 3) + 2] != 0)
				{
					ItemStack stackinslot = new ItemStack(itemdata[q * 3], itemdata[(q * 3) + 2], itemdata[(q + 3) + 1]);
					this.inv[q] = stackinslot;
				}
				else
				{
					this.inv[q] = null;
				}
			}
		}
		
		@SideOnly(Side.CLIENT)

		/**
		 * Returns an integer between 0 and the passed value representing how close the current item is to being completely
		 * done
		 */
		public int getWorkProgressScaled(int par1)
		{
			return this.workTime * par1 / this.recipeClicks;
		}
		
		public boolean isWorking()
		{
			return this.workTime > 0;
		}
		
		/**
		 * Check to see if the current recipe can be completed
		 *
		 */
		private boolean canWork()
		{
			//makes sure input is not empty
			if (this.inv[0] == null && this.inv[1] == null && this.inv[2] == null && this.inv[3] == null /* && this.inv[4] == null && this.inv[5] == null */)
			{
				return false;
			}
			
			//makes sure there is a tool in the slot; if not, then it would be a crafting recipe instead of culinary.
			//There is no check on the dish slot as not all recipes may require them.
			if (this.inv[6 - 2] == null)
			{
				return false;
			}
			
			LinkedList recipes = CulinaryRecipe.recipes;
			Iterator iterate = recipes.iterator();
			boolean hasfound = false;
			LinkedList<ItemStack> fail = new LinkedList<ItemStack>();
			fail.add(new ItemStack(0, 0, 0));
			CulinaryRecipe recipematch = new CulinaryRecipe(fail, new ItemStack(0, 0, 0), new ItemStack(0, 0, 0));

            while (iterate.hasNext())
            {
                CulinaryRecipe thisrecipe = (CulinaryRecipe)iterate.next();
				if (thisrecipe.matches(this))
				{
					hasfound = true;
					recipematch = thisrecipe;
				}
			}
			
			if (hasfound == false)
			{
				return false;
			}
			
			if (this.inv[8 - 2] != null && this.inv[8 - 2].itemID != recipematch.result.itemID)
			{
				return false;
			}
			
			if (this.inv[8 - 2] != null && this.inv[8 - 2].stackSize >= recipematch.result.getMaxStackSize())
			{
				return false;
			}
			
			if (recipematch.output1 != null)
			{
				if (recipematch.output2 != null)
				{
					if (this.inv[9 - 2] != null && this.inv[9 - 2].itemID != recipematch.output1.itemID && this.inv[9 - 2].itemID != recipematch.output2.itemID)
					{
						return false;
					}
					
					if (this.inv[9 - 2] != null && this.inv[9 - 2].itemID == recipematch.output1.itemID && this.inv[9 - 2].stackSize >= recipematch.output1.getMaxStackSize())
					{
						return false;
					}
					
					if (this.inv[9 - 2] != null && this.inv[9 - 2].itemID == recipematch.output2.itemID && this.inv[9 - 2].stackSize >= recipematch.output2.getMaxStackSize())
					{
						return false;
					}
					
					if (this.inv[10 - 2] != null && this.inv[10 - 2].itemID != recipematch.output1.itemID && this.inv[10 - 2].itemID != recipematch.output2.itemID)
					{
						return false;
					}
					
					if (this.inv[10 - 2] != null && this.inv[10 - 2].itemID == recipematch.output1.itemID && this.inv[10 - 2].stackSize >= recipematch.output1.getMaxStackSize())
					{
						return false;
					}
					
					if (this.inv[10 - 2] != null && this.inv[10 - 2].itemID == recipematch.output2.itemID && this.inv[10 - 2].stackSize >= recipematch.output2.getMaxStackSize())
					{
						return false;
					}
				}
				
				if (this.inv[9 - 2] != null && this.inv[9 - 2].itemID != recipematch.output1.itemID)
				{
					return false;
				}
				
				if (this.inv[9 - 2] != null && this.inv[9 - 2].itemID == recipematch.output1.itemID && this.inv[9 - 2].stackSize >= recipematch.output1.getMaxStackSize())
				{
					return false;
				}
				
				if (this.inv[10 - 2] != null && this.inv[10 - 2].itemID != recipematch.output1.itemID)
				{
					return false;
				}
				
				if (this.inv[10 - 2] != null && this.inv[10 - 2].itemID == recipematch.output1.itemID && this.inv[10 - 2].stackSize >= recipematch.output1.getMaxStackSize())
				{
					return false;
				}
			}
			
			return true;
		}
		
		public void workItem()
		{
			if(this.canWork())
			{
				LinkedList recipes = CulinaryRecipe.recipes;
				Iterator iterate = recipes.iterator();
				boolean hasfound = false;
				LinkedList<ItemStack> fail = new LinkedList<ItemStack>();
				fail.add(new ItemStack(0, 0, 0));
				CulinaryRecipe recipematch = new CulinaryRecipe(fail, new ItemStack(0, 0, 0), new ItemStack(0, 0, 0));

				while (iterate.hasNext() && hasfound == false)
				{
					CulinaryRecipe thisrecipe = (CulinaryRecipe)iterate.next();
					if (thisrecipe.matches(this))
					{
						hasfound = true;
						recipematch = thisrecipe;
					}
				}
				
				//we know that the recipematch exists because we used the same code in canWork.
				
				if(!isWorking())
				{
					this.recipeClicks = recipematch.clicks;
				}
				
				this.workTime = this.workTime + 1;
				
				if(this.workTime >= this.recipeClicks)
				{
					if(this.inv[8 - 2] == null)
					{
						this.inv[8 - 2] = recipematch.result.copy();
					}
					else if (this.inv[8 - 2].isItemEqual(recipematch.result))
					{
						this.inv[8 - 2].stackSize += recipematch.result.stackSize;
					}
					
					if(recipematch.output1 != null)
					{
						//if top stack matches
						if (this.inv[9 - 2].isItemEqual(recipematch.output1))
						{
							this.inv[9 - 2].stackSize += recipematch.output1.stackSize;
						}
						//if bottom stack matches
						else if (this.inv[10 - 2].isItemEqual(recipematch.output1))
						{
							this.inv[10 - 2].stackSize += recipematch.output1.stackSize;
						}
						//if neither stack matches but top is empty
						else if (this.inv[9 - 2] == null)
						{
							this.inv[9 - 2] = recipematch.output1.copy();
						}
						//if neither stack matches and top is full but bottom is empty
						else if (this.inv[10 - 2] == null)
						{
							this.inv[10 - 2] = recipematch.output1.copy();
						}
						//the recipe will not work if both stacks are not empty and don't match, so no code is needed here.
					}
					
					if(recipematch.output2 != null)
					{
						//if top stack matches
						if (this.inv[9 - 2].isItemEqual(recipematch.output2))
						{
							this.inv[9 - 2].stackSize += recipematch.output2.stackSize;
						}
						//if bottom stack matches
						else if (this.inv[10 - 2].isItemEqual(recipematch.output2))
						{
							this.inv[10 - 2].stackSize += recipematch.output2.stackSize;
						}
						//if neither stack matches but top is empty
						else if (this.inv[9 - 2] == null)
						{
							this.inv[9 - 2] = recipematch.output2.copy();
						}
						//if neither stack matches and top is full but bottom is empty
						else if (this.inv[10 - 2] == null)
						{
							this.inv[10 - 2] = recipematch.output2.copy();
						}
						//the recipe will not work if both stacks are not empty and don't match, so no code is needed here.
					}
					
					for (int q = 0; q < 6 - 2; q++)
					{
						if(this.inv[q] != null)
						{
							--this.inv[q].stackSize;
							if (this.inv[q].stackSize <= 0)
							{
								this.inv[q] = null;
							}
						}
					}
					
					//If tools are going to take damage, put that code here. But for now, they won't.
					
					if(this.inv[7 - 2] != null)
					{
						--this.inv[7 - 2].stackSize;
						if(this.inv[7 - 2].stackSize <= 0)
						{
							this.inv[7 - 2] = null;
						}
					}
				}
			}
			PacketHandlerCore.getPacketCulinary(this);
		}
}

