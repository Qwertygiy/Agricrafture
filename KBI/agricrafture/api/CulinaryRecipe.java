package KBI.agricrafture.api;

import net.minecraft.src.*;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class CulinaryRecipe {

	public static LinkedList<CulinaryRecipe> recipes = new LinkedList<CulinaryRecipe>();
	
	public final List ingredients;
	public final ItemStack tool;
	public final ItemStack dish;
	public final ItemStack result;
	public final ItemStack output1;
	public final ItemStack output2;

	public final int clicks;
	public final int output1percent;
	public final int output2percent;
	
	public static void registerCulinaryRecipe(CulinaryRecipe recipe) 
	{
		if (!recipes.contains(recipe)) 
		{
			recipes.add(recipe);
		}
	}
	
	/** the "true" recipe definer
	 *
	 */
	public CulinaryRecipe(List par1, ItemStack par2, ItemStack par3, ItemStack par4, ItemStack par5, ItemStack par6, int par7, int par8, int par9)
	{
		this.ingredients = par1;
		this.tool = par2;
		this.dish = par3;
		this.result = par4;
		this.output1 = par5;
		this.output2 = par6;
		this.clicks = par7;
		this.output1percent = par8;
		this.output2percent = par9;
	}
	
	/**ingredients, tool, result*/
	public CulinaryRecipe(List par1, ItemStack par2, ItemStack par3)
	{
		this(par1, par2, null, par3, null, null, 8, 0, 0);
	}
	
	/**ingredients, tool, result, clicks*/
	public CulinaryRecipe(List par1, ItemStack par2, ItemStack par3, int par4)
	{
		this(par1, par2, null, par3, null, null, par4, 0, 0);
	}
	
	/**ingredients, tool, dish, result*/
	public CulinaryRecipe(List par1, ItemStack par2, ItemStack par3, ItemStack par4)
	{
		this(par1, par2, par3, par4, null, null, 8, 0, 0);
	}
	
	/**ingredients, tool, dish, result, clicks*/
	public CulinaryRecipe(List par1, ItemStack par2, ItemStack par3, ItemStack par4, int par5)
	{
		this(par1, par2, par3, par4, null, null, par5, 0, 0);
	}
	
	/**ingredients, tool, dish, result, output1, clicks*/
	public CulinaryRecipe(List par1, ItemStack par2, ItemStack par3, ItemStack par4, ItemStack par5, int par6)
	{
		this(par1, par2, par3, par4, par5, null, par6, 100, 0);
	}
	
	/**ingredients, tool, dish, result, output1, clicks, output1percent*/
	public CulinaryRecipe(List par1, ItemStack par2, ItemStack par3, ItemStack par4, ItemStack par5, int par6, int par7)
	{
		this(par1, par2, par3, par4, par5, null, par6, par7, 0);
	}
	
	/**ingredients, tool, dish, result, output1, output2, clicks, output1percent*/
	public CulinaryRecipe(List par1, ItemStack par2, ItemStack par3, ItemStack par4, ItemStack par5, ItemStack par6, int par7, int par8)
	{
		this(par1, par2, par3, par4, par5, par6, par7, par8, 100);
	}
	
	/**ingredients, tool, dish, result, output1, output2, clicks*/
	public CulinaryRecipe(List par1, ItemStack par2, ItemStack par3, ItemStack par4, ItemStack par5, ItemStack par6, int par7)
	{
		this(par1, par2, par3, par4, par5, par6, par7, 100, 100);
	}
	
	
	/** check to see if the inventory matches this recipe
	 *
	 */
	public boolean matches(TileEntityCulinary par1)
    {
        ArrayList var2 = new ArrayList(this.ingredients);

        for (int var4 = 0; var4 < 5; ++var4)
        {
            ItemStack var5 = par1.getStackInSlot(var4);

            if (var5 != null)
            {
                boolean var6 = false;
                Iterator var7 = var2.iterator();

                while (var7.hasNext())
                {
                    ItemStack var8 = (ItemStack)var7.next();

                    if (var5.itemID == var8.itemID && (var8.getItemDamage() == -1 || var5.getItemDamage() == var8.getItemDamage()))
                    {
                        var6 = true;
                        var2.remove(var8);
                        break;
                    }
                }

                if (!var6)
                {
                    return false;
                }
            }
        }
        
		//main inventory has been checked, now checking tool and dish
		
		if (var2.isEmpty())
		{
			return (par1.getStackInSlot(6 - 2) == this.tool && par1.getStackInSlot(7 - 2) == this.dish);
		}
		else
		{
			return false;
		}
    }
	
	public ItemStack getMainResult(TileEntityCulinary par1)
    {
        return this.result.copy();
    }
	
	public ItemStack getSecondaryOutput(TileEntityCulinary par1)
	{
		if(this.output1 != null)
		{
			//if percentage is 0, 100, or doesn't exist, always output the item
			if(/*this.output1percent == null ||*/ this.output1percent == 100)
			{
				return this.output1.copy();
			}
			else
			{
				Random random = new Random();
				int chance = random.nextInt(100);
				if(chance < output1percent)
				{
					return this.output1.copy();
				}
				else return null;
			}
		}
		else return null;		
	}
	
	public ItemStack getSecondaryOutput2(TileEntityCulinary par1)
	{
		if(this.output2 != null)
		{
			//if percentage is 100, or doesn't exist, always output the item
			if(/*this.output2percent == null ||*/ this.output2percent == 100)
			{
				return this.output2.copy();
			}
			else
			{
				Random random = new Random();
				int chance = random.nextInt(100);
				if(chance < output2percent)
				{
					return this.output2.copy();
				}
				else return null;
			}
		}
		else return null;		
	}
	
	public int getRecipeSize()
    {
        return this.ingredients.size();
    }

}