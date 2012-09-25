package KBI.agricrafture.core;

import KBI.agricrafture.api.*;
import net.minecraft.src.*;
import java.util.Random;

public class BlockCulinary extends BlockContainer {

        protected BlockCulinary (int id) 
		{
                super(id, Material.wood);
                setHardness(2.0F);
                setResistance(5.0F);
                setBlockName("blockCulinary");
				setCreativeTab(CreativeTabs.tabFood);
				float var5 = 0.0625F;
				this.setBlockBounds(var5, 0.0F, var5, 1.0F - var5, 0.0625F, 1.0F - var5);
        }
		
		@Override
		public int getBlockTextureFromSideAndMetadata(int side, int md)
		{
			if(side==0 || side==1)
			{
				return 0;
			}
			else
			{
				return 1;
			}
		}
		
		@Override
		public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
		{
			return null;
		}
		
		@Override
		public boolean isOpaqueCube() 
		{
			return false;
		}

		@Override
		public boolean renderAsNormalBlock() 
		{
			
			return false;
		}
		
		@Override
		public void setBlockBoundsForItemRender()
		{
			float var1 = 0.5F;
			float var2 = 0.125F;
			float var3 = 0.5F;
			this.setBlockBounds(0.5F - var1, 0.5F - var2, 0.5F - var3, 0.5F + var1, 0.5F + var2, 0.5F + var3);
		}
		
		@Override
		public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4)
		{
			return par1World.doesBlockHaveSolidTopSurface(par2, par3 - 1, par4) || BlockFence.isIdAFence(par1World.getBlockId(par2, par3 - 1, par4));
		}
		
		@Override
		public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5)
		{
			boolean var6 = false;

			if (!par1World.doesBlockHaveSolidTopSurface(par2, par3 - 1, par4) && !BlockFence.isIdAFence(par1World.getBlockId(par2, par3 - 1, par4)))
			{
				var6 = true;
			}

			if (var6)
			{
				this.dropBlockAsItem(par1World, par2, par3, par4, par1World.getBlockMetadata(par2, par3, par4), 0);
				par1World.setBlockWithNotify(par2, par3, par4, 0);
			}
		}

        @Override
        public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int idk, float what, float these, float are) 
		{
			if (world.isRemote)
			{
				return true;
			}
			else
			{
				TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
				if (tileEntity == null || player.isSneaking()) 
				{
					return false;
				}
				player.openGui(AgricraftureMain.instance, 0, world, x, y, z);
				return true;
			}
        }

        @Override
        public void breakBlock(World world, int x, int y, int z, int par5, int par6) 
		{
                dropItems(world, x, y, z);
                super.breakBlock(world, x, y, z, par5, par6);
        }
       
        private void dropItems(World world, int x, int y, int z)
		{
                Random rand = new Random();

                TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
                if (!(tileEntity instanceof IInventory)) {
                        return;
                }
                IInventory inventory = (IInventory) tileEntity;

                for (int i = 0; i < inventory.getSizeInventory(); i++) {
                        ItemStack item = inventory.getStackInSlot(i);

                        if (item != null && item.stackSize > 0) {
                                float rx = rand.nextFloat() * 0.8F + 0.1F;
                                float ry = rand.nextFloat() * 0.8F + 0.1F;
                                float rz = rand.nextFloat() * 0.8F + 0.1F;

                                EntityItem entityItem = new EntityItem(world,
                                                x + rx, y + ry, z + rz,
                                                new ItemStack(item.itemID, item.stackSize, item.getItemDamage()));

                                if (item.hasTagCompound()) {
                                        entityItem.item.setTagCompound((NBTTagCompound) item.getTagCompound().copy());
                                }

                                float factor = 0.05F;
                                entityItem.motionX = rand.nextGaussian() * factor;
                                entityItem.motionY = rand.nextGaussian() * factor + 0.2F;
                                entityItem.motionZ = rand.nextGaussian() * factor;
                                world.spawnEntityInWorld(entityItem);
                                item.stackSize = 0;
                        }
                }
        }

        @Override
        public TileEntity createNewTileEntity(World world) {
                return new TileEntityCulinary();
        }
		
		public String getTextureFile()
		{
            return "/KBI/agricrafture/resources/1.png";
		}


}