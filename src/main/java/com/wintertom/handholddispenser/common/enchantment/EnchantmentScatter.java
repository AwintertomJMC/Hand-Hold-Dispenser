package com.wintertom.handholddispenser.common.enchantment;

import com.wintertom.handholddispenser.init.EnchantmentRegister;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;

public class EnchantmentScatter extends Enchantment {
    public static final String NAME = "scatter";
    public EnchantmentScatter(Rarity rarityIn, EntityEquipmentSlot[] slots) {
        super(rarityIn, EnumEnchantmentType.valueOf(EnchantmentRegister.NEW_ENCHANTMENT_NAME), slots);
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 20+(enchantmentLevel-1)*9;
    }

    @Override
    public int getMaxEnchantability(int enchantmentLevel) {
        return getMinEnchantability(enchantmentLevel)+50;
    }

    @Override
    public int getMaxLevel() {
        return 2;
    }
}
