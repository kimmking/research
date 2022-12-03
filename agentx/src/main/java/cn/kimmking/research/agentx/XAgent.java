package cn.kimmking.research.agentx;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2022/11/22 23:50
 */
public class XAgent {

    public static void premain(String args, Instrumentation inst) {
        System.out.println("invoke premain: " + args);
//        inst.addTransformer(transformer);
//        inst.redefineClasses(definition);
//        inst.retransformClasses(classes);

        inst.addTransformer(new ClassFileTransformer() {
            @Override
            public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
                System.out.println(className + ", byte[].len=" + className.length());
                final String TEST_CLASS_NAME = "Demo";
                final String METHOD_NAME = "hello";

                String finalClassName = className.replace("/", ".");

                    if (TEST_CLASS_NAME.equals(finalClassName)) {
                        System.out.println("class name is matched.");
                        CtClass ctClass;
                        try {
                            ctClass = ClassPool.getDefault().get(finalClassName);
                            System.out.println("ctClass is OK.");
                            CtMethod ctMethod = ctClass.getDeclaredMethod(METHOD_NAME);
                            System.out.println("CtMethod is OK.");
                            ctMethod.addLocalVariable("_start", CtClass.longType);
                            ctMethod.insertBefore("_start=System.currentTimeMillis();System.out.println(\"before hello 字节码添加成功。\");");
                            //ctMethod.addLocalVariable("_stop", CtClass.longType);
                            ctMethod.insertAfter( "_stop=System.currentTimeMillis();System.out.println(\"after hello 字节码添加成功。方法执行了 \" + (_stop-_start) +\" ms.\" );");
                            //ctMethod.insertAfter( "_stop=System.currentTimeMillis();System.out.println(\"after hello 字节码添加成功。方法执行了 \" + (_stop-Long.parseLong(String.valueOf(_start))) +\" ms.\" );");
                            ctClass.writeFile();
                            return ctClass.toBytecode();
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println(e.getMessage());
                        }
                    }
                    return null;
                }

//            @Override
//            public byte[] transform(Module module, ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
//                return classfileBuffer;
//            }
        });

    }

    public static void premain(String args) {
        // do nothing
    }
}
