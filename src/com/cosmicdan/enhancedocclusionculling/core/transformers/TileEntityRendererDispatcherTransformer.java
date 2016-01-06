package com.cosmicdan.enhancedocclusionculling.core.transformers;

import java.util.Iterator;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cosmicdan.enhancedocclusionculling.Main;

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.tileentity.TileEntity;

public class TileEntityRendererDispatcherTransformer extends TransformerHookMethodStart {

    @Override
    public String setTargetClassDev() {
        return "net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher";
    }

    @Override
    public String setTargetClassObf() {
        return "bmk";
    }

    @Override
    public String setTargetMethodDev() {
        return "renderTileEntityAt";
    }

    @Override
    public String setTargetMethodObf() {
        return "func_147549_a";
    }

    @Override
    public String setTargetDesc() {
        return "(Lnet/minecraft/tileentity/TileEntity;DDDF)V";
    }

    @Override
    public InsnList injectOps(InsnList toInject) {
        LabelNode labelTrue = new LabelNode();
        toInject.add(new VarInsnNode(Opcodes.ALOAD, 1)); // push first parameter (TileEntity p_147549_1_) onto stack
        toInject.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/cosmicdan/enhancedocclusionculling/trackers/ItemTracker", "shouldRenderTe", "(Lnet/minecraft/tileentity/TileEntity;)Z", false));
        toInject.add(new JumpInsnNode(Opcodes.IFNE, labelTrue)); // if result is true, go to labelTrue
        toInject.add(new InsnNode(Opcodes.RETURN)); // not true, so return (i.e. don't render)
        toInject.add(labelTrue); // is true, so continue with the rest of method
        return(toInject);
    }
}
