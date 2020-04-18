package com.wintertom.handholddispenser.common.util;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class ItemMathHelper
{
    public static Vec3d calculateWithYawAndPitch(float angle,float yaw, float pitch,float y)
    {
        yaw += 90;
        float cos = (float) Math.cos(angle*0.017453292F);
        float sin = (float) Math.sin(angle*0.017453292F);
        float x = cos* MathHelper.cos(yaw*0.017453292F)*MathHelper.cos(pitch*0.017453292F)-sin*MathHelper.sin(yaw*0.017453292F);
        float z = cos*MathHelper.sin(yaw*0.017453292F)*MathHelper.cos(pitch*0.017453292F)+sin*MathHelper.cos(yaw*0.017453292F);
        return new Vec3d(x,y,z);
    }
}
