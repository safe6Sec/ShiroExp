package cn.safe6.payload.shortPayload.asm;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import java.nio.file.Files;
import java.nio.file.Paths;


public class Resolver {
    public static void resolve(String path) {
        try {
            byte[] bytes = Files.readAllBytes(Paths.get(path));
            ClassReader cr = new ClassReader(bytes);
            ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
            int api = Opcodes.ASM9;
            ClassVisitor cv = new ShortClassVisitor(api, cw);
            int parsingOptions = ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES;
            cr.accept(cv, parsingOptions);
            byte[] out = cw.toByteArray();
            Files.write(Paths.get(path), out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
