package com.cosmicdan.enhancedocclusionculling.core.transformers;

import java.util.Iterator;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodNode;

import com.cosmicdan.enhancedocclusionculling.Main;

import net.minecraft.launchwrapper.IClassTransformer;

public abstract class TransformerHookMethodStart implements IClassTransformer  {
    String targetClassDev;
    String targetClassObf;
    String targetMethodDev;
    String targetMethodObf;
    String targetDesc;

    public TransformerHookMethodStart() {
        targetClassDev = setTargetClassDev();
        targetClassObf = setTargetClassObf();
        targetMethodDev = setTargetMethodDev();
        targetMethodObf = setTargetMethodObf();
        targetDesc = setTargetDesc();
    }

    public abstract String setTargetClassDev();
    public abstract String setTargetClassObf();
    public abstract String setTargetMethodDev();
    public abstract String setTargetMethodObf();
    public abstract String setTargetDesc();
    
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
                InsnList toInject = injectOps(new InsnList());
                m.instructions.insertBefore(ain, toInject);
                Main.LOGGER.info("[i] Patched " + targetClassDev + "." + targetMethodDev);
                break;
            }
        }
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        classNode.accept(writer);
        return writer.toByteArray();
    }
    
    public abstract InsnList injectOps(InsnList toInject);
}