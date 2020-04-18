package com.wintertom.handholddispenser.init;

import com.wintertom.handholddispenser.common.item.ItemHandHoldDispenser;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = HandHoldDispenser.modId)
public class ItemInitializer
{
    public static Item itemHandHoldDispenser = new ItemHandHoldDispenser()
            .setRegistryName(HandHoldDispenser.modId, ItemHandHoldDispenser.NAME)
            .setUnlocalizedName(ItemHandHoldDispenser.NAME);
    @SubscribeEvent
    public static void registerItem(RegistryEvent.Register<Item> itemRegister)
    {
        itemRegister.getRegistry().registerAll(
                itemHandHoldDispenser
        );
    }
}
