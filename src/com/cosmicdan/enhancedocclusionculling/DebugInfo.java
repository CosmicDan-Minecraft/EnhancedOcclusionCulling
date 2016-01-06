package com.cosmicdan.enhancedocclusionculling;

import java.util.Map;

import com.cosmicdan.enhancedocclusionculling.trackers.ItemRecord;
import com.cosmicdan.enhancedocclusionculling.trackers.ItemRecord.TYPE;
import com.cosmicdan.enhancedocclusionculling.trackers.ItemTracker;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

public class DebugInfo {
    
    private int updateCount = 0; // we'll update debug screen values every few frames
    private int teCount;
    private int fxCount;
    
    @SubscribeEvent
    public void RenderGameOverlayEvent(RenderGameOverlayEvent event) {
        if(event.type == RenderGameOverlayEvent.ElementType.TEXT) {
            if (Minecraft.getMinecraft().gameSettings.showDebugInfo) {
                ScaledResolution scaledresolution = new ScaledResolution(Minecraft.getMinecraft(), Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
                int x = scaledresolution.getScaledWidth();
                int y = scaledresolution.getScaledHeight();
                
                if (updateCount == 10) {
                    updateCount = 0; 
                    teCount = 0;
                    fxCount = 0;
                    for (Map.Entry<String, ItemRecord> itemRecordEntry : ItemTracker.TRACKED_ITEMS.entrySet()) {
                        if (itemRecordEntry.getValue().getType() == TYPE.TILEENTITY)
                            teCount++;
                        if (itemRecordEntry.getValue().getType() == TYPE.PARTICLE)
                            fxCount++;
                    }
                }
                updateCount++;
                
                String info = "ECO Tracking: " + teCount + " TE's; " + fxCount + " FX's";
                FontRenderer fr = Minecraft.getMinecraft().fontRenderer;
                Minecraft.getMinecraft().fontRenderer.drawString(info, x - fr.getStringWidth(info) - 10, 22, 16777215, true);
            }
        }
    }
}
