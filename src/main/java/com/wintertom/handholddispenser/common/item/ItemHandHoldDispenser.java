package com.wintertom.handholddispenser.common.item;

import com.wintertom.handholddispenser.common.entity.projectile.EntityDispenserFireworkRocket;
import com.wintertom.handholddispenser.common.entity.projectile.EntityDispenserFireball;
import com.wintertom.handholddispenser.common.util.ItemMathHelper;
import com.wintertom.handholddispenser.init.EnchantmentRegister;
import com.wintertom.handholddispenser.init.HandHoldDispenser;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ItemHandHoldDispenser extends Item
{
    public static final String NAME = "hand_hold_dispenser";
    public ItemHandHoldDispenser()
    {
        this.setCreativeTab(HandHoldDispenser.HAND_HOLD_DISPENSER_MOD_TAB);
        this.setMaxStackSize(1);
        this.setMaxDamage(256);
    }

    //Called when the equipped item is right clicked.
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack dispenserItemStack = playerIn.getHeldItem(handIn);
        ItemStack currentItemStack = findCurrentItem(playerIn);
        boolean hasCurrentItemFlag = !currentItemStack.isEmpty();
        if(playerIn.capabilities.isCreativeMode)
        {
            launch(worldIn,playerIn,currentItemStack,dispenserItemStack);
            playerIn.setActiveHand(handIn);
            return new ActionResult<>(EnumActionResult.SUCCESS,dispenserItemStack);
        }
        else
        {
            if(!hasCurrentItemFlag)
            {
                playerIn.setActiveHand(handIn);
                return new ActionResult<>(EnumActionResult.FAIL,dispenserItemStack);
            }
            else
            {
                launch(worldIn,playerIn,currentItemStack,dispenserItemStack);
                int j = EnchantmentHelper.getEnchantmentLevel(EnchantmentRegister.enchantmentReduceFreeze,dispenserItemStack);
                playerIn.getCooldownTracker().setCooldown(this, (int) (30*(1-0.2*j)));
                dispenserItemStack.damageItem(1,playerIn);
                currentItemStack.shrink(1);
                playerIn.setActiveHand(handIn);
                return new ActionResult<>(EnumActionResult.SUCCESS,dispenserItemStack);
            }
        }
    }

    @Override
    public int getItemEnchantability() {
        return 1;
    }

    private ItemStack findCurrentItem(EntityPlayer entityPlayer)
    {
        if(this.isCurrentItem(entityPlayer.getHeldItem(EnumHand.OFF_HAND)))
        {
            return entityPlayer.getHeldItemOffhand();
        }
        else if(this.isCurrentItem(entityPlayer.getHeldItem(EnumHand.MAIN_HAND)))
        {
            return entityPlayer.getHeldItemMainhand();
        }
        else
        {
            for (int i=0;i < entityPlayer.inventory.getSizeInventory();++i)
            {
                ItemStack itemStack = entityPlayer.inventory.getStackInSlot(i);
                if(isCurrentItem(itemStack))
                {
                    return itemStack;
                }
            }
        }
        return ItemStack.EMPTY;
    }
    private boolean isCurrentItem(ItemStack itemStack)
    {
        Item item = itemStack.getItem();
        if (item == Items.FIRE_CHARGE||item == Items.FIREWORKS)
        {
            //If the item is fire charge or fireworks(rocket)
            return true;
        }
        //If the item is splash_potion or lingering_potion
        else return item == Items.SPLASH_POTION || item == Items.LINGERING_POTION;
    }
    //TODO:add item judgement
    private void launch(World worldIn,EntityPlayer entityPlayer,ItemStack currentItemStack,ItemStack dispenserItemStack)
    {
        if(!worldIn.isRemote)
        {
//            int scatteringLevel = EnchantmentHelper.getEnchantmentLevel(EnchantmentRegister.enchantmentScattering,dispenserItemStack);
            if (currentItemStack.isEmpty())
            {
                launchFire(worldIn,entityPlayer,dispenserItemStack);
            }
            else
            {
                Item item = currentItemStack.getItem();
                if(item == Items.FIRE_CHARGE)
                {
                    launchFire(worldIn,entityPlayer,dispenserItemStack);
                }
                else if(item == Items.FIREWORKS)
                {
                    launchFirework(worldIn,currentItemStack,entityPlayer,dispenserItemStack);
                }
                else if(item == Items.SPLASH_POTION)
                {
                    launchPotion(worldIn, entityPlayer, currentItemStack,dispenserItemStack);
                }
                else if(item == Items.LINGERING_POTION)
                {
                    launchPotion(worldIn, entityPlayer, currentItemStack,dispenserItemStack);
                }
            }
        }
    }
    private void launchFire(World worldIn,EntityPlayer entityPlayer,ItemStack itemStack)
    {
        int unBurnLevel = EnchantmentHelper.getEnchantmentLevel(EnchantmentRegister.enchantmentUnBurn,itemStack);
        worldIn.playSound((EntityPlayer)null, entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ, SoundEvents.ENTITY_GHAST_SHOOT, SoundCategory.PLAYERS, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
        for (Vec3d vec3d:getShootVec3ds(entityPlayer,itemStack,7.5F))
        {
            EntityDispenserFireball entityDispenserFireball = new EntityDispenserFireball(worldIn,entityPlayer,
                    entityPlayer.posX + vec3d.x*1.0D,
                    entityPlayer.posY + (double)(entityPlayer.height / 2.0F) + 0.5D+vec3d.y,
                    entityPlayer.posZ + vec3d.z*1.0D,
                    vec3d.x,
                    vec3d.y,
                    vec3d.z,
                    5+ EnchantmentHelper.getEnchantmentLevel(EnchantmentRegister.enchantmentFierceFire,itemStack));
            if (unBurnLevel>0)
            {
                entityDispenserFireball.setBurn(false);
            }
            worldIn.spawnEntity(entityDispenserFireball);
        }
    }

    private void launchFirework(World worldIn, ItemStack ammoItemStack, EntityPlayer entityPlayer,ItemStack handHoldDispenserItemStack)
    {
        for (Vec3d vec3d:getShootVec3ds(entityPlayer,handHoldDispenserItemStack,10F))
        {
            EntityDispenserFireworkRocket entityDispenserFireworkRocket = new EntityDispenserFireworkRocket(worldIn,ammoItemStack,entityPlayer);
            entityDispenserFireworkRocket.shoot(vec3d.x,vec3d.y,vec3d.z,0,0);
            worldIn.spawnEntity(entityDispenserFireworkRocket);
        }
    }
    private void launchPotion(World worldIn,EntityPlayer entityPlayer,ItemStack itemStack,ItemStack handHoldDispenserItemStack)
    {
        ItemStack itemStack1 = entityPlayer.capabilities.isCreativeMode ? itemStack.copy() : itemStack.splitStack(1);
        worldIn.playSound((EntityPlayer)null, entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ, SoundEvents.ENTITY_SPLASH_POTION_THROW, SoundCategory.PLAYERS, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
        for (Vec3d vec3d:getShootVec3ds(entityPlayer,handHoldDispenserItemStack,7.5F))
        {
            EntityPotion entitypotion = new EntityPotion(worldIn, entityPlayer, itemStack1);
            entitypotion.shoot(vec3d.x,vec3d.y,vec3d.z,1.0F,1.0F);
            worldIn.spawnEntity(entitypotion);
        }
    }

    private Vec3d[] getShootVec3ds(EntityPlayer entityPlayer,ItemStack handHoldDispenserItemStack,float angle)
    {
        int scatterLevel = EnchantmentHelper.getEnchantmentLevel(EnchantmentRegister.enchantmentScatter,handHoldDispenserItemStack)+1;
        if(scatterLevel<=1)
        {
            return new Vec3d[]{
                    entityPlayer.getLook(1.0F)
            };
        }
        Vec3d[] vec3ds = new Vec3d[scatterLevel];
        Vec3d playerLook = entityPlayer.getLook(1.0F);
        int index = 0;
        if(scatterLevel%2!=0)
        {
            vec3ds[0] = playerLook;
            index = 1;
        }
        for (int i = 0; i < scatterLevel / 2; i++,index++) {
            vec3ds[index] = ItemMathHelper.calculateWithYawAndPitch(angle*(i+1),entityPlayer.rotationYaw,entityPlayer.rotationPitch, (float) playerLook.y);
        }
        for (int i = 0; i < scatterLevel / 2; i++,index++) {
            vec3ds[index] = ItemMathHelper.calculateWithYawAndPitch(-angle*(i+1),entityPlayer.rotationYaw,entityPlayer.rotationPitch, (float) playerLook.y);
        }
        return vec3ds;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
        if(state.getBlockHardness(worldIn,pos) != 0.0D)
        {
            stack.damageItem(2,entityLiving);
        }
        return true;
    }

    @Override
    public boolean isFull3D() {
        return true;
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        Item item = repair.getItem();
        return item == Items.DIAMOND || item == Items.GOLD_INGOT || item == Items.IRON_INGOT || item == ItemBlock.getItemFromBlock(Blocks.DISPENSER);
    }
}
