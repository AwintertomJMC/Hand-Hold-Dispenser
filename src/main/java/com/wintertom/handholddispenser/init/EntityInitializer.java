package com.wintertom.handholddispenser.init;

import com.wintertom.handholddispenser.common.entity.projectile.EntityDispenserFireworkRocket;
import com.wintertom.handholddispenser.common.entity.render.EntityDispenserFireworkRocketRender;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(modid = HandHoldDispenser.modId)
public class EntityInitializer
{
    @SubscribeEvent
    public static void registerEntity(RegistryEvent.Register<EntityEntry> event)
    {
        event.getRegistry().register(EntityEntryBuilder.create()
        .entity(EntityDispenserFireworkRocket.class)
        .id(new ResourceLocation(HandHoldDispenser.modId,"fireworks"),1)
        .name("entity_dispenser_firework_rocket")
        .tracker(100,1,false)
        .build());
    }
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void renderModel(ModelRegistryEvent event)
    {
        RenderingRegistry.registerEntityRenderingHandler(EntityDispenserFireworkRocket.class, new IRenderFactory<EntityDispenserFireworkRocket>() {
            @Override
            public Render<? super EntityDispenserFireworkRocket> createRenderFor(RenderManager manager) {
                return new EntityDispenserFireworkRocketRender(manager);
            }
        });
    }
}
