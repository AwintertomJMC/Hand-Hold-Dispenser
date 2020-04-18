package com.wintertom.handholddispenser.common.enchantment;

import com.wintertom.handholddispenser.init.EnchantmentRegister;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;

public class EnchantmentReduceFreeze extends Enchantment
{
    public static final String NAME = "reduce_freeze";
    public EnchantmentReduceFreeze(Rarity rarityIn, EntityEquipmentSlot[] slots) {
        super(rarityIn, EnumEnchantmentType.valueOf(EnchantmentRegister.NEW_ENCHANTMENT_NAME), slots);

    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 7+(enchantmentLevel-1)*9;
    }

    @Override
    public int getMaxEnchantability(int enchantmentLevel) {
        return 50+getMinEnchantability(enchantmentLevel);
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }
}
