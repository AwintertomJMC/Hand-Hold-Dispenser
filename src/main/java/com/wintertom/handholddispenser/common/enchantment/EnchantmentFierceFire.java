package com.wintertom.handholddispenser.common.enchantment;

import com.wintertom.handholddispenser.init.EnchantmentRegister;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;

public class EnchantmentFierceFire extends Enchantment {
    public static final String NAME = "fierce_fire";
    public EnchantmentFierceFire(Rarity rarityIn, EntityEquipmentSlot[] slots) {
        super(rarityIn, EnumEnchantmentType.valueOf(EnchantmentRegister.NEW_ENCHANTMENT_NAME), slots);
    }
    public int getMinEnchantability(int enchantmentLevel)
    {
        return 1 + (enchantmentLevel - 1) * 7;
    }

    /**
     * Returns the maximum value of enchantability needed on the enchantment level passed.
     */
    public int getMaxEnchantability(int enchantmentLevel)
    {
        return this.getMinEnchantability(enchantmentLevel) + 20;
    }

    /**
     * Returns the maximum level that the enchantment can have.
     */
    public int getMaxLevel()
    {
        return 5;
    }
}
