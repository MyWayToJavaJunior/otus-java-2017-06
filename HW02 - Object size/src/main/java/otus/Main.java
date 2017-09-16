package otus;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Weighter counter = new Weighter();

        System.out.println("Object: " + counter.run(() -> new Object()));
        System.out.println("String(\"\"): " + counter.run(() -> new String("")));
        System.out.println("Empty string: " + counter.run(() -> new String(new char[0])));
        System.out.println("Integer: " + counter.run(() -> new Integer(0)));
        System.out.println("Long: " + counter.run(() -> new Long(0)));
        System.out.println("Custom class {int; long; String}: " + counter.run(() -> new Custom()));
        System.out.println("int[5]: " + counter.run(() -> new int[5]));
        System.out.println("int[100]: " + counter.run(() -> new int[100]));
        System.out.println("Integer[5]: " + counter.run(() -> new Integer[5]));
        System.out.println("Integer[100]: " + counter.run(() -> new Integer[100]));
        System.out.println("LinkedList<Integer>: " + counter.run(() -> new LinkedList<Integer>()));
        System.out.println("ArrayList<Integer>(5): " + counter.run(() -> new ArrayList<Integer>(5)));
        System.out.println("ArrayList<Integer>(100): " + counter.run(() -> new ArrayList<Integer>(100)));
        System.out.println("LinkedList<Integer>(5): " + counter.run(() -> {
            List<Integer> list = new LinkedList<>();
            for (int i = 0; i < 5; i++) list.add(new Integer(0));
            return list;
        }));
    }

    static class Custom {
        int i;
        long l;
        String s;
    }
}
