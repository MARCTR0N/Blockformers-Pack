package com.marctron.blockformers_pack.blockformers_pack;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class Sounds {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Blockformers_Pack.MOD_ID);

    public static final RegistryObject<SoundEvent> BREAKDOWN_TO_VEHICLE = SOUNDS.register("breakdown_to_vehicle", () -> new SoundEvent(new ResourceLocation(Blockformers_Pack.MOD_ID, "breakdown_to_vehicle")));
    public static final RegistryObject<SoundEvent> BREAKDOWN_TO_ROBOT = SOUNDS.register("breakdown_to_robot", () -> new SoundEvent(new ResourceLocation(Blockformers_Pack.MOD_ID, "breakdown_to_robot")));


    public static final RegistryObject<SoundEvent> G1_STEP_1 = SOUNDS.register("g1_step_1", () -> new SoundEvent(new ResourceLocation(Blockformers_Pack.MOD_ID, "g1_step_1")));
    public static final RegistryObject<SoundEvent> G1_STEP_2 = SOUNDS.register("g1_step_2", () -> new SoundEvent(new ResourceLocation(Blockformers_Pack.MOD_ID, "g1_step_2")));

    public static final RegistryObject<SoundEvent> G1_TRANSFORM_TO_VEHICLE = SOUNDS.register("g1_transform_to_vehicle", () -> new SoundEvent(new ResourceLocation(Blockformers_Pack.MOD_ID, "g1_transform_to_vehicle")));
    public static final RegistryObject<SoundEvent> G1_TRANSFORM_TO_ROBOT = SOUNDS.register("g1_transform_to_robot", () -> new SoundEvent(new ResourceLocation(Blockformers_Pack.MOD_ID, "g1_transform_to_robot")));

    public static final RegistryObject<SoundEvent> STEP_SMALL_1 = SOUNDS.register("step_small_1", () -> new SoundEvent(new ResourceLocation(Blockformers_Pack.MOD_ID, "step_small_1")));
    public static final RegistryObject<SoundEvent> STEP_SMALL_2 = SOUNDS.register("step_small_2", () -> new SoundEvent(new ResourceLocation(Blockformers_Pack.MOD_ID, "step_small_2")));
    public static final RegistryObject<SoundEvent> STEP_SMALL_3 = SOUNDS.register("step_small_3", () -> new SoundEvent(new ResourceLocation(Blockformers_Pack.MOD_ID, "step_small_3")));
    public static final RegistryObject<SoundEvent> STEP_SMALL_4 = SOUNDS.register("step_small_4", () -> new SoundEvent(new ResourceLocation(Blockformers_Pack.MOD_ID, "step_small_4")));

    public static void register() {}
}