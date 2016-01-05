package com.cosmicdan.enhancedocclusionculling.renderhandlers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.culling.Frustrum;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3;

public class TileEntities {

    public static boolean shouldRender(TileEntity te) {
        // if the TE's co-ords are 0,0,0 then ALWAYS return true (cheap check for a non-worldspace TESR render)
        if ((te.xCoord == 0) && (te.yCoord == 0) && (te.zCoord == 0)) return true;
        
        EntityLivingBase camera = Minecraft.getMinecraft().renderViewEntity;
        
        float partialTicks = 1.0F; // we won't worry about a higher resolution for now. In future, I may try to use actual partialTicks from e.g. RenderWorldLastEvent event
        Vec3 cameraVector = camera.getPosition(partialTicks);
        Frustrum frustrum = new Frustrum();
        double viewX = camera.lastTickPosX + (camera.posX - camera.lastTickPosX) * partialTicks;
        double viewY = camera.lastTickPosY + (camera.posY - camera.lastTickPosY) * partialTicks;
        double viewZ = camera.lastTickPosZ + (camera.posZ - camera.lastTickPosZ) * partialTicks;
        frustrum.setPosition(viewX, viewY, viewZ);
        
        if (frustrum.isBoundingBoxInFrustum(te.getRenderBoundingBox()))
            return true;
        else
            return false;

    }
}
