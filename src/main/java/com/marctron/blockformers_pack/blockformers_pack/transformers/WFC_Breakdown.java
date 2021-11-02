package com.marctron.blockformers_pack.blockformers_pack.transformers;

import com.marctron.transformersunlimited.capabilities.ITransformer;
import com.marctron.transformersunlimited.capabilities.TransformerProvider;
import com.marctron.transformersunlimited.objects.client.RenderTypes;
import com.marctron.transformersunlimited.objects.client.renderer.TransformerGeoLayer;
import com.marctron.transformersunlimited.objects.items.guns.GunItemBase;
import com.marctron.transformersunlimited.objects.transformers.AltModes;
import com.marctron.transformersunlimited.objects.transformers.Factions;
import com.marctron.transformersunlimited.objects.transformers.Transformer;
import com.marctron.blockformers_pack.blockformers_pack.Blockformers_Pack;
import com.marctron.blockformers_pack.blockformers_pack.Sounds;
import com.marctron.blockformers_pack.blockformers_pack.client.models.WFC_BreakdownModel;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.HandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.vector.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.event.SoundKeyframeEvent;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.util.RenderUtils;

import java.util.concurrent.atomic.AtomicBoolean;

public class WFC_Breakdown extends Transformer {


    public WFC_Breakdown() {
        super(new Transformer.Properties()
                .setName("WFC Breakdown")
                .setCharacterIcon(new ResourceLocation(Blockformers_Pack.MOD_ID, "textures/icons/wfc_breakdown.png"))
                .setModel(new WFC_BreakdownModel())
                .setFaction(Factions.DECEPTICON)
                .setJumpHeight(2)
                .setSize(new EntitySize(0.6F, 1.75F, false))
                .setEyeHeight(1.6F)
                .setRangeMultiplier(2)
                .setAltMode(AltModes.LAND_VEHICLE)
                .setAltModeSize(new EntitySize(0.9F, 0.5F, false))
                .setAltModeEyeHeight(0.5F)
                .setAltModeTransformAnimation("robot_to_car")
                .setAltModeMotionTransformAnimation("robot_to_car")
                .setAltModeSpeedMultiplier(1.3F)
                .setAttackMultiplier(20)
                .setNormalModeSpeedMultiplier(0.50F)
                .setNormalModeTransformAnimation("car_to_robot")
                .setNormalModeMotionTransformAnimation("car_to_robot")
                .setHasGlowingTexture(true)
                , Blockformers_Pack.PACK);
    }

    @Override
    public boolean shouldCallPredicate(AnimationEvent<ITransformer> event, PlayerEntity player) {
        AtomicBoolean shouldCall = new AtomicBoolean(true);
        player.getCapability(TransformerProvider.TRANSFORMER_CAPABILITY).ifPresent(h -> {
            if (h.getAttackingCount() > 0 && h.getAttackingCount() < getMaxAttackAnimationTicks()) {
                shouldCall.set(false);
            }
            if (h.getTransformationCount() > 0 && h.getTransformationCount() < getMaxTransformAnimationTicks()) {
                shouldCall.set(false);
            }
        });
        return shouldCall.get();
    }

    @Override
    public float getScale(){
        return 40;
    }

    @Override
    public int getMaxTransformAnimationTicks() {
        return 70;
    }

    private <ENTITY extends IAnimatable> void soundListener(SoundKeyframeEvent<ENTITY> event) {
        ClientPlayerEntity player = Minecraft.getInstance().player;
        if (player != null) {
            player.getCapability(TransformerProvider.TRANSFORMER_CAPABILITY).ifPresent((h) ->
            {
                if (!h.isTransformed()) {
                    if (event.sound.equals("to_robot")){
                        player.playSound((SoundEvent) Sounds.BREAKDOWN_TO_ROBOT.get(), 2.0F, 1.09F);
                    }
                    if (event.sound.equals("walk_small_1")){
                        player.playSound((SoundEvent) Sounds.STEP_SMALL_1.get(), 2.0F, 1.09F);
                    }
                    if (event.sound.equals("walk_small_2")){
                        player.playSound((SoundEvent) Sounds.STEP_SMALL_2.get(), 2.0F, 1.09F);
                    }
                    if (event.sound.equals("walk_small_3")){
                        player.playSound((SoundEvent) Sounds.STEP_SMALL_3.get(), 2.0F, 1.09F);
                    }
                    if (event.sound.equals("walk_small_4")){
                        player.playSound((SoundEvent) Sounds.STEP_SMALL_4.get(), 2.0F, 1.09F);
                    }
                }
                else if (h.isTransformed()) {
                    if (event.sound.equals("to_car")){
                        player.playSound((SoundEvent) Sounds.BREAKDOWN_TO_VEHICLE.get(), 2.0F, 1.09F);
                    }
                }
            });

        }

    }

