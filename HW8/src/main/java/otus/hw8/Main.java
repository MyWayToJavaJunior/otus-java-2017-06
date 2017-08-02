package otus.hw8;

import otus.hw8.serialization.Serializer;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        new Main().run();
    }

    public void run() {
        Serializer serializer = new Serializer();
        String result = serializer.exec(new Test1());
        System.out.println(result);
    }

    class Test1 {
        private String name = "Name";
        private Integer age = 1;
        private Long number = 10L;
    }

    class Test2 {
        private String name = "Name";
        private int age = 1;
        private long number = 10L;
        private Test1 test1 = new Test1();
    }

    class Test3 {
        private Integer[] a = new Integer[] {1, 2 ,3};
        private List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3));
        private List<String> list2 = new ArrayList<>(Arrays.asList("1", "sdfa", "asf"));
        private List<String> list3 = new LinkedList<>(Arrays.asList("1", "sdfa", "asf"));
        private Set<String> set = new HashSet<>(Arrays.asList("1", "sdfa", "asf"));
    }

    class Test4 {
        private Map<Integer, Integer> map;

        public Test4() {
            map = new HashMap<>();
            map.put(1, 2);
            map.put(3, 3);
            map.put(5, 1);
        }
    }

    class Test5 {
        private Test3[] test3Arr= new Test3[] {new Test3(), new Test3(), new Test3()};
        private Set<Test1> set = new HashSet<>(Arrays.asList(new Test1(), new Test1()));
        private String str = "asdfa";
        private int[] arr = new int[] {1, 2, 3};
    }
}
