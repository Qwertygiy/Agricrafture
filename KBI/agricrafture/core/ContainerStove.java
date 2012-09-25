package KBI.agricrafture.core;

import net.minecraft.src.*;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.util.Iterator;

public class ContainerStove extends Container
{
    private TileEntityStove furnace;
    private int lastCookTime1 = 0;
	private int lastCookTime2 = 0;
	private int lastCookTime3 = 0;
	private int lastCookTime4 = 0;
    private int lastBurnTime1 = 0;
	private int lastBurnTime2 = 0;
    private int lastItemBurnTime1 = 0;
	private int lastItemBurnTime2 = 0;

    public ContainerStove(InventoryPlayer par1InventoryPlayer, TileEntityStove par2TileEntityFurnace)
    {
        this.furnace = par2TileEntityFurnace;
		//fuel
        this.addSlotToContainer(new Slot(par2TileEntityFurnace, 0, 8, 46));
        this.addSlotToContainer(new Slot(par2TileEntityFurnace, 1, 98, 46));
		//input
		this.addSlotToContainer(new Slot(par2TileEntityFurnace, 2, 8, 11));
		this.addSlotToContainer(new Slot(par2TileEntityFurnace, 3, 8, 81));
		this.addSlotToContainer(new Slot(par2TileEntityFurnace, 4, 98, 11));
		this.addSlotToContainer(new Slot(par2TileEntityFurnace, 5, 98, 81));
		//output
        this.addSlotToContainer(new SlotFurnace(par1InventoryPlayer.player, par2TileEntityFurnace, 6, 58, 11));
		this.addSlotToContainer(new SlotFurnace(par1InventoryPlayer.player, par2TileEntityFurnace, 7, 58, 81));
		this.addSlotToContainer(new SlotFurnace(par1InventoryPlayer.player, par2TileEntityFurnace, 8, 148, 11));
		this.addSlotToContainer(new SlotFurnace(par1InventoryPlayer.player, par2TileEntityFurnace, 9, 148, 81));
        int var3;

        for (var3 = 0; var3 < 3; ++var3)
        {
            for (int var4 = 0; var4 < 9; ++var4)
            {
                this.addSlotToContainer(new Slot(par1InventoryPlayer, var4 + var3 * 9 + 9, 8 + var4 * 18, 107 + var3 * 18));
            }
        }

        for (var3 = 0; var3 < 9; ++var3)
        {
            this.addSlotToContainer(new Slot(par1InventoryPlayer, var3, 8 + var3 * 18, 165));
        }
    }

    public void addCraftingToCrafters(ICrafting par1ICrafting)
    {
        super.addCraftingToCrafters(par1ICrafting);
		par1ICrafting.updateCraftingInventoryInfo(this, 0, this.furnace.furnaceBurnTime1);
		par1ICrafting.updateCraftingInventoryInfo(this, 1, this.furnace.furnaceBurnTime2);
        par1ICrafting.updateCraftingInventoryInfo(this, 2, this.furnace.furnaceCookTime1);
		par1ICrafting.updateCraftingInventoryInfo(this, 3, this.furnace.furnaceCookTime2);
		par1ICrafting.updateCraftingInventoryInfo(this, 4, this.furnace.furnaceCookTime3);
		par1ICrafting.updateCraftingInventoryInfo(this, 5, this.furnace.furnaceCookTime4);
        par1ICrafting.updateCraftingInventoryInfo(this, 6, this.furnace.currentItemBurnTime1);
		par1ICrafting.updateCraftingInventoryInfo(this, 7, this.furnace.currentItemBurnTime1);
		par1ICrafting.updateCraftingInventoryInfo(this, 8, this.furnace.currentItemBurnTime2);
		par1ICrafting.updateCraftingInventoryInfo(this, 9, this.furnace.currentItemBurnTime2);
    }

