package com.marctron.blockformers_pack.blockformers_pack;

import com.marctron.transformersunlimited.TransformersUnlimited;
import com.marctron.transformersunlimited.objects.transformers.Transformer;
import com.marctron.transformersunlimited.objects.transformers.TransformersPack;
import com.marctron.blockformers_pack.blockformers_pack.transformers.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Blockformers_Pack.MOD_ID)
public class Blockformers_Pack {



    public static final String MOD_ID = "blockformers_pack";
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();
    public static final TransformersPack PACK = new TransformersPack();
    static {
        if(!FMLEnvironment.production){
            TransformersUnlimited.SHOULD_INIT_GECKOLIB = true;
        }
    }

    public Blockformers_Pack() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        Sounds.SOUNDS.register(modEventBus);
    }

    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onTransformersRegistry(final RegistryEvent.Register<Transformer> transformerRegisterEvent) {
            //Breakdown
            //Transformer wfcBreakdown = new WFC_Breakdown();
            //wfcBreakdown.setRegistryName(new ResourceLocation(MOD_ID,"wfc_breakdown"));
            //transformerRegisterEvent.getRegistry().register(wfcBreakdown);

            Transformer G1OptimusPrime = new G1OptimusPrime();
            G1OptimusPrime.setRegistryName(new ResourceLocation(MOD_ID,"g1_optimus_prime"));
            transformerRegisterEvent.getRegistry().register(G1OptimusPrime);

            Transformer G1Thundercracker = new G1Thundercracker();
            G1Thundercracker.setRegistryName(new ResourceLocation(MOD_ID,"g1_thundercracker"));
            transformerRegisterEvent.getRegistry().register(G1Thundercracker);

            Transformer G1Hook = new G1Hook();
            G1Hook.setRegistryName(new ResourceLocation(MOD_ID,"g1_hook"));
            transformerRegisterEvent.getRegistry().register(G1Hook);
        }

        @SubscribeEvent
        public static void onTransformersPackRegistry(final RegistryEvent.Register<TransformersPack> transformerRegisterEvent) {
            PACK.setRegistryName(new ResourceLocation(MOD_ID,"blockformers_pack"));
            transformerRegisterEvent.getRegistry().register(PACK);
        }

    }
}
