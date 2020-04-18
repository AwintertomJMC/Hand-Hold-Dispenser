package com.wintertom.handholddispenser.init;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(modid = HandHoldDispenser.modId,value = Side.CLIENT)
public class ItemModelRegister
{
    @SubscribeEvent
    public static void itemModelRegister(ModelRegistryEvent event)
    {
        ModelLoader.setCustomModelResourceLocation(ItemInitializer.itemHandHoldDispenser,
                0,
                new ModelResourceLocation(ItemInitializer.itemHandHoldDispenser.getRegistryName(),"inventory"));

    }
}
