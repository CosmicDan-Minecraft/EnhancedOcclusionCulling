package com.cosmicdan.enhancedocclusionculling;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.cosmicdan.enhancedocclusionculling.rendertrackers.TileEntityRecord;
import com.cosmicdan.enhancedocclusionculling.rendertrackers.TileEntityTracker;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.culling.Frustrum;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class RenderWorker {
    
    private static final BlockingQueue<TileEntityRecord> RENDER_WORKER_QUEUE = new LinkedBlockingQueue<TileEntityRecord>();
    private static Thread RENDER_WORKER_THREAD;
    private static final int MAX_AGE_BEFORE_UPDATE = ModConfig.FRAMES_BEFORE_UPDATE;
    private static final int MAX_AGE_BEFORE_PURGE = ModConfig.FRAMES_BEFORE_PURGE;
    private static final int CAMERA_UPDATE_RATE = ModConfig.FRAMES_BEFORE_CAMERA_UPDATE;
    
    private static int CAMERA_UPDATE_COUNTER = 0;
    
    // used in worker thread
    protected TileEntityRecord record;
    protected EntityLivingBase camera;
    protected Vec3 cameraVector;
    protected Frustrum frustrum = new Frustrum();
    
    public RenderWorker() {
        RENDER_WORKER_THREAD = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        record = RENDER_WORKER_QUEUE.take();
                        record.setShouldRender(frustrum.isBoundingBoxInFrustum(record.getBoundingBox()));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        RENDER_WORKER_THREAD.start();
    }
    
    
    
    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        if (CAMERA_UPDATE_COUNTER == CAMERA_UPDATE_RATE) {
            camera = Minecraft.getMinecraft().renderViewEntity;
            cameraVector = camera.getPosition(event.partialTicks);
            double viewX = camera.lastTickPosX + (camera.posX - camera.lastTickPosX) * event.partialTicks;
            double viewY = camera.lastTickPosY + (camera.posY - camera.lastTickPosY) * event.partialTicks;
            double viewZ = camera.lastTickPosZ + (camera.posZ - camera.lastTickPosZ) * event.partialTicks;
            frustrum.setPosition(viewX, viewY, viewZ);
            CAMERA_UPDATE_COUNTER = 0;
        }
        CAMERA_UPDATE_COUNTER++;
        
        for (Map.Entry<String, TileEntityRecord> tileEntityTrack : TileEntityTracker.TRACKED_ITEMS.entrySet()) {
            if ((tileEntityTrack.getValue().getAge() == MAX_AGE_BEFORE_PURGE) && (!tileEntityTrack.getValue().getShouldRender())) {
                // entry expired, purge it
                TileEntityTracker.TRACKED_ITEMS.remove(tileEntityTrack.getKey());
            }
            else if (tileEntityTrack.getValue().getLastUpdate() == MAX_AGE_BEFORE_UPDATE) {
                tileEntityTrack.getValue().resetLastUpdate();
                RENDER_WORKER_QUEUE.offer(tileEntityTrack.getValue());
            }
            tileEntityTrack.getValue().addAge();
        }
    }
}