    /**
     * Updates crafting matrix; called from onCraftMatrixChanged. Args: none
     */
    public void updateCraftingResults()
    {
        super.updateCraftingResults();
        Iterator var1 = this.crafters.iterator();

        while (var1.hasNext())
        {
            ICrafting var2 = (ICrafting)var1.next();

            if (this.lastCookTime1 != this.furnace.furnaceCookTime1)
            {
                var2.updateCraftingInventoryInfo(this, 2, this.furnace.furnaceCookTime1);
            }
			
			if (this.lastCookTime2 != this.furnace.furnaceCookTime2)
            {
                var2.updateCraftingInventoryInfo(this, 3, this.furnace.furnaceCookTime2);
            }
			
			if (this.lastCookTime3 != this.furnace.furnaceCookTime3)
            {
                var2.updateCraftingInventoryInfo(this, 4, this.furnace.furnaceCookTime3);
            }
			
			if (this.lastCookTime4 != this.furnace.furnaceCookTime4)
            {
                var2.updateCraftingInventoryInfo(this, 5, this.furnace.furnaceCookTime4);
            }

            if (this.lastBurnTime1 != this.furnace.furnaceBurnTime1)
            {
                var2.updateCraftingInventoryInfo(this, 0, this.furnace.furnaceBurnTime1);
            }
			
			if (this.lastBurnTime2 != this.furnace.furnaceBurnTime2)
            {
                var2.updateCraftingInventoryInfo(this, 1, this.furnace.furnaceBurnTime2);
            }

            if (this.lastItemBurnTime1 != this.furnace.currentItemBurnTime1)
            {
                var2.updateCraftingInventoryInfo(this, 6, this.furnace.currentItemBurnTime1);
				var2.updateCraftingInventoryInfo(this, 7, this.furnace.currentItemBurnTime1);
            }
			
			if (this.lastItemBurnTime2 != this.furnace.currentItemBurnTime2)
            {
                var2.updateCraftingInventoryInfo(this, 8, this.furnace.currentItemBurnTime2);
				var2.updateCraftingInventoryInfo(this, 9, this.furnace.currentItemBurnTime2);
            }
        }

        this.lastCookTime1 = this.furnace.furnaceCookTime1;
		this.lastCookTime2 = this.furnace.furnaceCookTime2;
		this.lastCookTime3 = this.furnace.furnaceCookTime3;
		this.lastCookTime4 = this.furnace.furnaceCookTime4;
        this.lastBurnTime1 = this.furnace.furnaceBurnTime1;
		this.lastBurnTime2 = this.furnace.furnaceBurnTime2;
        this.lastItemBurnTime1 = this.furnace.currentItemBurnTime1;
		this.lastItemBurnTime2 = this.furnace.currentItemBurnTime2;
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int par1, int par2)
    {
		if (par1 == 0)
        {
            this.furnace.furnaceBurnTime1 = par2;
        }
		
		if (par1 == 1)
        {
            this.furnace.furnaceBurnTime2 = par2;
        }
	
	
        if (par1 == 2)
        {
            this.furnace.furnaceCookTime1 = par2;
        }
		
		if (par1 == 3)
        {
            this.furnace.furnaceCookTime2 = par2;
        }
		
		if (par1 == 4)
        {
            this.furnace.furnaceCookTime3 = par2;
        }
		
		if (par1 == 5)
        {
            this.furnace.furnaceCookTime4 = par2;
        }

        if (par1 == 6 || par1 == 7)
        {
            this.furnace.currentItemBurnTime1 = par2;
        }
		
		if (par1 == 8 || par1 == 9)
        {
            this.furnace.currentItemBurnTime2 = par2;
        }
    }

    public boolean canInteractWith(EntityPlayer par1EntityPlayer)
    {
        return this.furnace.isUseableByPlayer(par1EntityPlayer);
    }

    /**
     * Called to transfer a stack from one inventory to the other eg. when shift clicking.
     */
    public ItemStack transferStackInSlot(int par1)
    {
        ItemStack var2 = null;
        Slot var3 = (Slot)this.inventorySlots.get(par1);

        if (var3 != null && var3.getHasStack())
        {
            ItemStack var4 = var3.getStack();
            var2 = var4.copy();

            if (par1 == 6 || par1 == 7 || par1 == 8 || par1 == 9)
            {
                if (!this.mergeItemStack(var4, 10, 46, true))
                {
                    return null;
                }

                var3.onSlotChange(var4, var2);
            }
            else if (par1 != 1 && par1 != 0)
            {
                if (Configurator.useFurnaceRecipesInStove == true && FurnaceRecipes.smelting().getSmeltingResult(var4) != null)
                {
                    if (!this.mergeItemStack(var4, 2, 6, false))
                    {
                        return null;
                    }
                }
                else if (TileEntityStove.isItemFuel(var4))
                {
                    if (!this.mergeItemStack(var4, 0, 2, false))
                    {
                        return null;
                    }
                }
                else if (par1 >= 10 && par1 < 37)
                {
                    if (!this.mergeItemStack(var4, 37, 46, false))
                    {
                        return null;
                    }
                }
                else if (par1 >= 37 && par1 < 46 && !this.mergeItemStack(var4, 10, 37, false))
                {
                    return null;
                }
            }
            else if (!this.mergeItemStack(var4, 10, 46, false))
            {
                return null;
            }

            if (var4.stackSize == 0)
            {
                var3.putStack((ItemStack)null);
            }
            else
            {
                var3.onSlotChanged();
            }

            if (var4.stackSize == var2.stackSize)
            {
                return null;
            }

            var3.onPickupFromSlot(var4);
        }

        return var2;
    }
}
