package com.cosmicdan.enhancedocclusionculling.core;

import java.util.Map;

import com.cosmicdan.enhancedocclusionculling.core.transformers.*;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

@IFMLLoadingPlugin.Name(value = "Entity Render Tweaks Core")
@IFMLLoadingPlugin.MCVersion(value = "1.7.10")
@IFMLLoadingPlugin.TransformerExclusions(value = "com.cosmicdan.enhancedocclusionculling.")
@IFMLLoadingPlugin.SortingIndex(value = 1001)

public class CorePlugin implements IFMLLoadingPlugin {
    @Override
    public String[] getASMTransformerClass() {
        return new String[]{
                TileEntityRendererDispatcherTransformer.class.getName(),
                WorldTransformer.class.getName()
        };
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
