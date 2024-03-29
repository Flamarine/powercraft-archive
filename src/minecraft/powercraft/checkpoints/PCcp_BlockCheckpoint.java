package powercraft.checkpoints;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import powercraft.api.PC_Utils;
import powercraft.api.PC_Utils.Communication;
import powercraft.api.PC_Utils.GameInfo;
import powercraft.api.PC_Utils.Inventory;
import powercraft.api.PC_VecI;
import powercraft.api.annotation.PC_BlockInfo;
import powercraft.api.block.PC_Block;
import powercraft.api.item.PC_IItemInfo;
import powercraft.api.registry.PC_GresRegistry;
import powercraft.api.registry.PC_LangRegistry;
import powercraft.api.registry.PC_MSGRegistry;
import powercraft.api.tileentity.PC_TileEntity;

@PC_BlockInfo(tileEntity=PCcp_TileEntityCheckpoint.class)
public class PCcp_BlockCheckpoint extends PC_Block implements PC_IItemInfo {
	
	public PCcp_BlockCheckpoint(int id) {
		super(id, 1, Material.air);
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F/16.0F, 1.0F);
        setHardness(1.0F);
        setResistance(8.0F);
        setStepSound(Block.soundMetalFootstep);
        setCreativeTab(CreativeTabs.tabTools);
	}
	
	@Override
	public TileEntity newTileEntity(World world, int metadata) {
		return new PCcp_TileEntityCheckpoint();
	}

	@Override
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
		if(entity instanceof EntityPlayer){
			PCcp_TileEntityCheckpoint te = GameInfo.getTE(world, x, y, z);
			EntityPlayer player = (EntityPlayer) entity;
			if(!world.isRemote){
				if(player.ticksExisted<=2 && Inventory.isInventoryEmpty(player.inventory)){
					for(int i=0; i<te.getSizeInventory(); i++){
						ItemStack is = te.getStackInSlot(i);
						if(is!=null){
							player.inventory.setInventorySlotContents(i, is.copy());
						}
					}
				}
			}
			if(te.isCollideTriggerd()){
				ChunkCoordinates cc = new ChunkCoordinates(x, y, z);
				if(!player.getBedLocation().equals(cc)){
					player.setSpawnChunk(cc, true);
					if(world.isRemote){
			        	Communication.chatMsg(PC_LangRegistry.tr("pc.checkpoint.setSpawn", new PC_VecI(x, y, z).toString()), true);
			        }
				}
			}
		}
	}

	@Override
    public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int par6, float par7, float par8, float par9){
		if(entityplayer.isSneaking()){
			if(!world.isRemote && GameInfo.isPlayerOPOrOwner(entityplayer)){
				PC_GresRegistry.openGres("Checkpoint", entityplayer, GameInfo.<PC_TileEntity>getTE(world, i, j, k));
			}
			return true;
		}
		if(world.isRemote){
        	Communication.chatMsg(PC_LangRegistry.tr("pc.checkpoint.setSpawn", new PC_VecI(i, j, k).toString()), true);
        }
		entityplayer.setSpawnChunk(new ChunkCoordinates(i, j, k), true);
        return true;
    }
	
	@Override
	public int getBlockTextureFromSide(int par1) {
		if(par1==0)
			return 0;
		if(par1==1)
			return 1;
		return 2;
	}

	@Override
    public boolean renderAsNormalBlock(){
        return false;
    }

    @Override
    public boolean isOpaqueCube(){
        return false;
    }
    
    @Override
	public List<ItemStack> getItemStacks(List<ItemStack> arrayList) {
		arrayList.add(new ItemStack(this));
		return arrayList;
	}
    
	@Override
	public Object msg(IBlockAccess world, PC_VecI pos, int msg, Object... obj) {
		switch(msg){
		case PC_MSGRegistry.MSG_DEFAULT_NAME:
			return "Checkpoint";
		case PC_MSGRegistry.MSG_BLOCK_FLAGS:{
			List<String> list = (List<String>)obj[0];
			list.add(PC_Utils.NO_HARVEST);
			list.add(PC_Utils.NO_PICKUP);
	   		return list;
		}case PC_MSGRegistry.MSG_ITEM_FLAGS:{
			List<String> list = (List<String>)obj[1];
			list.add(PC_Utils.NO_BUILD);
			return list;
		}default:
			return null;
		}
	}

}
