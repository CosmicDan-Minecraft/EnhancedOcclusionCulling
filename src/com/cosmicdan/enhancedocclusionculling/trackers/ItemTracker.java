package com.cosmicdan.enhancedocclusionculling.trackers;

import java.util.concurrent.ConcurrentHashMap;

import com.cosmicdan.enhancedocclusionculling.ModConfig;
import com.cosmicdan.enhancedocclusionculling.trackers.ItemRecord.TYPE;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.culling.Frustrum;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;

public class ItemTracker {
    
    public static ConcurrentHashMap<String, ItemRecord> TRACKED_ITEMS;
    
    public static void init() {
        TRACKED_ITEMS = new ConcurrentHashMap<String, ItemRecord>(ModConfig.WORKER_INITIAL_SIZE, (float) ModConfig.WORKER_LOAD_FACTOR);
    }

    public static boolean shouldRenderTe(TileEntity te) {
        // If the TE's co-ords are 0,0,0 then ALWAYS render it (cheap check for a non-worldspace TESR render)
        // Has the side-effect of always rendering a TESR if it's at 0,0,0 obviously, but meh.
        if ((te.xCoord == 0) && (te.yCoord == 0) && (te.zCoord == 0)) return true;
        
        String teKey = te.getBlockType().getUnlocalizedName() + "///" +  te.xCoord  + "///" + te.yCoord  + "///" + te.zCoord;
        
        if (!TRACKED_ITEMS.containsKey(teKey)) {
            TRACKED_ITEMS.put(teKey, new ItemRecord(te.getRenderBoundingBox(), TYPE.TILEENTITY, (!ModConfig.OBJECTS_INITIALLY_HIDDEN)));
        }
        
        return TRACKED_ITEMS.get(teKey).getShouldRender();
    }
    
    public static boolean shouldRenderItem(String name, double x, double y, double z) {
        if (ModConfig.DO_FX) {
            String key = name + "///" +  x  + "///" + y  + "///" + z;
            
            if (!TRACKED_ITEMS.containsKey(key)) {
                TRACKED_ITEMS.put(key, new ItemRecord(AxisAlignedBB.getBoundingBox(x, y, z, x, y, z), TYPE.PARTICLE, (!ModConfig.OBJECTS_INITIALLY_HIDDEN)));
            }
            
            return TRACKED_ITEMS.get(key).getShouldRender();
        } else
            return true;
    }
}