    @Override
    public PlayState predicate(AnimationEvent<ITransformer> event, PlayerEntity player)  {

        event.getController().registerSoundListener(this::soundListener);
        //isJumping=
        //System.out.println(event.isMoving());

        double motionX = player.prevPosX - player.getPosX();
        double motionY = player.prevPosY - player.getPosY();
        double motionZ = player.prevPosZ - player.getPosZ();
        boolean isMoving= motionX + motionZ !=0;

        player.getCapability(TransformerProvider.TRANSFORMER_CAPABILITY).ifPresent((h) -> {
            if (!h.isTransformed()) {
                if(isMoving){
                    event.getController().setAnimation((new AnimationBuilder()).addAnimation("walk", true));
                }
                else {
                    event.getController().setAnimation((new AnimationBuilder()).addAnimation("idle", true));
                }
            }
            else
                event.getController().setAnimation((new AnimationBuilder()).addAnimation("altmode", true));
        });
        return PlayState.CONTINUE;
    }
    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, AbstractClientPlayerEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, TransformerGeoLayer layer, ITransformer transformer) {
        //super.render(matrixStackIn, bufferIn, packedLightIn, entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, layer, transformer);
        matrixStackIn.push();
        matrixStackIn.rotate(new Quaternion(0, 0, 180, true));

        matrixStackIn.translate(0, -1.5, 0);
        matrixStackIn.scale(0.5F,0.5F,0.5F);
        layer.setModelProvider(getAnimatedModel());

        AnimatedGeoModel<IAnimatable> animatedGeoModel = layer.getGeoModelProvider();
        GeoModel geoModel = animatedGeoModel.getModel(animatedGeoModel.getModelLocation(transformer));

        ItemStack stack = entitylivingbaseIn.getHeldItemMainhand();

        if (!Minecraft.getInstance().isGamePaused()) {
            layer.getGeoModelProvider().setLivingAnimations((com.marctron.transformersunlimited.capabilities.Transformer) transformer, layer.getUniqueID(transformer), null);
        }
        GeoBone neck = (GeoBone)geoModel.getBone("neck").orElse(new GeoBone());
        GeoBone head = (GeoBone)geoModel.getBone("Head").orElse(new GeoBone());
        GeoBone Right_Arm = ((GeoBone)geoModel.getBone("Right_Arm").get());
        GeoBone Right_Lower_Arm = ((GeoBone)geoModel.getBone("Right_Lower_Arm").get());
        GeoBone Left_Arm = ((GeoBone)geoModel.getBone("Left_Arm").get());
        GeoBone Left_Lower_Arm = ((GeoBone)geoModel.getBone("Left_Lower_Arm").get());

        GeoBone lower_arm = ((GeoBone)geoModel.getBone("lower_arm").get());
        GeoBone Left_Hand = ((GeoBone)geoModel.getBone("Right_Hand").get());

        GeoBone Right_Leg = ((GeoBone)geoModel.getBone("Right_Leg").get());
        GeoBone Right_Lower_Leg = ((GeoBone)geoModel.getBone("Right_Lower_Leg").get());
        GeoBone Right_Foot = ((GeoBone)geoModel.getBone("Right_Foot").get());
        GeoBone Left_Foot = ((GeoBone)geoModel.getBone("Left_Foot").get());
        GeoBone Left_Leg = ((GeoBone)geoModel.getBone("Left_Leg").get());
        GeoBone Left_Lower_Leg = ((GeoBone)geoModel.getBone("Left_Lower_Leg").get());

        GeoBone Body = (GeoBone)geoModel.getBone("Waist").get();

        //if (ModList.get().isLoaded("firstpersonmod")) {
        //    neck.setHidden(!FirstPersonModelUtil.shouldRenderHead());
        //}

        if(entitylivingbaseIn.isSneaking()){
            Body.setPositionY(6);
        }

        if (Minecraft.getInstance().isGamePaused()) {
            head.setRotationX(0);
            head.setRotationY(0);
            head.setRotationZ(0);
        }
        head.setRotationX(head.getRotationX()-((PlayerModel)layer.getEntityModel()).bipedHead.rotateAngleX);
        head.setRotationY(head.getRotationY()-((PlayerModel)layer.getEntityModel()).bipedHead.rotateAngleY);
        head.setRotationZ(head.getRotationZ()-((PlayerModel)layer.getEntityModel()).bipedHead.rotateAngleZ);

        if (stack.getItem() instanceof GunItemBase){
            Right_Arm.setRotationX(head.getRotationX());
            Right_Arm.setRotationY(neck.getRotationY()+head.getRotationY());
            Right_Arm.setRotationZ(head.getRotationZ());
            Right_Lower_Arm.setRotationX(1.5F);
            lower_arm.setHidden(true);
        }
        else
            lower_arm.setHidden(false);
        //Jump and Falling

        if (entitylivingbaseIn instanceof PlayerEntity) {

            double motionY= entitylivingbaseIn.getMotion().y;
            float upwardPose = (float) (1 / (1 + Math.exp(-10 * (motionY + 0.2))));
            float downwardPose = (float) (1 / (1 + Math.exp(10 * (motionY + 0.2))));
            int backwardInverter = 1;


        }
        //Right_Leg.setRotationX(0.2F * upwardPose);
        //Left_Leg.setRotationX(-0.8F * upwardPose);
        //Right_Lower_Leg.setRotationX(0.3F * upwardPose);
        //Left_Lower_Leg.setRotationX(1.5F * upwardPose);

        //walk(Right_Leg, 0.5F * globalSpeed, 0.2F * globalDegree * downwardPose, false, 0, 0, f1, 1);
        //walk(Left_Leg, 0.5F * globalSpeed, 0.2F * globalDegree * downwardPose, true, 0, 0, f1, 1);
        //walk(Right_Lower_Leg, 0.5F * globalSpeed, 0.2F * globalDegree * downwardPose, false, -2.2F * backwardInverter, 0F, f1, 1);
        //walk(Left_Lower_Leg, 0.5F * globalSpeed, 0.2F * globalDegree * downwardPose, true, -2.2F * backwardInverter, 0F, f1, 1);

        //Right_Leg.rotateAngleX -= 1.2 * downwardPose;
        //Left_Leg.rotateAngleX += 0.7 * downwardPose;
        //Right_Lower_Leg.rotateAngleX += 2 * downwardPose;
        //Left_Lower_Leg.rotateAngleX += 0.5 * downwardPose;

        //Right_Leg.rotateAngleX -= .5F *downwardPose;
        //Left_Leg.rotateAngleX -= .5F *downwardPose;


        //this.Chest.rotateAngleX = 0.4F *downwardPose;
        //this.Torso.rotateAngleX = -.2F *downwardPose;
        //this.Torso2.rotateAngleX = 0.2F *downwardPose;


        //Right_Arm.rotateAngleX = 0.6F*downwardPose;
        //Left_Arm.rotateAngleX = 0.6F*downwardPose;

        //Right_Arm.rotateAngleZ = 0.4F *downwardPose;
        //Left_Arm.rotateAngleZ = -0.4F *downwardPose;

        //Right_Lower_Arm.rotateAngleX = -1.1F *downwardPose;
        //Left_Lower_Arm.rotateAngleX = -1.1F *downwardPose;

        //Right_Lower_Arm.rotateAngleY = -0.2F *downwardPose;
        //Left_Lower_Arm.rotateAngleY = 0.2F *downwardPose;

        GeoBone RightFrontWheel = ((GeoBone)geoModel.getBone("Wheel").get());
        GeoBone LeftFrontWheel = ((GeoBone)geoModel.getBone("Wheel4").get());
        GeoBone RightBackWheel = ((GeoBone)geoModel.getBone("Wheel6").get());
        GeoBone LeftBackWheel = ((GeoBone)geoModel.getBone("Wheel5").get());

        double mx= entitylivingbaseIn.getPosX() - entitylivingbaseIn.prevPosX;
        double my= entitylivingbaseIn.getPosY() - entitylivingbaseIn.prevPosY;
        double mz= entitylivingbaseIn.getPosZ() - entitylivingbaseIn.prevPosZ;
        double speed =(Math.sqrt(mx*mx+my*my+mz*mz));
        //System.out.println(speed);
        float wheelSpinSpeed = (speed < 1 ? -entitylivingbaseIn.limbSwing : entitylivingbaseIn.limbSwingAmount) * 0.7F;

        for (GeoBone wheels : new GeoBone[] {RightFrontWheel, LeftFrontWheel, RightBackWheel, LeftBackWheel})
        {
            wheels.setRotationX((float) (speed *0.1F));
        }
        for (GeoBone steeringwheels : new GeoBone[] {RightFrontWheel, LeftFrontWheel})
        {
            steeringwheels.setRotationY(0);
        }

        if ((entitylivingbaseIn.getPrimaryHand() == HandSide.LEFT ? entitylivingbaseIn.getHeldItemMainhand() : entitylivingbaseIn.getHeldItemOffhand()).getItem() instanceof GunItemBase) {

        }




        RenderType type = RenderType.getEntityTranslucent(layer.getGeoModelProvider().getTextureLocation(transformer));
        layer.setActualRenderType(type);
        Minecraft.getInstance().textureManager.bindTexture(layer.getTextureLocation(transformer));
        layer.render(layer.getGeoModelProvider().getModel(layer.getGeoModelProvider().getModelLocation(transformer)), transformer, partialTicks, type, matrixStackIn, null, bufferIn.getBuffer(type), packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        if(hasGlowingTexture()){
            layer.setRenderingGlowing(true);
            Minecraft.getInstance().textureManager.bindTexture(layer.getTextureLocation(transformer));
            type = RenderTypes.getGlow(layer.getTextureLocation(transformer));
            layer.render(layer.getGeoModelProvider().getModel(layer.getGeoModelProvider().getModelLocation(transformer)), transformer, partialTicks, type, matrixStackIn, null, bufferIn.getBuffer(type), packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        }
        matrixStackIn.pop();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public boolean onRenderBone(GeoBone bone, MatrixStack stack, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha, TransformerGeoLayer layer) {
        if (!layer.isRenderingGlowing()) {
            if (bone.getName().equals("Right_Item")) {
                stack.push();
                stack.translate(0.95D, 1.75D, -0.25D);
                stack.rotate(Vector3f.XP.rotationDegrees(-75.0F));
                if (!((layer.getActualEntity().getPrimaryHand() == HandSide.RIGHT ? layer.getActualEntity().getHeldItemMainhand() : layer.getActualEntity().getHeldItemOffhand()).getItem() instanceof GunItemBase)) {
                    stack.scale(1.5F, 1.5F, 1.5F);
                }

                Minecraft.getInstance().getItemRenderer().renderItem(layer.getActualEntity().getPrimaryHand() == HandSide.RIGHT ? layer.getActualEntity().getHeldItemMainhand() : layer.getActualEntity().getHeldItemOffhand(), ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, packedLightIn, packedOverlayIn, stack, layer.getActualBuffer());
                stack.pop();
                bufferIn = layer.getActualBuffer().getBuffer(layer.getActualRenderType());
            }

            if (bone.getName().equals("Left_Item")) {
                stack.push();
                stack.translate(-0.95D, 1.75D, -0.25D);
                stack.rotate(Vector3f.XP.rotationDegrees(-5.0F));
                if (!((layer.getActualEntity().getPrimaryHand() == HandSide.LEFT ? layer.getActualEntity().getHeldItemMainhand() : layer.getActualEntity().getHeldItemOffhand()).getItem() instanceof GunItemBase)) {
                    stack.scale(1.5F, 1.5F, 1.5F);
                }

                Minecraft.getInstance().getItemRenderer().renderItem(layer.getActualEntity().getPrimaryHand() == HandSide.LEFT ? layer.getActualEntity().getHeldItemMainhand() : layer.getActualEntity().getHeldItemOffhand(), ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, packedLightIn, packedOverlayIn, stack, layer.getActualBuffer());
                stack.pop();
                bufferIn = layer.getActualBuffer().getBuffer(layer.getActualRenderType());
            }

            if (bone.getName().equals("Head")) {
                stack.push();
                RenderUtils.translate(bone, stack);
                RenderUtils.moveToPivot(bone, stack);
                Matrix4f matrix = new Matrix4f(stack.getLast().getMatrix());
                matrix.invert();
                Vector4f vector4f = new Vector4f(0.0F, 0.0F, 0.0F, 1.0F);
                vector4f.transform(matrix);
                Vector3d pos = new Vector3d((double)vector4f.getX(), (double)(-vector4f.getY()), (double)vector4f.getZ());
                layer.getActualCapability().setCameraPosition(pos);
                stack.pop();
            }
        }

        return true;
    }
}
