package com.wintertom.handholddispenser.common.entity.projectile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class EntityDispenserFireworkRocket extends Entity implements IProjectile {
    private static final DataParameter<ItemStack> FIREWORK_ITEM = EntityDataManager.createKey(EntityFireworkRocket.class, DataSerializers.ITEM_STACK);
    private int fireworkAge;
    /** The lifetime of the firework in ticks. When the age reaches the lifetime the firework explodes. */
    private int lifetime;
    private EntityLivingBase entityLivingBase;

    public EntityDispenserFireworkRocket(World worldIn) {
        super(worldIn);
        this.setSize(0.25F, 0.25F);
    }
    public EntityDispenserFireworkRocket(World worldIn,ItemStack itemStack,EntityLivingBase entityLivingBase)
    {
        this(worldIn);
        this.fireworkAge = 0;
        this.entityLivingBase = entityLivingBase;
        this.setPosition(this.entityLivingBase.posX,
                this.entityLivingBase.posY + (double)this.entityLivingBase.getEyeHeight() - 0.10000000149011612D,
                this.entityLivingBase.posZ);
        int i = 1;
        if (!itemStack.isEmpty() && itemStack.hasTagCompound())
        {
            this.dataManager.set(FIREWORK_ITEM, itemStack.copy());
            NBTTagCompound nbttagcompound = itemStack.getTagCompound();
            NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("Fireworks");
            i += nbttagcompound1.getByte("Flight");
        }
        this.lifetime = 10*i;
    }
    public EntityDispenserFireworkRocket(World worldIn,ItemStack itemStack,EntityLivingBase entityLivingBase,double posX,double posY,double posZ,double motionX,double motionY,double motionZ)
    {
        this(worldIn);
        this.fireworkAge = 0;
        this.entityLivingBase = entityLivingBase;
        this.setPosition(posX, posY, posZ);
        int i = 1;
        if (!itemStack.isEmpty() && itemStack.hasTagCompound())
        {
            this.dataManager.set(FIREWORK_ITEM, itemStack.copy());
            NBTTagCompound nbttagcompound = itemStack.getTagCompound();
            NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("Fireworks");
            i += nbttagcompound1.getByte("Flight");
        }
        this.motionX = motionX;
        this.motionY = motionY;
        this.motionZ = motionZ;
        this.lifetime = 10*i;
    }
    protected void entityInit()
    {
        this.dataManager.register(FIREWORK_ITEM, ItemStack.EMPTY);
    }
    @SideOnly(Side.CLIENT)
    public boolean isInRangeToRenderDist(double distance)
    {
        return distance < 4096.0D;
    }

    @SideOnly(Side.CLIENT)
    public boolean isInRangeToRender3d(double x, double y, double z)
    {
        return super.isInRangeToRender3d(x, y, z);
    }

    @Override
    public void onUpdate()
    {
        this.lastTickPosX = this.posX;
        this.lastTickPosY = this.posY;
        this.lastTickPosZ = this.posZ;
        super.onUpdate();
        Vec3d point1 = new Vec3d(posX,posY,posZ);
        Vec3d point2 = new Vec3d(posX+motionX,posY+motionY,posZ+motionZ);
        RayTraceResult rayTraceResult = this.world.rayTraceBlocks(point1,point2,false,true,false);
        point1 = new Vec3d(posX,posY,posZ);
        point2 = new Vec3d(posX+motionX,posY+motionY,posZ+motionZ);

        if(rayTraceResult != null)
        {
            point2 = new Vec3d(rayTraceResult.hitVec.x,rayTraceResult.hitVec.y,rayTraceResult.hitVec.z);
        }
        Entity entity = null;
        List<Entity> entityList = world.getEntitiesWithinAABBExcludingEntity(this,this.getEntityBoundingBox().expand(motionX,motionY,motionZ).grow(1.0D));
        double d0 = 0.0;
        for (int i = 0; i < entityList.size(); i++) {
            Entity entity1 = entityList.get(i);
            if ((entity1 != entityLivingBase || ticksExisted>=5)&&!(entity1 instanceof EntityDispenserFireworkRocket))
            {
                AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().grow(0.30000001192092896D);
                RayTraceResult raytraceresult1 = axisalignedbb.calculateIntercept(point1, point2);

                if (raytraceresult1 != null)
                {
                    double d1 = point1.squareDistanceTo(raytraceresult1.hitVec);

                    if (d1 < d0 || d0 == 0.0D)
                    {
                        entity = entity1;
                        d0 = d1;
                    }
                }
            }
        }
        if(entity != null)
        {
            rayTraceResult = new RayTraceResult(entity);
        }
        if(rayTraceResult != null && !ForgeEventFactory.onProjectileImpact(this,rayTraceResult))
        {
            trigExplosion();
        }
        if (!this.world.isRemote && this.fireworkAge > this.lifetime)
        {
            trigExplosion();
        }
        this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);

        float f = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
        this.rotationYaw = (float)(MathHelper.atan2(this.motionX, this.motionZ) * (180D / Math.PI));

        for (this.rotationPitch = (float)(MathHelper.atan2(this.motionY, f) * (180D / Math.PI)); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F)
        {
        }

        while (this.rotationPitch - this.prevRotationPitch >= 180.0F)
        {
            this.prevRotationPitch += 360.0F;
        }

        while (this.rotationYaw - this.prevRotationYaw < -180.0F)
        {
            this.prevRotationYaw -= 360.0F;
        }

        while (this.rotationYaw - this.prevRotationYaw >= 180.0F)
        {
            this.prevRotationYaw += 360.0F;
        }

        this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
        this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;

        if (this.fireworkAge == 0 && !this.isSilent())
        {
            this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_FIREWORK_LAUNCH, SoundCategory.AMBIENT, 3.0F, 1.0F);
        }

        ++this.fireworkAge;

        if (this.world.isRemote)
        {
            this.world.spawnParticle(EnumParticleTypes.FIREWORKS_SPARK, this.posX, this.posY - 0.3D, this.posZ, this.rand.nextGaussian() * 0.05D, -this.motionY * 0.5D, this.rand.nextGaussian() * 0.05D);
        }


    }

    private void dealExplosionDamage()
    {
        float f = 0.0F;
        ItemStack itemstack = this.dataManager.get(FIREWORK_ITEM);
        NBTTagCompound nbttagcompound = itemstack.isEmpty() ? null : itemstack.getSubCompound("Fireworks");
        NBTTagList nbttaglist = nbttagcompound != null ? nbttagcompound.getTagList("Explosions", 10) : null;

        if (nbttaglist != null && !nbttaglist.hasNoTags())
        {
            f = (float)(5 + nbttaglist.tagCount() * 2);
        }

        if (f > 0.0F)
        {
            if (!this.world.isRemote)
            {
                for (EntityLivingBase entitylivingbase : this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().grow(5.0D)))
                {
                    if (this.getDistanceSq(entitylivingbase) <= 25.0D)
                    {
                        float f1 = f * (float)Math.sqrt((5.0D - (double)this.getDistance(entitylivingbase)) / 5.0D);
                        entitylivingbase.attackEntityFrom(DamageSource.causeExplosionDamage(this.entityLivingBase), f1);
                    }
                }

            }
        }
    }
    @SideOnly(Side.CLIENT)
    public void handleStatusUpdate(byte id)
    {
        if (id == 17 && this.world.isRemote)
        {
            ItemStack itemstack = (ItemStack)this.dataManager.get(FIREWORK_ITEM);
            NBTTagCompound nbttagcompound = itemstack.isEmpty() ? null : itemstack.getSubCompound("Fireworks");
            this.world.makeFireworks(this.posX, this.posY, this.posZ, this.motionX, this.motionY, this.motionZ, nbttagcompound);
        }

        super.handleStatusUpdate(id);
    }
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        compound.setInteger("Life", this.fireworkAge);
        compound.setInteger("LifeTime", this.lifetime);
        ItemStack itemstack = (ItemStack)this.dataManager.get(FIREWORK_ITEM);

        if (!itemstack.isEmpty())
        {
            compound.setTag("FireworksItem", itemstack.writeToNBT(new NBTTagCompound()));
        }
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        this.fireworkAge = compound.getInteger("Life");
        this.lifetime = compound.getInteger("LifeTime");
        NBTTagCompound nbttagcompound = compound.getCompoundTag("FireworksItem");

        if (nbttagcompound != null)
        {
            ItemStack itemstack = new ItemStack(nbttagcompound);

            if (!itemstack.isEmpty())
            {
                this.dataManager.set(FIREWORK_ITEM, itemstack);
            }
        }
    }

    /**
     * Returns true if it's possible to attack this entity with an item.
     */
    public boolean canBeAttackedWithItem()
    {
        return false;
    }

    private void trigExplosion()
    {
        this.world.setEntityState(this, (byte)17);
        this.dealExplosionDamage();
        this.setDead();
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public float getCollisionBorderSize() {
        return 0.25F;
    }

    @Override
    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
        float f = MathHelper.sqrt(x * x + y * y + z * z);
        x = x / (double)f;
        y = y / (double)f;
        z = z / (double)f;
        this.motionX = x;
        this.motionY = y;
        this.motionZ = z;
        float f1 = MathHelper.sqrt(x * x + z * z);
        this.rotationYaw = (float)(MathHelper.atan2(x, z) * (180D / Math.PI));
        this.rotationPitch = (float)(MathHelper.atan2(y, (double)f1) * (180D / Math.PI));
        this.prevRotationYaw = this.rotationYaw;
        this.prevRotationPitch = this.rotationPitch;
    }

    /**
     * Updates the entity motion clientside, called by packets from the server
     */
    @SideOnly(Side.CLIENT)
    public void setVelocity(double x, double y, double z)
    {
        this.motionX = x;
        this.motionY = y;
        this.motionZ = z;

        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F)
        {
            float f = MathHelper.sqrt(x * x + z * z);
            this.rotationPitch = (float)(MathHelper.atan2(y, (double)f) * (180D / Math.PI));
            this.rotationYaw = (float)(MathHelper.atan2(x, z) * (180D / Math.PI));
            this.prevRotationPitch = this.rotationPitch;
            this.prevRotationYaw = this.rotationYaw;
        }
    }

}
