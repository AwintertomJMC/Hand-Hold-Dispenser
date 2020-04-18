package com.wintertom.handholddispenser.init;

import com.wintertom.handholddispenser.common.enchantment.EnchantmentFierceFire;
import com.wintertom.handholddispenser.common.enchantment.EnchantmentReduceFreeze;
import com.wintertom.handholddispenser.common.enchantment.EnchantmentScatter;
import com.wintertom.handholddispenser.common.enchantment.EnchantmentUnBurn;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = HandHoldDispenser.modId)
public class EnchantmentRegister
{
    public static final String NEW_ENCHANTMENT_NAME = "hand_held_dispenser";
    public static Enchantment enchantmentFierceFire = new EnchantmentFierceFire(
            Enchantment.Rarity.COMMON,
            new EntityEquipmentSlot[]{EntityEquipmentSlot.MAINHAND})
            .setRegistryName(HandHoldDispenser.modId,EnchantmentFierceFire.NAME)
            .setName(EnchantmentFierceFire.NAME);
    public static Enchantment enchantmentReduceFreeze = new EnchantmentReduceFreeze(
            Enchantment.Rarity.UNCOMMON,
            new EntityEquipmentSlot[]{EntityEquipmentSlot.MAINHAND})
            .setRegistryName(HandHoldDispenser.modId,EnchantmentReduceFreeze.NAME)
            .setName(EnchantmentReduceFreeze.NAME);
    public static Enchantment enchantmentUnBurn = new EnchantmentUnBurn(
            Enchantment.Rarity.UNCOMMON,
            new EntityEquipmentSlot[]{EntityEquipmentSlot.MAINHAND})
            .setRegistryName(HandHoldDispenser.modId,EnchantmentUnBurn.NAME)
            .setName(EnchantmentUnBurn.NAME);
    public static Enchantment enchantmentScatter = new EnchantmentScatter(
            Enchantment.Rarity.RARE,
            new EntityEquipmentSlot[]{EntityEquipmentSlot.MAINHAND})
            .setRegistryName(HandHoldDispenser.modId,EnchantmentScatter.NAME)
            .setName(EnchantmentScatter.NAME);
    @SubscribeEvent
    public static void registerEnchantment(RegistryEvent.Register<Enchantment> register)
    {
        register.getRegistry().registerAll(enchantmentReduceFreeze,
                enchantmentFierceFire,
                enchantmentUnBurn,
                enchantmentScatter);

    }
}
