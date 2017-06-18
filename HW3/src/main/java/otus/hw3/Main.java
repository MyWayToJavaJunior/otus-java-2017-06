package otus.hw3;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Integer> list = new MyArrayList<>(Integer.class);

        list.add(1);
        list.add(2);
        System.out.println("Test: " + list);
        list.add(3);
        list.add(7);
        list.add(8);
        list.add(52);
        list.add(104);
        System.out.println("Test: " + list);
        System.out.println("Get 0: " + list.get(0));
        System.out.println("Get 3: " + list.get(3));
        list.set(3, 1);
        System.out.println("Get 3 after Set 3: " + list.get(3));

        System.out.println("Test: " + list);

        list.replaceAll((i) -> (i+5));
        System.out.println("Replace: " + list);

        list.remove(2);
        list.remove(3);
        System.out.println("Remove 2 and 3: " + list);

        System.out.println("Index 7: " + list.indexOf(7));

        System.out.println("SubList: " + list.subList(1, 4));

        System.out.print("Iterator: ");
        for (Integer i : list) {
            System.out.print(i + " ");
        }
        System.out.println();

        List<Integer> vanillaList = new ArrayList<>();
        vanillaList.add(6);
        vanillaList.add(57);
        System.out.println("ContainsAll: " + list.containsAll(vanillaList));
        vanillaList.add(100);
        System.out.println("ContainsAll with 100: " + list.containsAll(vanillaList));

        list.addAll(vanillaList);
        System.out.println("AddAll: " + list);

        list.addAll(3, vanillaList);
        System.out.println("AddAll after 3: " + list);

        list.removeAll(vanillaList);
        System.out.println("RemoveAll: " + list);

        vanillaList.clear();
        vanillaList.add(109);

        list.retainAll(vanillaList);
        System.out.println("RetainAll: " + list);

        System.out.print("ToArray: ");
        for (Object i : list.toArray()) System.out.print(i + " ");
        System.out.println();

        list.clear();
        System.out.println("Clear: " + list);

        for (int i = 0; i < 100; i++) list.add(i);
        System.out.println("Add 100 values: " + list);
    }
}
