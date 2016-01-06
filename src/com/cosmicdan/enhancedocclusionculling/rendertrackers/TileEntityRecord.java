package com.cosmicdan.enhancedocclusionculling.rendertrackers;

import net.minecraft.util.AxisAlignedBB;

public class TileEntityRecord {
    private final AxisAlignedBB boundingBox;
    private boolean shouldRender;
    private int lastUpdate;
    private int age;
    
    public TileEntityRecord(AxisAlignedBB boundingBox, boolean shouldRender) {
        this.boundingBox = boundingBox;
        this.shouldRender = shouldRender;
        this.lastUpdate = 0;
    }
    
    public void addAge() {
        lastUpdate++;
        age++;
    }
    
    public void resetLastUpdate() {
        this.lastUpdate = 0;
    }
    
    public int getLastUpdate() {
        return lastUpdate;
    }
    
    public int getAge() {
        return age;
    }
    
    public AxisAlignedBB getBoundingBox() {
        return boundingBox;
    }
    
    public void setShouldRender(boolean shouldRender) {
        if (shouldRender != this.shouldRender) {
            this.shouldRender = shouldRender;
            this.age = 0;
        }
    }
    
    public boolean getShouldRender() {
        return shouldRender;
    }
}
