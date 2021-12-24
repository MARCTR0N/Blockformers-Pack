package com.marctron.blockformers_pack.blockformers_pack.transformers;

import com.marctron.blockformers_pack.blockformers_pack.Blockformers_Pack;
import com.marctron.blockformers_pack.blockformers_pack.Sounds;
import com.marctron.blockformers_pack.blockformers_pack.client.models.G1HookModel;
import com.marctron.blockformers_pack.blockformers_pack.client.models.G1OptimusPrimeModel;
import com.marctron.transformersunlimited.capabilities.ITransformer;
import com.marctron.transformersunlimited.capabilities.TransformerProvider;
import com.marctron.transformersunlimited.objects.client.RenderTypes;
import com.marctron.transformersunlimited.objects.client.renderer.TransformerGeoLayer;
import com.marctron.transformersunlimited.objects.items.guns.GunItemBase;
import com.marctron.transformersunlimited.objects.transformers.AltModes;
import com.marctron.transformersunlimited.objects.transformers.Factions;
import com.marctron.transformersunlimited.objects.transformers.Transformer;
import com.marctron.transformersunlimited.util.FirstPersonModelUtil;
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
import net.minecraft.util.HandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.vector.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.ModList;
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

public class G1Hook extends Transformer {


    public G1Hook() {
        super(new Properties()
                .setName("G1 Hook")
                .setCharacterIcon(new ResourceLocation(Blockformers_Pack.MOD_ID, "textures/icons/g1_hook.png"))
                .setModel(new G1HookModel())
                .setFaction(Factions.DECEPTICON)
                .setJumpHeight(4)
                .setSize(new EntitySize(2.5F, 7.5F, false))
                .setEyeHeight(7F)
                .setRangeMultiplier(2)
                .setAltMode(AltModes.LAND_VEHICLE)
                .setAltModeSize(new EntitySize(2.75F, 3F, false))
                .setAltModeEyeHeight(2.0F)
                .setAltModeTransformAnimation("transform")
                .setAltModeMotionTransformAnimation("transform")
                .setAltModeSpeedMultiplier(1.3F)
                .setAttackMultiplier(20)
                .setNormalModeSpeedMultiplier(1.2F)
                .setNormalModeTransformAnimation("transform_back")
                .setNormalModeMotionTransformAnimation("transform_back")
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
        return 20;
    }

    @Override
    public int getMaxTransformAnimationTicks() {
        return 65;
    }

    private <ENTITY extends IAnimatable> void soundListener(SoundKeyframeEvent<ENTITY> event) {
        ClientPlayerEntity player = Minecraft.getInstance().player;
        if (player != null) {
            player.getCapability(TransformerProvider.TRANSFORMER_CAPABILITY).ifPresent((h) ->
            {
                if (!h.isTransformed()) {

                    if (event.sound.equals("transform_to_robot")){
                        player.playSound((SoundEvent) Sounds.G1_TRANSFORM_TO_ROBOT.get(), 2.0F, 1.15F);
                    }
                    if (event.sound.equals("step1")){
                        player.playSound((SoundEvent) Sounds.G1_STEP_1.get(), 2.0F, 1.0F);
                    }
                    if (event.sound.equals("step2")){
                        player.playSound((SoundEvent) Sounds.G1_STEP_2.get(), 2.0F, 1.0F);
                    }
                }
                else if (h.isTransformed()) {
                    if (event.sound.equals("transform_to_vehicle")){
                        player.playSound((SoundEvent) Sounds.G1_TRANSFORM_TO_VEHICLE.get(), 2.0F, 1.15F);
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
                    if(player.isSprinting()){
                        event.getController().setAnimation((new AnimationBuilder()).addAnimation("run", true));
                    }
                    else
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

    public static void setupModel(GeoModel model, AbstractClientPlayerEntity playerEntity, PlayerModel<AbstractClientPlayerEntity> playerModel) {
        GeoBone head = model.getBone("Head").orElse(new GeoBone());
        GeoBone leftShoulder = model.getBone("Left_Arm").orElse(new GeoBone());
        GeoBone leftLowerArm = model.getBone("Left_Lower_Arm").orElse(new GeoBone());
        GeoBone rightShoulder = model.getBone("Right_Arm").orElse(new GeoBone());
        GeoBone rightLowerArm = model.getBone("Right_Lower_Arm").orElse(new GeoBone());

        head.setRotationX(-playerModel.bipedHead.rotateAngleX);
        head.setRotationY(-playerModel.bipedHead.rotateAngleY);
        head.setRotationZ(-playerModel.bipedHead.rotateAngleZ);

        if ((playerEntity.getPrimaryHand() == HandSide.RIGHT ? playerEntity.getHeldItemMainhand() : playerEntity.getHeldItemOffhand()).getItem() instanceof GunItemBase){

            if(Minecraft.getInstance().gameSettings.keyBindAttack.isKeyDown()){
                leftShoulder.setRotationX(-playerModel.bipedHead.rotateAngleX + (float) (Math.PI * 0.3));
                leftShoulder.setRotationY(head.getRotationY() +(float)(Math.PI*0.1));
                leftShoulder.setRotationZ(0);

                leftLowerArm.setRotationX((float)(Math.PI*0.1));
                leftLowerArm.setRotationY(0);
                leftLowerArm.setRotationZ(0);
            }
            else {
                leftShoulder.setRotationX(-playerModel.bipedHead.rotateAngleX + (float) (Math.PI * 0.1));
                leftShoulder.setRotationY(head.getRotationY());
                leftShoulder.setRotationZ(0);

                leftLowerArm.setRotationX((float) (Math.PI * 0.4));
                leftLowerArm.setRotationY(0);
                leftLowerArm.setRotationZ(0);
            }
        }
        if ((playerEntity.getPrimaryHand() == HandSide.LEFT ? playerEntity.getHeldItemMainhand() : playerEntity.getHeldItemOffhand()).getItem() instanceof GunItemBase){

            if(Minecraft.getInstance().gameSettings.keyBindAttack.isKeyDown()){
                rightShoulder.setRotationX(-playerModel.bipedHead.rotateAngleX + (float) (Math.PI * 0.3));
                rightShoulder.setRotationY(head.getRotationY() - (float) (Math.PI * 0.1));
                rightShoulder.setRotationZ(0);

                rightLowerArm.setRotationX((float)(Math.PI*0.1));
                rightLowerArm.setRotationY(0);
                rightLowerArm.setRotationZ(0);
            }
            else {
                rightShoulder.setRotationX(-playerModel.bipedHead.rotateAngleX + (float) (Math.PI * 0.1));
                rightShoulder.setRotationY(head.getRotationY());
                rightShoulder.setRotationZ(0);

                rightLowerArm.setRotationX((float) (Math.PI * 0.4));
                rightLowerArm.setRotationY(0);
                rightLowerArm.setRotationZ(0);
            }
        }
        if (ModList.get().isLoaded("firstpersonmod")) {
            //head.setHidden(!FirstPersonModelUtil.shouldRenderHead());
        }
    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, AbstractClientPlayerEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, TransformerGeoLayer layer, ITransformer transformer) {
        //super.render(matrixStackIn, bufferIn, packedLightIn, entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, layer, transformer);
        matrixStackIn.push();
        matrixStackIn.rotate(new Quaternion(0, 0, 180, true));

        matrixStackIn.translate(0, -1.5, 0);
        matrixStackIn.scale(1F,1F,1F);
        layer.setModelProvider(getAnimatedModel());

        AnimatedGeoModel<IAnimatable> animatedGeoModel = layer.getGeoModelProvider();
        GeoModel geoModel = animatedGeoModel.getModel(animatedGeoModel.getModelLocation(transformer));



        if (!Minecraft.getInstance().isGamePaused()) {
            layer.getGeoModelProvider().setLivingAnimations((com.marctron.transformersunlimited.capabilities.Transformer) transformer, layer.getUniqueID(transformer), null);
        }
        GeoBone neck = (GeoBone)geoModel.getBone("Neck").orElse(new GeoBone());
        GeoBone head = (GeoBone)geoModel.getBone("Head").orElse(new GeoBone());
        GeoBone Right_Arm = ((GeoBone)geoModel.getBone("Right_Arm").get());
        GeoBone Right_Lower_Arm = ((GeoBone)geoModel.getBone("Right_Lower_Arm").get());
        GeoBone Left_Arm = ((GeoBone)geoModel.getBone("Left_Arm").get());
        GeoBone Left_Lower_Arm = ((GeoBone)geoModel.getBone("Left_Lower_Arm").get());

        GeoBone Right_Leg = ((GeoBone)geoModel.getBone("Right_Leg").get());
        GeoBone Right_Lower_Leg = ((GeoBone)geoModel.getBone("Right_Lower_Leg").get());
        GeoBone Right_Foot = ((GeoBone)geoModel.getBone("Right_Foot").get());
        GeoBone Left_Foot = ((GeoBone)geoModel.getBone("Left_Foot").get());
        GeoBone Left_Leg = ((GeoBone)geoModel.getBone("Left_Leg").get());
        GeoBone Left_Lower_Leg = ((GeoBone)geoModel.getBone("Left_Lower_Leg").get());

        GeoBone Body = (GeoBone)geoModel.getBone("Waist").get();

        if (ModList.get().isLoaded("firstpersonmod")) {
           //neck.setHidden(!FirstPersonModelUtil.shouldRenderHead());
        }

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



        //Jump and Falling

        if (entitylivingbaseIn instanceof PlayerEntity) {

            double motionY = entitylivingbaseIn.getMotion().y;
            float upwardPose = (float) (1 / (1 + Math.exp(-20 * (motionY + 0.2))));
            float downwardPose = (float) (1 / (1 + Math.exp(10 * (motionY + 0.2))));
            int backwardInverter = 1;

            //System.out.println(motionY);
            //Right_Leg.setRotationX(         Right_Leg.getRotationX()        - 0.2F * (float)downwardPose);
            //Left_Leg.setRotationX(          Left_Leg.getRotationX()         + 0.8F * (float)downwardPose);
            //Right_Lower_Leg.setRotationX(   Right_Lower_Leg.getRotationX()  - 0.3F * (float)downwardPose);
            //Left_Lower_Leg.setRotationX(    Left_Lower_Leg.getRotationX()   - 1.5F * (float)downwardPose);

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
        }

        double mx= entitylivingbaseIn.getPosX() - entitylivingbaseIn.prevPosX;
        double my= entitylivingbaseIn.getPosY() - entitylivingbaseIn.prevPosY;
        double mz= entitylivingbaseIn.getPosZ() - entitylivingbaseIn.prevPosZ;
        double speed =(Math.sqrt(mx*mx+my*my+mz*mz));
        //System.out.println(speed);
        float wheelSpinSpeed = (speed < 10 ? -entitylivingbaseIn.limbSwing : entitylivingbaseIn.limbSwingAmount) * 0.7F;

        GeoBone Right_Front_Wheel = (GeoBone)geoModel.getBone("Right_Front_Wheel").orElse(new GeoBone());
        GeoBone Right_Back_Wheel_1 = (GeoBone)geoModel.getBone("Right_Back_Wheel_1").orElse(new GeoBone());
        GeoBone Right_Back_Wheel_2 = (GeoBone)geoModel.getBone("Right_Back_Wheel_2").orElse(new GeoBone());
        GeoBone Left_Front_Wheel = (GeoBone)geoModel.getBone("Left_Front_Wheel").orElse(new GeoBone());
        GeoBone Left_Back_Wheel_1 = (GeoBone)geoModel.getBone("Left_Back_Wheel_1").orElse(new GeoBone());
        GeoBone Left_Back_Wheel_2 = (GeoBone)geoModel.getBone("Left_Back_Wheel_2").orElse(new GeoBone());

        for (GeoBone wheels : new GeoBone[] {Right_Front_Wheel, Left_Front_Wheel, Right_Back_Wheel_1, Right_Back_Wheel_2, Left_Back_Wheel_1, Left_Back_Wheel_2})
        {
            wheels.setRotationX((float) (-wheelSpinSpeed * 0.5F));
        }
        //for (GeoBone steeringwheels : new GeoBone[] {RightFrontWheel, LeftFrontWheel})
        {
           // steeringwheels.setRotationY(0);
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
            if (bone.getName().equals("Right_Hand")) {
                stack.push();
                stack.translate(1.9D, 3.15D, -0.51D);
                stack.rotate(Vector3f.XP.rotationDegrees(-75.0F));
                if (!((layer.getActualEntity().getPrimaryHand() == HandSide.RIGHT ? layer.getActualEntity().getHeldItemMainhand() : layer.getActualEntity().getHeldItemOffhand()).getItem() instanceof GunItemBase)) {
                    stack.scale(2F, 2F, 2F);
                }

                Minecraft.getInstance().getItemRenderer().renderItem(layer.getActualEntity().getPrimaryHand() == HandSide.RIGHT ? layer.getActualEntity().getHeldItemMainhand() : layer.getActualEntity().getHeldItemOffhand(), ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, packedLightIn, packedOverlayIn, stack, layer.getActualBuffer());
                stack.pop();
                bufferIn = layer.getActualBuffer().getBuffer(layer.getActualRenderType());
            }

            if (bone.getName().equals("Left_Hand")) {
                stack.push();
                stack.translate(-1.9D, 2.875D, -1.15D);
                stack.rotate(Vector3f.XP.rotationDegrees(14.0F));
                if (!((layer.getActualEntity().getPrimaryHand() == HandSide.LEFT ? layer.getActualEntity().getHeldItemMainhand() : layer.getActualEntity().getHeldItemOffhand()).getItem() instanceof GunItemBase)) {
                    stack.scale(2F, 2F, 2F);
                }

                Minecraft.getInstance().getItemRenderer().renderItem(layer.getActualEntity().getPrimaryHand() == HandSide.LEFT ? layer.getActualEntity().getHeldItemMainhand() : layer.getActualEntity().getHeldItemOffhand(), ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, packedLightIn, packedOverlayIn, stack, layer.getActualBuffer());
                stack.pop();
                bufferIn = layer.getActualBuffer().getBuffer(layer.getActualRenderType());
            }

           if (bone.getName().equals("camera")) {
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
