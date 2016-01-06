package com.cosmicdan.enhancedocclusionculling;

import java.util.Map;

import net.minecraftforge.common.MinecraftForge;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cosmicdan.enhancedocclusionculling.rendertrackers.TileEntityTracker;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

@Mod(
    modid = Main.MODID, 
    name = Main.MODNAME,
    version = "${version}",
    dependencies = "required-after:Forge@[10.13,)"
)

public class Main {
    public static final String MODID = "enhancedocclusionculling";
    public static final String MODNAME = "Enhanced Occlusion Culling";
    
    public static final Logger LOGGER = LogManager.getLogger(Main.MODNAME);
    
    @Instance(MODNAME)
    public static Main INSTANCE;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ModConfig.init(event.getSuggestedConfigurationFile());
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new RenderWorker());
        MinecraftForge.EVENT_BUS.register(new DebugInfo());
        TileEntityTracker.init();
    }
    
}
