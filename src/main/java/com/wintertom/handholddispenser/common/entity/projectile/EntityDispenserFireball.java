package com.wintertom.handholddispenser.common.entity.projectile;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityDispenserFireball extends EntityFireball {

    private float fireDamage = 0.0f;
    private boolean isBurn = true;
    public EntityDispenserFireball(World worldIn) {
        super(worldIn);
    }

    public EntityDispenserFireball(World worldIn, EntityLivingBase entityLivingBase, double x, double y, double z, double accelX, double accelY, double accelZ, float fireDamage) {
        super(worldIn, x, y, z, accelX, accelY, accelZ);
        this.shootingEntity = entityLivingBase;
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;
        this.fireDamage = fireDamage;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if(this.ticksExisted >= 200)
        {
            this.setDead();
        }
    }
    @Override
    protected void onImpact(RayTraceResult result) {
        if(!this.world.isRemote)
        {
            if(result.entityHit != null)
            {
                boolean flag = result.entityHit.attackEntityFrom(DamageSource.causeFireballDamage(this,this.shootingEntity),fireDamage);
                this.applyEnchantments(this.shootingEntity,result.entityHit);
                if(flag)
                {
                    if(!result.entityHit.isImmuneToFire()) result.entityHit.setFire(5);
                }
            }
            else
            {
                boolean flag1 = true;

                if (this.shootingEntity != null && this.shootingEntity instanceof EntityLiving)
                {
                    flag1 = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.world, this.shootingEntity);
                }

                if (flag1&&isBurn)
                {
                    BlockPos blockpos = result.getBlockPos().offset(result.sideHit);

                    if (this.world.isAirBlock(blockpos))
                    {
                        this.world.setBlockState(blockpos, Blocks.FIRE.getDefaultState());
                    }
                }
            }
            this.setDead();
        }
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    public void setBurn(boolean burn) {
        isBurn = burn;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        return false;
    }
}
