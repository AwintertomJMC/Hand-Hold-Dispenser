package com.wintertom.handholddispenser.init;

import com.google.common.base.Predicate;
import com.wintertom.handholddispenser.common.item.ItemHandHoldDispenser;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import javax.annotation.Nullable;

@Mod(modid = HandHoldDispenser.modId,useMetadata = true)
public class HandHoldDispenser {
    public static final CreativeTabs HAND_HOLD_DISPENSER_MOD_TAB = new CreativeTabs("hand_hold_dispenser_mod_tab") {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(ItemInitializer.itemHandHoldDispenser);
        }
    };
    public static final String modId = "handholddispenser";
    @Mod.Instance
    public static HandHoldDispenser handHoldDispenser;
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        EnumEnchantmentType enumEnchantmentType = EnumHelper.addEnchantmentType(EnchantmentRegister.NEW_ENCHANTMENT_NAME, new Predicate<Item>() {
            @Override
            public boolean apply(@Nullable Item input) {
                return input instanceof ItemHandHoldDispenser;
            }
        });
        HAND_HOLD_DISPENSER_MOD_TAB.setRelevantEnchantmentTypes(enumEnchantmentType);
    }
    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {

    }
}
