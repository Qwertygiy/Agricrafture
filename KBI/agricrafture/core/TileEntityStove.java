package KBI.agricrafture.core;

import net.minecraft.src.*;

import net.minecraftforge.common.ISidedInventory;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.common.registry.GameRegistry;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

public class TileEntityStove extends TileEntity implements IInventory, ISidedInventory
{
    /**
     * The ItemStacks that hold the items currently being used in the furnace
     */
    private ItemStack[] furnaceItemStacks = new ItemStack[10];

    /** The number of ticks that the furnace will keep burning */
    public int furnaceBurnTime1 = 0;
	public int furnaceBurnTime2 = 0;

    /**
     * The number of ticks that a fresh copy of the currently-burning item would keep the furnace burning for
     */
    public int currentItemBurnTime1 = 0;
	public int currentItemBurnTime2 = 0;

    /** The number of ticks that the current item has been cooking for */
    public int furnaceCookTime1 = 0;
	public int furnaceCookTime2 = 0;
	public int furnaceCookTime3 = 0;
	public int furnaceCookTime4 = 0;

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory()
    {
        return this.furnaceItemStacks.length;
    }

    /**
     * Returns the stack in slot i
     */
    public ItemStack getStackInSlot(int par1)
    {
        return this.furnaceItemStacks[par1];
    }

    /**
     * Removes from an inventory slot (first arg) up to a specified number (second arg) of items and returns them in a
     * new stack.
     */
    public ItemStack decrStackSize(int par1, int par2)
    {
        if (this.furnaceItemStacks[par1] != null)
        {
            ItemStack var3;

            if (this.furnaceItemStacks[par1].stackSize <= par2)
            {
                var3 = this.furnaceItemStacks[par1];
                this.furnaceItemStacks[par1] = null;
                return var3;
            }
            else
            {
                var3 = this.furnaceItemStacks[par1].splitStack(par2);

                if (this.furnaceItemStacks[par1].stackSize == 0)
                {
                    this.furnaceItemStacks[par1] = null;
                }

                return var3;
            }
        }
        else
        {
            return null;
        }
    }

    /**
     * When some containers are closed they call this on each slot, then drop whatever it returns as an EntityItem -
     * like when you close a workbench GUI.
     */
    public ItemStack getStackInSlotOnClosing(int par1)
    {
        if (this.furnaceItemStacks[par1] != null)
        {
            ItemStack var2 = this.furnaceItemStacks[par1];
            this.furnaceItemStacks[par1] = null;
            return var2;
        }
        else
        {
            return null;
        }
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setInventorySlotContents(int par1, ItemStack par2ItemStack)
    {
        this.furnaceItemStacks[par1] = par2ItemStack;

        if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit())
        {
            par2ItemStack.stackSize = this.getInventoryStackLimit();
        }
    }

    /**
     * Returns the name of the inventory.
     */
    public String getInvName()
    {
        return "tile.blockStoveIdle.name";
    }

    /**
     * Reads a tile entity from NBT.
     */
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
        NBTTagList var2 = par1NBTTagCompound.getTagList("Items");
        this.furnaceItemStacks = new ItemStack[this.getSizeInventory()];

        for (int var3 = 0; var3 < var2.tagCount(); ++var3)
        {
            NBTTagCompound var4 = (NBTTagCompound)var2.tagAt(var3);
            byte var5 = var4.getByte("Slot");

            if (var5 >= 0 && var5 < this.furnaceItemStacks.length)
            {
                this.furnaceItemStacks[var5] = ItemStack.loadItemStackFromNBT(var4);
            }
        }

