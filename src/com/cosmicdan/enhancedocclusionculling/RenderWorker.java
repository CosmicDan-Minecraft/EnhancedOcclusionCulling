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
    
    private static BlockingQueue<TileEntityRecordTask> RENDER_WORKER_QUEUE = new LinkedBlockingQueue<TileEntityRecordTask>();
    private static Thread RENDER_WORKER_THREAD;
    private static int MAX_AGE_BEFORE_UPDATE = 2;
    private static int MAX_AGE_BEFORE_PURGE = 1000;
    
    // used in worker thread
    protected TileEntityRecordTask recordTask;
    protected EntityLivingBase camera;
    protected Vec3 cameraVector;
    protected Frustrum frustrum = new Frustrum();
    
    public RenderWorker() {
        RENDER_WORKER_THREAD = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        recordTask = RENDER_WORKER_QUEUE.take();
                        camera = Minecraft.getMinecraft().renderViewEntity;
                        cameraVector = camera.getPosition(recordTask.PARTIAL_TICKS);
                        double viewX = camera.lastTickPosX + (camera.posX - camera.lastTickPosX) * recordTask.PARTIAL_TICKS;
                        double viewY = camera.lastTickPosY + (camera.posY - camera.lastTickPosY) * recordTask.PARTIAL_TICKS;
                        double viewZ = camera.lastTickPosZ + (camera.posZ - camera.lastTickPosZ) * recordTask.PARTIAL_TICKS;
                        frustrum.setPosition(viewX, viewY, viewZ);
                        recordTask.TILE_ENTITY_RECORD.setShouldRender(frustrum.isBoundingBoxInFrustum(recordTask.TILE_ENTITY_RECORD.getBoundingBox()));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        
        RENDER_WORKER_THREAD.start();
    }
    
    public void addEntry() {
        
    }
    
    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        for (Map.Entry<String, TileEntityRecord> tileEntityTrack : TileEntityTracker.TRACKED_ITEMS.entrySet()) {
            if ((tileEntityTrack.getValue().getAge() == MAX_AGE_BEFORE_PURGE) && (!tileEntityTrack.getValue().getShouldRender())) {
                // entry expired, purge it
                TileEntityTracker.TRACKED_ITEMS.remove(tileEntityTrack.getKey());
            }
            else if (tileEntityTrack.getValue().getLastUpdate() == MAX_AGE_BEFORE_UPDATE) {
                tileEntityTrack.getValue().resetLastUpdate();
                RENDER_WORKER_QUEUE.offer(new TileEntityRecordTask(event.partialTicks, tileEntityTrack.getValue()));
            }
            tileEntityTrack.getValue().addAge();
        }
    }
    
    class TileEntityRecordTask {
        final float PARTIAL_TICKS;
        final TileEntityRecord TILE_ENTITY_RECORD;
        
        public TileEntityRecordTask(float partialTicks, TileEntityRecord tileEntityRecord) {
            this.PARTIAL_TICKS = partialTicks;
            this.TILE_ENTITY_RECORD = tileEntityRecord;
        }
    }
}
