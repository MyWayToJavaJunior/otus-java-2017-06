package otus.hw5;

import junit.framework.AssertionFailedError;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import sun.reflect.Reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class TestFramework {
    private int testCount = 0;
    private int failedCount = 0;

    public static void run(String path) {
        List<ClassLoader> classLoadersList = new LinkedList<ClassLoader>();
        classLoadersList.add(ClasspathHelper.contextClassLoader());
        classLoadersList.add(ClasspathHelper.staticClassLoader());

        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setScanners(new SubTypesScanner(false), new ResourcesScanner())
                .setUrls(ClasspathHelper.forClassLoader(classLoadersList.toArray(new ClassLoader[0])))
                .filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix(path))));

        Set<Class<? extends Object>> clazzes = reflections.getSubTypesOf(Object.class);

        TestFramework obj = new TestFramework();
        for (Class clazz : clazzes) obj.exec(clazz);

        System.out.println("Passed: " + obj.testCount + ", failed: " + obj.failedCount);
    }

    public static void run(Class<?>[] clazzes) {
        TestFramework obj = new TestFramework();
        for (Class clazz : clazzes) obj.exec(clazz);

        System.out.println("Passed: " + obj.testCount + ", failed: " + obj.failedCount);
    }

    public void exec(Class<?> clazz) {
        Method before = getMethodFromList(ReflectionHelper.getMethodsAnnotatedWith(clazz, MyBefore.class));
        Method after = getMethodFromList(ReflectionHelper.getMethodsAnnotatedWith(clazz, MyAfter.class));
        List<Method> tests = ReflectionHelper.getMethodsAnnotatedWith(clazz, MyTest.class);

        for (Method test : tests) {
            Object obj = ReflectionHelper.instantiate(clazz);
            callMethod(obj, before);
            callMethod(obj, test);
            callMethod(obj, after);
            testCount++;
        }
    }

    private Method getMethodFromList(List<Method> methods) {
        if (methods.isEmpty()) return null;
        else return methods.get(0);
    }

    private void callMethod(Object object, Method method) {
        if (method == null) return;

        boolean isAccessible = true;
        try {
            isAccessible = method.isAccessible();
            method.setAccessible(true);
            method.invoke(object);
        } catch (IllegalAccessException | InvocationTargetException e) {
            if (e.getCause() instanceof AssertionFailedError) {
                failedCount++;

                System.out.print(object.getClass().getName() + "." + method.getName() + "() failed: ");
                System.out.println(e.getCause().getMessage());
                for (StackTraceElement el : e.getCause().getStackTrace()) System.out.println("\t" + el);
                System.out.println();
            }
            else e.printStackTrace();
        }
        finally {
            if (!isAccessible) method.setAccessible(false);
        }
    }
}
