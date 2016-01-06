package com.cosmicdan.enhancedocclusionculling;

import java.io.File;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class ModConfig {

    public static int FRAMES_BEFORE_CAMERA_UPDATE = 2;
    public static int FRAMES_BEFORE_UPDATE = 2;
    public static int FRAMES_BEFORE_PURGE = 1000;
    public static int WORKER_INITIAL_SIZE = 100;
    public static double WORKER_LOAD_FACTOR = 0.5;
    public static boolean OBJECTS_INITIALLY_HIDDEN = true;
    
    private static Configuration CONFIG;
    
    public static void init(File configFile) {
        CONFIG = new Configuration(configFile);
        CONFIG.load();
        
        FRAMES_BEFORE_CAMERA_UPDATE = loadInt("FRAMES_BEFORE_CAMERA_UPDATE",
                "How many frames before the player camera (viewport) is updated.\n" +
                "Default: " + FRAMES_BEFORE_CAMERA_UPDATE + "\n" +
                "This determines the rate that the player 'camera' position is updated, used in calculation for raytracing." +
                "Must be 1 or greater.",
                FRAMES_BEFORE_CAMERA_UPDATE);
        
        FRAMES_BEFORE_UPDATE = loadInt("FRAMES_BEFORE_UPDATE",
                "How many frames before each tracked object is checked for viewport occlusion, where 0 is every frame.\n" +
                "Default: " + FRAMES_BEFORE_UPDATE + "\n" +
                "Note that setting this to zero will result in NO updates, 1 is every frame, 2 is every second frame, etc.\n" + 
                "Values higher than 1 (every frame) *may* cause a split-second pop-in in low FPS situations. Must be 1 or greater.\n" +
                "If you want to tune your performance, increasing this value will help.",
                FRAMES_BEFORE_UPDATE);
        
        FRAMES_BEFORE_PURGE = loadInt("FRAMES_BEFORE_PURGE",
                "How many frames before hidden objects are considered 'stale' and purged from the tracker.\n" +
                "Default: " + FRAMES_BEFORE_PURGE + "\n" + 
                "Whenever an object is hidden or unhidden, the 'age' is reset - but if an object is hidden for this number of \n" + 
                "frames, it will be purged from the queue. Lower numbers will reduce memory use at the cost of higher CPU work.",
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
        
        OBJECTS_INITIALLY_HIDDEN = loadBool("OBJECTS_INITIALLY_HIDDEN",
                "Newly-tracked objects hidden?\n" +
                "Default: " + OBJECTS_INITIALLY_HIDDEN + "\n" +
                "If true, then objects which are freshly added (including after a purge) will be hidden. \n" +
                "This will be better performance, HOWEVER if the object is already within-view then there may be \n" +
                "visible pop-in, depending on how high the FRAMES_BEFORE_UPDATE setting is, and your overall FPS (in low-FPS\n" +
                "situations, pop-in is more likely to be noticed).",
                OBJECTS_INITIALLY_HIDDEN);
        
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
    
    public static boolean loadBool(String name, String desc, boolean def) {
        Property prop = CONFIG.get(Configuration.CATEGORY_GENERAL, name, def);
        prop.comment = desc;
        return prop.getBoolean(def);
    }
}
