package com.cosmicdan.enhancedocclusionculling.rendertrackers;

import java.util.concurrent.ConcurrentHashMap;

import com.cosmicdan.enhancedocclusionculling.ModConfig;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.culling.Frustrum;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;

public class TileEntityTracker {
    
    public static ConcurrentHashMap<String, TileEntityRecord> TRACKED_ITEMS;
    
    public static void init() {
        TRACKED_ITEMS = new ConcurrentHashMap<String, TileEntityRecord>(ModConfig.WORKER_INITIAL_SIZE, (float) ModConfig.WORKER_LOAD_FACTOR);
    }

    public static boolean shouldRender(TileEntity te) {
        // If the TE's co-ords are 0,0,0 then ALWAYS render it (cheap check for a non-worldspace TESR render)
        // Has the side-effect of always rendering a TESR if it's at 0,0,0 obviously, but meh.
        if ((te.xCoord == 0) && (te.yCoord == 0) && (te.zCoord == 0)) return true;
        
        
        
        String teKey = te.getBlockType().getUnlocalizedName() + "///" +  te.xCoord  + "///" + te.yCoord  + "///" + te.zCoord;
        
        if (!TRACKED_ITEMS.containsKey(teKey)) {
            TRACKED_ITEMS.put(teKey, new TileEntityRecord(te.getRenderBoundingBox(), (!ModConfig.OBJECTS_INITIALLY_HIDDEN)));
        }
        
        return TRACKED_ITEMS.get(teKey).getShouldRender();
    }
}
