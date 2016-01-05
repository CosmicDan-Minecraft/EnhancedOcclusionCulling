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

public class TileEntityRendererDispatcherTransformer implements IClassTransformer {
    
    String targetClassDev = "net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher";
    String targetClassObf = "bmk";
    String targetMethodDev = "renderTileEntityAt";
    String targetMethodObf = "func_147549_a";
    String targetDesc = "(Lnet/minecraft/tileentity/TileEntity;DDDF)V";
    String callbackDesc = "(Lnet/minecraft/tileentity/TileEntity;)Z";
    
    @Override
    public byte[] transform(String name, String transformedName, byte[] classBytes) {
        if (name.equals(targetClassDev)) // dev environment
            return patchClass(classBytes, true);
        if (name.equals(targetClassObf)) // obf environment
            return patchClass(classBytes, false);
        return classBytes;
    }
    
    private byte[] patchClass(byte[] classBytes, boolean dev) {
        String targetMethod = dev ? targetMethodDev : targetMethodObf;

        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(classBytes);
        classReader.accept(classNode, 0);

        Iterator<MethodNode> methods = classNode.methods.iterator();
        while(methods.hasNext()) {
            MethodNode m = methods.next();
            int fdiv_index = -1;
            if ((m.name.equals(targetMethod) && m.desc.equals(targetDesc))) {
                AbstractInsnNode currentNode = null;
                AbstractInsnNode targetNode = null;

                AbstractInsnNode ain = m.instructions.getFirst();
                LabelNode labelTrue = new LabelNode();
                //LabelNode labelFalse = new LabelNode();
                InsnList toInject = new InsnList();
                toInject.add(new VarInsnNode(Opcodes.ALOAD, 1)); // push first parameter (TileEntity p_147549_1_) onto stack
                
                toInject.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/cosmicdan/entityrendertweaks/TileEntityRendererHook", "shouldRenderTileEntityAt", callbackDesc, false));
                toInject.add(new JumpInsnNode(Opcodes.IFNE, labelTrue)); // if result is true, go to labelTrue
                toInject.add(new InsnNode(Opcodes.RETURN)); // not true, so return
                toInject.add(labelTrue); // is true, so continue with the rest of method
                m.instructions.insertBefore(ain, toInject);
                Main.LOGGER.info("[i] Patched net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher#renderTileEntityAt");
                break;
            }
        }
    ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
    classNode.accept(writer);
    return writer.toByteArray();
    }
}
