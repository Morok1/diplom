package com.smagin.valuetypeanalyzer.valuetypeanalyzer.bytecodeexchanger;


import org.objectweb.asm.*;
import typefier.ValueCapableClass;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.*;
import java.util.function.Function;

import static org.objectweb.asm.Opcodes.ACC_PROTECTED;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;

class ValueTypeAnnotationUtil {
  static class Info {
    final boolean valueCapable;
    final Set<String> internals;

    Info(boolean valueCapable, Set<String> internals) {
      this.valueCapable = valueCapable;
      this.internals = internals;
    }
  }

  static String mangle(String method, String descriptor) {
    return method + descriptor;
  }

  static final String VALUE_CAPABLE_CLASS_NAME = 'L' + ValueCapableClass.class.getName().replace('.', '/') + ';';

  private final HashMap<String, Info> cache = new HashMap<>();
  private final Function<String, Optional<InputStream>> classFileFinder;

  public ValueTypeAnnotationUtil(Function<String, Optional<InputStream>> classFileFinder) {
    this.classFileFinder = Objects.requireNonNull(classFileFinder);
  }

  private Info getInfo(String className) {
    return cache.computeIfAbsent(className, this::analyzeClass);
  }

  public boolean isAValueCapableClass(String className) {
    return getInfo(className).valueCapable;
  }

  public boolean isInternal(String className, String member, String descriptor) {
    return getInfo(className).internals.contains(mangle(member, descriptor));
  }

  private Info analyzeClass(String className) {
    Optional<InputStream> classFileInputStream = classFileFinder.apply(className);
    if (!classFileInputStream.isPresent()) {
      return new Info(false, Collections.emptySet());
    }

    HashSet<String> internals = new HashSet<>();
    boolean[] valueCapable = new boolean[1];
    try(InputStream input = classFileInputStream.get()) {
      ClassReader reader = new ClassReader(input);
      reader.accept(new ClassVisitor(Opcodes.ASM5) {
        @Override
        public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
          if (desc.equals(VALUE_CAPABLE_CLASS_NAME)) {
            valueCapable[0] = true;
          }
          return null;
        }

        @Override
        public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
          checkAndAddInternalMember(access, name, desc);
          return null;
        }

        @Override

        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
          checkAndAddInternalMember(access, name, desc);
          return null;
        }

        private void checkAndAddInternalMember(int access, String name, String desc) {
          if ((access & (ACC_PUBLIC | ACC_PROTECTED)) != 0) {
            return;
          }

          internals.add(mangle(name, desc));
        }

      }, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
    return new Info(valueCapable[0], internals);
  }
}
