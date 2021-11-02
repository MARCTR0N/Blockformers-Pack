package com.marctron.blockformers_pack.blockformers_pack.client.models;

import com.marctron.blockformers_pack.blockformers_pack.Blockformers_Pack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.resource.GeckoLibCache;
import software.bernie.shadowed.eliotlash.molang.MolangParser;

public class G1OptimusPrimeModel extends AnimatedGeoModel {

    @Override
    public ResourceLocation getModelLocation(Object transformer) {
        return new ResourceLocation(Blockformers_Pack.MOD_ID, "geo/transformers/g1_optimus_prime.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(Object transformer) {
        return new ResourceLocation(Blockformers_Pack.MOD_ID, "textures/transformers/g1_optimus_prime.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(Object transformer) {
        return new ResourceLocation(Blockformers_Pack.MOD_ID, "animations/transformers/g1_optimus_prime.animation.json");
    }

    @Override
    public void setMolangQueries(IAnimatable animatable, double currentTick) {
        MolangParser parser = GeckoLibCache.getInstance().parser;
        Minecraft minecraftInstance = Minecraft.getInstance();
        ClientPlayerEntity player = Minecraft.getInstance().player;
        ;
        double wX = player.getPositionVec().getX() - player.prevPosX;
        double lookZ = player.getLookVec().getZ();
        double lookY = player.getLookVec().getY();
        double lookX = player.getLookVec().getX();
        double lookDirection = Math.sqrt(lookZ*lookZ+lookX*lookX+lookY*lookY);
        double mx= player.getPosX() - player.prevPosX;
        double mz= player.getPosZ() - player.prevPosZ;
        double offset = player.cameraYaw * Math.sqrt(Math.pow(Math.atan2(mx, mz) * 180/ Math.PI, 2));


        //System.out.println(offset);
        double walkX = 1;
        parser.setValue("walk_x", walkX);
    }
}
