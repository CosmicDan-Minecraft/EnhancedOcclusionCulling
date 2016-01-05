package com.cosmicdan.enhancedocclusionculling.rendertrackers;

import java.util.HashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.culling.Frustrum;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;

public class TileEntityTracker {
    
    public static HashMap<String, TileEntityRecord> TRACKED_ITEMS;
    
    public static void init() {
        TRACKED_ITEMS = new HashMap<String, TileEntityRecord>();
    }

    public static boolean shouldRender(TileEntity te) {
        // If the TE's co-ords are 0,0,0 then ALWAYS render it (cheap check for a non-worldspace TESR render)
        // Has the side-effect of always rendering a TESR if it's at 0,0,0 obviously, but meh.
        if ((te.xCoord == 0) && (te.yCoord == 0) && (te.zCoord == 0)) return true;
        
        String teKey = te.getBlockType().getUnlocalizedName() + "///" +  te.xCoord  + "///" + te.yCoord  + "///" + te.zCoord;
        
        // add new tracked items with shouldRender=false as default (RenderWorldLastWorker takes over from here)
        if (!TRACKED_ITEMS.containsKey(teKey)) {
            TRACKED_ITEMS.put(teKey, new TileEntityRecord(te.getRenderBoundingBox(), false));
        }
        
        return TRACKED_ITEMS.get(teKey).getShouldRender();
        
        /*
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
        */
    }
    
    
}