        this.furnaceBurnTime1 = par1NBTTagCompound.getShort("BurnTime1");
		this.furnaceBurnTime2 = par1NBTTagCompound.getShort("BurnTime2");
        this.furnaceCookTime1 = par1NBTTagCompound.getShort("CookTime1");
		this.furnaceCookTime2 = par1NBTTagCompound.getShort("CookTime2");
		this.furnaceCookTime3 = par1NBTTagCompound.getShort("CookTime3");
		this.furnaceCookTime4 = par1NBTTagCompound.getShort("CookTime4");
        this.currentItemBurnTime1 = getItemBurnTime(this.furnaceItemStacks[0]);
		this.currentItemBurnTime2 = getItemBurnTime(this.furnaceItemStacks[1]);
    }

    /**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        par1NBTTagCompound.setShort("BurnTime1", (short)this.furnaceBurnTime1);
		par1NBTTagCompound.setShort("BurnTime2", (short)this.furnaceBurnTime2);
        par1NBTTagCompound.setShort("CookTime1", (short)this.furnaceCookTime1);
		par1NBTTagCompound.setShort("CookTime2", (short)this.furnaceCookTime2);
		par1NBTTagCompound.setShort("CookTime3", (short)this.furnaceCookTime3);
		par1NBTTagCompound.setShort("CookTime4", (short)this.furnaceCookTime4);
        NBTTagList var2 = new NBTTagList();

        for (int var3 = 0; var3 < this.furnaceItemStacks.length; ++var3)
        {
            if (this.furnaceItemStacks[var3] != null)
            {
                NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("Slot", (byte)var3);
                this.furnaceItemStacks[var3].writeToNBT(var4);
                var2.appendTag(var4);
            }
        }

        par1NBTTagCompound.setTag("Items", var2);
		super.writeToNBT(par1NBTTagCompound);
    }

    /**
     * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended. *Isn't
     * this more of a set than a get?*
     */
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @SideOnly(Side.CLIENT)

    /**
     * Returns an integer between 0 and the passed value representing how close the current item is to being completely
     * cooked
     */
    public int getCookProgressScaled1(int par1)
    {
        return this.furnaceCookTime1 * par1 / 200;
    }
	
	public int getCookProgressScaled2(int par1)
    {
        return this.furnaceCookTime2 * par1 / 200;
    }
	
	public int getCookProgressScaled3(int par1)
    {
        return this.furnaceCookTime3 * par1 / 200;
    }
	
	public int getCookProgressScaled4(int par1)
    {
        return this.furnaceCookTime4 * par1 / 200;
    }

    @SideOnly(Side.CLIENT)

    /**
     * Returns an integer between 0 and the passed value representing how much burn time is left on the current fuel
     * item, where 0 means that the item is exhausted and the passed value means that the item is fresh
     */
    public int getBurnTimeRemainingScaled1(int par1)
    {
        if (this.currentItemBurnTime1 == 0)
        {
            this.currentItemBurnTime1 = 200;
        }

        return this.furnaceBurnTime1 * par1 / this.currentItemBurnTime1;
    }
	
	public int getBurnTimeRemainingScaled2(int par1)
    {
        if (this.currentItemBurnTime2 == 0)
        {
            this.currentItemBurnTime2 = 200;
        }

        return this.furnaceBurnTime2 * par1 / this.currentItemBurnTime2;
    }

    /**
     * Returns true if the furnace is currently burning
     */
    public boolean isBurning1()
    {
        return this.furnaceBurnTime1 > 0;
    }
	
	public boolean isBurning2()
    {
        return this.furnaceBurnTime1 > 0;
    }

    /**
     * Allows the entity to update its state. Overridden in most subclasses, e.g. the mob spawner uses this to count
     * ticks and creates a new spawn inside its implementation.
     */
    public void updateEntity()
    {
        boolean var1 = this.furnaceBurnTime1 > 0;
        boolean var2 = false;
		boolean var3 = this.furnaceBurnTime2 > 0;
        boolean var4 = false;

        if (this.furnaceBurnTime1 > 0)
        {
            --this.furnaceBurnTime1;
        }
		
		if (this.furnaceBurnTime2 > 0)
        {
            --this.furnaceBurnTime2;
        }

        if (!this.worldObj.isRemote)
        {
            if (this.furnaceBurnTime1 == 0 && (this.canSmelt1() || this.canSmelt2()))
            {
                this.currentItemBurnTime1 = this.furnaceBurnTime1 = getItemBurnTime(this.furnaceItemStacks[0]);

                if (this.furnaceBurnTime1 > 0)
                {
                    var2 = true;

                    if (this.furnaceItemStacks[0] != null)
                    {
                        --this.furnaceItemStacks[0].stackSize;

                        if (this.furnaceItemStacks[0].stackSize == 0)
                        {
                            this.furnaceItemStacks[0] = this.furnaceItemStacks[0].getItem().getContainerItemStack(furnaceItemStacks[0]);
                        }
                    }
                }
            }
			
			if (this.furnaceBurnTime2 == 0 && (this.canSmelt3() || this.canSmelt4()))
            {
                this.currentItemBurnTime2 = this.furnaceBurnTime2 = getItemBurnTime(this.furnaceItemStacks[1]);

                if (this.furnaceBurnTime2 > 0)
                {
                    var4 = true;

                    if (this.furnaceItemStacks[1] != null)
                    {
                        --this.furnaceItemStacks[1].stackSize;

                        if (this.furnaceItemStacks[1].stackSize == 0)
                        {
                            this.furnaceItemStacks[1] = this.furnaceItemStacks[1].getItem().getContainerItemStack(furnaceItemStacks[1]);
                        }
                    }
                }
            }

            if (this.isBurning1())
            {
				if(this.canSmelt1())
				{
					++this.furnaceCookTime1;

					if (this.furnaceCookTime1 == 200)
					{
						this.furnaceCookTime1 = 0;
						this.smeltItem1();
						var2 = true;
					}
				}
				else
				{
					this.furnaceCookTime1 = 0;
				}
				
				if(this.canSmelt2())
				{
					++this.furnaceCookTime2;

					if (this.furnaceCookTime2 == 200)
					{
						this.furnaceCookTime2 = 0;
						this.smeltItem2();
						var2 = true;
					}
				}
				else
				{
					this.furnaceCookTime2 = 0;
				}
				
            }
			
			if (this.isBurning2())
            {
				if(this.canSmelt3())
				{
					++this.furnaceCookTime3;

					if (this.furnaceCookTime3 == 200)
					{
						this.furnaceCookTime3 = 0;
						this.smeltItem3();
						var4 = true;
					}
				}
				else
				{
					this.furnaceCookTime3 = 0;
				}
				
				if(this.canSmelt4())
				{
					++this.furnaceCookTime4;

					if (this.furnaceCookTime4 == 200)
					{
						this.furnaceCookTime4 = 0;
						this.smeltItem4();
						var4 = true;
					}
				}
				else
				{
					this.furnaceCookTime4 = 0;
				}
				
            }

            if (var1 != this.furnaceBurnTime1 > 0)
            {
                var2 = true;
                BlockFurnace.updateFurnaceBlockState(this.furnaceBurnTime1 > 0, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
            }
			else
			{
				if (var3 != this.furnaceBurnTime2 > 0)
				{
					var4 = true;
					BlockFurnace.updateFurnaceBlockState(this.furnaceBurnTime2 > 0, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
				}
			}
        }

        if (var2 || var4)
        {
            this.onInventoryChanged();
        }
    }

    /**
     * Returns true if the furnace can smelt an item, i.e. has a source item, destination stack isn't full, etc.
     */
    private boolean canSmelt1()
    {
        if (this.furnaceItemStacks[2] == null)
        {
            return false;
        }
        else
        {
			ItemStack var1 = null;
			if(Configurator.useFurnaceRecipesInStove == true)
			{
				var1 = FurnaceRecipes.smelting().getSmeltingResult(this.furnaceItemStacks[2]);
			}
            if (var1 == null) return false;
            if (this.furnaceItemStacks[6] == null) return true;
            if (!this.furnaceItemStacks[6].isItemEqual(var1)) return false;
            int result = furnaceItemStacks[6].stackSize + var1.stackSize;
            return (result <= getInventoryStackLimit() && result <= var1.getMaxStackSize());
        }
    }
	
	private boolean canSmelt2()
    {
        if (this.furnaceItemStacks[3] == null)
        {
            return false;
        }
        else
        {
			ItemStack var1 = null;
			if(Configurator.useFurnaceRecipesInStove == true)
			{
				var1 = FurnaceRecipes.smelting().getSmeltingResult(this.furnaceItemStacks[3]);
			}
            if (var1 == null) return false;
            if (this.furnaceItemStacks[7] == null) return true;
            if (!this.furnaceItemStacks[7].isItemEqual(var1)) return false;
            int result = furnaceItemStacks[7].stackSize + var1.stackSize;
            return (result <= getInventoryStackLimit() && result <= var1.getMaxStackSize());
        }
    }
	
	private boolean canSmelt3()
    {
        if (this.furnaceItemStacks[4] == null)
        {
            return false;
        }
        else
        {
			ItemStack var1 = null;
			if(Configurator.useFurnaceRecipesInStove == true)
			{
				var1 = FurnaceRecipes.smelting().getSmeltingResult(this.furnaceItemStacks[4]);
			}
            if (var1 == null) return false;
            if (this.furnaceItemStacks[8] == null) return true;
            if (!this.furnaceItemStacks[8].isItemEqual(var1)) return false;
            int result = furnaceItemStacks[8].stackSize + var1.stackSize;
            return (result <= getInventoryStackLimit() && result <= var1.getMaxStackSize());
        }
    }
	
	private boolean canSmelt4()
    {
        if (this.furnaceItemStacks[5] == null)
        {
            return false;
        }
        else
        {		
			ItemStack var1 = null;
			
			if(Configurator.useFurnaceRecipesInStove == true)
			{
				var1 = FurnaceRecipes.smelting().getSmeltingResult(this.furnaceItemStacks[5]);
			}
            if (var1 == null) return false;
            if (this.furnaceItemStacks[9] == null) return true;
            if (!this.furnaceItemStacks[9].isItemEqual(var1)) return false;
            int result = furnaceItemStacks[9].stackSize + var1.stackSize;
            return (result <= getInventoryStackLimit() && result <= var1.getMaxStackSize());
        }
    }

    /**
     * Turn one item from the furnace source stack into the appropriate smelted item in the furnace result stack
     */
    public void smeltItem1()
    {
        if (this.canSmelt1())
        {
			ItemStack var1 = (ItemStack)null;
			
			if(Configurator.useFurnaceRecipesInStove == true)
			{
				var1 = FurnaceRecipes.smelting().getSmeltingResult(this.furnaceItemStacks[2]);
			}

            if (this.furnaceItemStacks[6] == null)
            {
                this.furnaceItemStacks[6] = var1.copy();
            }
            else if (this.furnaceItemStacks[6].isItemEqual(var1))
            {
                furnaceItemStacks[6].stackSize += var1.stackSize;
            }

            --this.furnaceItemStacks[2].stackSize;

            if (this.furnaceItemStacks[2].stackSize <= 0)
            {
                this.furnaceItemStacks[2] = null;
            }
        }
    }
	
	public void smeltItem2()
    {
        if (this.canSmelt2())
        {
			ItemStack var1 = (ItemStack)null;
			
			if(Configurator.useFurnaceRecipesInStove == true)
			{
				var1 = FurnaceRecipes.smelting().getSmeltingResult(this.furnaceItemStacks[3]);
			}

            if (this.furnaceItemStacks[7] == null)
            {
                this.furnaceItemStacks[7] = var1.copy();
            }
            else if (this.furnaceItemStacks[7].isItemEqual(var1))
            {
                furnaceItemStacks[7].stackSize += var1.stackSize;
            }

            --this.furnaceItemStacks[3].stackSize;

            if (this.furnaceItemStacks[3].stackSize <= 0)
            {
                this.furnaceItemStacks[3] = null;
            }
        }
    }
	
	public void smeltItem3()
    {
        if (this.canSmelt3())
        {
			ItemStack var1 = (ItemStack)null;
			
			if(Configurator.useFurnaceRecipesInStove == true)
			{
				var1 = FurnaceRecipes.smelting().getSmeltingResult(this.furnaceItemStacks[4]);
			}

            if (this.furnaceItemStacks[8] == null)
            {
                this.furnaceItemStacks[8] = var1.copy();
            }
            else if (this.furnaceItemStacks[8].isItemEqual(var1))
            {
                furnaceItemStacks[8].stackSize += var1.stackSize;
            }

            --this.furnaceItemStacks[4].stackSize;

            if (this.furnaceItemStacks[4].stackSize <= 0)
            {
                this.furnaceItemStacks[4] = null;
            }
        }
    }
	
	public void smeltItem4()
    {
        if (this.canSmelt4())
        {
			ItemStack var1 = (ItemStack)null;
			
			if(Configurator.useFurnaceRecipesInStove == true)
			{
				var1 = FurnaceRecipes.smelting().getSmeltingResult(this.furnaceItemStacks[5]);
			}

            if (this.furnaceItemStacks[9] == null)
            {
                this.furnaceItemStacks[9] = var1.copy();
            }
            else if (this.furnaceItemStacks[9].isItemEqual(var1))
            {
                furnaceItemStacks[9].stackSize += var1.stackSize;
            }

            --this.furnaceItemStacks[5].stackSize;

            if (this.furnaceItemStacks[5].stackSize <= 0)
            {
                this.furnaceItemStacks[5] = null;
            }
        }
    }

    /**
     * Returns the number of ticks that the supplied fuel item will keep the furnace burning, or 0 if the item isn't
     * fuel
     */
    public static int getItemBurnTime(ItemStack par0ItemStack)
    {
        if (par0ItemStack == null)
        {
            return 0;
        }
        else
        {
            int var1 = par0ItemStack.getItem().shiftedIndex;
            Item var2 = par0ItemStack.getItem();

            if (par0ItemStack.getItem() instanceof ItemBlock && Block.blocksList[var1] != null)
            {
                Block var3 = Block.blocksList[var1];

                if (var3 == Block.woodSingleSlab)
                {
                    return 150;
                }

                if (var3.blockMaterial == Material.wood)
                {
                    return 300;
                }
            }
            if (var2 instanceof ItemTool && ((ItemTool) var2).func_77861_e().equals("WOOD")) return 200;
            if (var2 instanceof ItemSword && ((ItemSword) var2).func_77825_f().equals("WOOD")) return 200;
            if (var2 instanceof ItemHoe && ((ItemHoe) var2).func_77842_f().equals("WOOD")) return 200;
            if (var1 == Item.stick.shiftedIndex) return 100;
            if (var1 == Item.coal.shiftedIndex) return 1600;
            if (var1 == Item.bucketLava.shiftedIndex) return 20000;
            if (var1 == Block.sapling.blockID) return 100;
            if (var1 == Item.blazeRod.shiftedIndex) return 2400;
            return GameRegistry.getFuelValue(par0ItemStack);
        }
    }

    /**
     * Return true if item is a fuel source (getItemBurnTime() > 0).
     */
    public static boolean isItemFuel(ItemStack par0ItemStack)
    {
        return getItemBurnTime(par0ItemStack) > 0;
    }

    /**
     * Do not make give this method the name canInteractWith because it clashes with Container
     */
    public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
    {
        return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : par1EntityPlayer.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
    }

    public void openChest() {}

    public void closeChest() {}

    @Override
    public int getStartInventorySide(ForgeDirection side)
    {
        if (side == ForgeDirection.DOWN) return 0;
        if (side == ForgeDirection.UP) return 2; 
        return 6;
    }

    @Override
    public int getSizeInventorySide(ForgeDirection side)
    {
		if (side == ForgeDirection.DOWN) return 2;
        return 4;
    }
}