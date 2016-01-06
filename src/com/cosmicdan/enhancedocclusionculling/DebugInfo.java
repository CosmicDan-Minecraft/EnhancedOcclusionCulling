package com.cosmicdan.enhancedocclusionculling;

import com.cosmicdan.enhancedocclusionculling.rendertrackers.TileEntityTracker;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

public class DebugInfo {
    @SubscribeEvent
    public void RenderGameOverlayEvent(RenderGameOverlayEvent event) {
        if(event.type == RenderGameOverlayEvent.ElementType.TEXT) {
            if (Minecraft.getMinecraft().gameSettings.showDebugInfo) {
                ScaledResolution scaledresolution = new ScaledResolution(Minecraft.getMinecraft(), Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
                int x = scaledresolution.getScaledWidth();
                int y = scaledresolution.getScaledHeight();
                String info = "ECO: Tracking " + TileEntityTracker.TRACKED_ITEMS.size() + " objects";
                FontRenderer fr = Minecraft.getMinecraft().fontRenderer;
                Minecraft.getMinecraft().fontRenderer.drawString(info, x - fr.getStringWidth(info) - 10, 22, 16777215, true);
            }
        }
    }
}
