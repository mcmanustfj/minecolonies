package com.minecolonies.coremod.client.model;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class ModelEntityComposterMale extends BipedModel
{
    //fields
    RendererModel gloveR;
    RendererModel gloveL;
    RendererModel bootR;
    RendererModel bootL;

    public ModelEntityComposterMale()
    {
        textureWidth = 128;
        textureHeight = 64;

        gloveR = new RendererModel(this, 20, 32);
        gloveR.addBox(1.5F, 3F, -2.5F, 5, 1, 5);
        gloveR.setRotationPoint(-5F, 2F, 0F);
        gloveR.setTextureSize(128, 64);
        gloveR.mirror = true;
        setRotation(gloveR, 0F, 0F, 0F);

        gloveL = new RendererModel(this, 0, 32);
        gloveL.addBox(-6.5F, 3F, -2.5F, 5, 1, 5);
        gloveL.setRotationPoint(5F, 2F, 0F);
        gloveL.setTextureSize(128, 64);
        gloveL.mirror = true;
        setRotation(gloveL, 0F, 0F, 0F);
        gloveL.mirror = false;

        bootR = new RendererModel(this, 20, 38);
        bootR.addBox(-0.5F, -8F, -2.5F, 5, 2, 5);
        bootR.setRotationPoint(-2F, 12F, 0F);
        bootR.setTextureSize(128, 64);
        bootR.mirror = true;
        setRotation(bootR, 0F, 0F, 0F);

        bootL = new RendererModel(this, 0, 38);
        bootL.addBox(-4.5F, -8F, -2.5F, 5, 2, 5);
        bootL.setRotationPoint(2F, 12F, 0F);
        bootL.setTextureSize(128, 64);
        bootL.mirror = true;
        setRotation(bootL, 0F, 0F, 0F);

        bipedLeftLeg = new RendererModel(this, 0, 16);
        bipedLeftLeg.addBox(-2F, 0F, -2F, 4, 12, 4);
        bipedLeftLeg.setRotationPoint(2F, 12F, 0F);
        bipedLeftLeg.setTextureSize(128, 64);
        bipedLeftLeg.mirror = true;
        setRotation(bipedLeftLeg, 0F, 0F, 0F);
        bipedLeftLeg.mirror = false;

        bipedRightLeg = new RendererModel(this, 0, 16);
        bipedRightLeg.addBox(-2F, 0F, -2F, 4, 12, 4);
        bipedRightLeg.setRotationPoint(-2F, 12F, 0F);
        bipedRightLeg.setTextureSize(128, 64);
        bipedRightLeg.mirror = true;
        setRotation(bipedRightLeg, 0F, 0F, 0F);

        bipedLeftArm = new RendererModel(this, 40, 16);
        bipedLeftArm.addBox(-1F, -2F, -2F, 4, 12, 4);
        bipedLeftArm.setRotationPoint(5F, 2F, 0F);
        bipedLeftArm.setTextureSize(128, 64);
        bipedLeftArm.mirror = true;
        setRotation(bipedLeftArm, 0F, 0F, 0F);

        bipedRightArm = new RendererModel(this, 40, 16);
        bipedRightArm.addBox(-3F, -2F, -2F, 4, 12, 4);
        bipedRightArm.setRotationPoint(-5F, 2F, 0F);
        bipedRightArm.setTextureSize(128, 64);
        bipedRightArm.mirror = true;
        setRotation(bipedRightArm, 0F, 0F, 0F);

        bipedHead = new RendererModel(this, 0, 0);
        bipedHead.addBox(-4F, -8F, -4F, 8, 8, 8);
        bipedHead.setRotationPoint(0F, 0F, 0F);
        bipedHead.setTextureSize(128, 64);
        bipedHead.mirror = true;
        setRotation(bipedHead, 0F, 0F, 0F);

        bipedBody = new RendererModel(this, 16, 16);
        bipedBody.addBox(-4F, 0F, -2F, 8, 12, 4);
        bipedBody.setRotationPoint(0F, 0F, 0F);
        bipedBody.setTextureSize(128, 64);
        bipedBody.mirror = true;
        setRotation(bipedBody, 0F, 0F, 0F);

        bipedRightArm.addChild(gloveR);
        bipedLeftArm.addChild(gloveL);

        bipedRightLeg.addChild(bootR);
        bipedLeftLeg.addChild(bootL);
    }

    @Override
    public void render(
      @NotNull final LivingEntity entity,
      final float limbSwing,
      final float limbSwingAmount,
      final float ageInTicks,
      final float netHeadYaw,
      final float headPitch,
      final float scaleFactor)
    {
        super.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
        setRotationAngles(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
    }

    private void setRotation(@NotNull final RendererModel model, final float x, final float y, final float z)
    {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }
}
