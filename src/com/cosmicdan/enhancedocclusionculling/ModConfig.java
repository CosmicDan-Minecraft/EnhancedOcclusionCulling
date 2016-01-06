package com.cosmicdan.enhancedocclusionculling;

import java.io.File;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class ModConfig {

    public static int FRAMES_BEFORE_UPDATE = 1;
    public static int FRAMES_BEFORE_PURGE = 100;
    public static int WORKER_INITIAL_SIZE = 100;
    public static double WORKER_LOAD_FACTOR = 0.5;
    
    private static Configuration CONFIG;
    
    public static void init(File configFile) {
        CONFIG = new Configuration(configFile);
        CONFIG.load();
        
        FRAMES_BEFORE_UPDATE = loadInt("FRAMES_BEFORE_UPDATE",
                "How many frames before each tracked object is checked for viewport occlusion, where 0 is every frame.\n" +
                "Default: " + FRAMES_BEFORE_UPDATE + "\n" +
                "The default is usually good enough, but if you have inconsistent FPS and/or a lot of objects in your area\n" + 
                "then you *may* notice split-second pop-in. 0 is *every* frame however, which may result in worse FPS.",
                FRAMES_BEFORE_UPDATE);
        
        FRAMES_BEFORE_PURGE = loadInt("FRAMES_BEFORE_PURGE",
                "How many frames before hidden objects are considered 'stale' and purged from the tracker.\n" +
                "Default: + " + FRAMES_BEFORE_PURGE + "\n" + 
                "Whenever an object is hidden or unhidden, the 'age' is reset - but if an object is hidden for this number of \n" + 
                "frames, it will be purged from the queue. Lower numbers will reduce memory at the cost of higher CPU work.",
                FRAMES_BEFORE_PURGE);
        
        WORKER_INITIAL_SIZE = loadInt("WORKER_INITIAL_SIZE",
                "Initial size of the worker queue\n" +
                "Default: " + WORKER_INITIAL_SIZE + "\n" +
                "Mostly for advanced tuning (not too important). The initial size of the HashMap-backed item tracker.",
                WORKER_INITIAL_SIZE);
        
        WORKER_LOAD_FACTOR = loadDouble("WORKER_LOAD_FACTOR",
                "Load factor of the worker queue\n" +
                "Default: " + WORKER_LOAD_FACTOR + "\n" +
                "Again, mostly for advanced tuning. Load factor of the HashMap-backed item tracker. \n" +
                "Higher value = Potentially lower RAM consumption at the potential expense of higher CPU work. \n" +
                "Must be higher than 0 and lower than 1.0 (but 1.0 would cause MAJOR performance issues).",
                WORKER_LOAD_FACTOR);
        
        if(CONFIG.hasChanged())
            CONFIG.save();
    }
    
    public static int loadInt(String name, String desc, int def) {
        Property prop = CONFIG.get(Configuration.CATEGORY_GENERAL, name, def);
        prop.comment = desc;
        return prop.getInt(def);
    }
    
    public static double loadDouble(String name, String desc, double def) {
        Property prop = CONFIG.get(Configuration.CATEGORY_GENERAL, name, def);
        prop.comment = desc;
        return prop.getDouble(def);
    }
}
