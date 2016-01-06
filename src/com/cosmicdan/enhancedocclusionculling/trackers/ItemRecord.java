package com.cosmicdan.enhancedocclusionculling.trackers;

import net.minecraft.util.AxisAlignedBB;

public class ItemRecord {
    private final AxisAlignedBB boundingBox;
    private boolean shouldRender;
    private int lastUpdate;
    private int age;
    private TYPE itemType;
    
    public static enum TYPE {
        TILEENTITY,
        PARTICLE
    }
    
    public ItemRecord(AxisAlignedBB boundingBox, TYPE itemType, boolean shouldRender) {
        this.boundingBox = boundingBox;
        this.shouldRender = shouldRender;
        this.itemType = itemType;
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
    
    public TYPE getType() {
        return itemType;
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
