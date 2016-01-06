package com.cosmicdan.enhancedocclusionculling.core.transformers;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import net.minecraft.launchwrapper.IClassTransformer;

public class WorldTransformer extends TransformerHookMethodStart{
    @Override
    public String setTargetClassDev() {
        return "net.minecraft.world.World";
    }

    @Override
    public String setTargetClassObf() {
        return "ahb";
    }

    @Override
    public String setTargetMethodDev() {
        return "spawnParticle";
    }

    @Override
    public String setTargetMethodObf() {
        return "func_72869_a";
    }

    @Override
    public String setTargetDesc() {
        return "(Ljava/lang/String;DDDDDD)V";
    }

    @Override
    public InsnList injectOps(InsnList toInject) {
        LabelNode labelTrue = new LabelNode();
        toInject.add(new VarInsnNode(Opcodes.ALOAD, 1));
        toInject.add(new VarInsnNode(Opcodes.DLOAD, 2));
        toInject.add(new VarInsnNode(Opcodes.DLOAD, 4)); // doubles consume two frames
        toInject.add(new VarInsnNode(Opcodes.DLOAD, 6));
        toInject.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/cosmicdan/enhancedocclusionculling/trackers/ItemTracker", "shouldRenderItem", "(Ljava/lang/String;DDD)Z", false));
        toInject.add(new JumpInsnNode(Opcodes.IFNE, labelTrue)); // if result is true, go to labelTrue
        toInject.add(new InsnNode(Opcodes.RETURN)); // not true, so return (i.e. don't render)
        toInject.add(labelTrue); // is true, so continue with the rest of method
        return(toInject);
    }
}
