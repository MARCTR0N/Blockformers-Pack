package com.marctron.blockformers_pack.blockformers_pack.client.models;

import com.marctron.blockformers_pack.blockformers_pack.Blockformers_Pack;
import net.minecraft.util.ResourceLocation;
import software.bernie.transformersunlimited.geckolib3.model.AnimatedGeoModel;

public class WFC_ShockwaveModel extends AnimatedGeoModel {
    @Override
    public ResourceLocation getModelLocation(Object transformer) {
        return new ResourceLocation(Blockformers_Pack.MOD_ID, "geo/transformers/wfc_shockwave.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(Object transformer) {
        return new ResourceLocation(Blockformers_Pack.MOD_ID, "textures/transformers/wfc_shockwave.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(Object transformer) {
        return new ResourceLocation(Blockformers_Pack.MOD_ID, "animations/transformers/wfc_shockwave.animation.json");
    }
}
