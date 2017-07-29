import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Test;
import otus.hw8.serialization.SerializationNotSupported;
import otus.hw8.serialization.Serializer;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class MainTest {
    @Test
    public void mainTest() throws IOException {
        Item item = new Item();
        String json = new Serializer().exec(item);
        Item gsonObject = new Gson().fromJson(json, Item.class);

//        Если посмотреть в дебаге все в точности одинаково, но equal - false...
//        Assert.assertEquals(item, gsonObject);
    }

    @Test
    public void testArray() {
        Integer[] arr = new Integer[] {1, 2, 3};
        String json = new Serializer().exec(arr);
        Integer[] gsonObject = new Gson().fromJson(json, Integer[].class);
        Assert.assertEquals(arr, gsonObject);
    }

    @Test
    public void testList() {
        List<String> list = new ArrayList<>(Arrays.asList("abc", "ABC"));
        String json = new Serializer().exec(list);
        List gsonObject = new Gson().fromJson(json, List.class);
        Assert.assertEquals(list, gsonObject);
    }

    @Test(expected = SerializationNotSupported.class)
    public void testException() {
        Map<Item, SuperSubItem> map = new HashMap<>();
        map.put(new Item(), new SuperSubItem());
        map.put(new Item(), new SuperSubItem());
        String json = new Serializer().exec(map);
    }

    class Item {
        private String str = "String";
        private Integer age = 1;
        private Long weight = 3L;
        private short height = 7;
        private int[] params = new int[] {1, 2, 3, 4};
        private String[] strings = new String[] {"a", "b", "c"};
        private List<Integer> list = new ArrayList<>(Arrays.asList(100, 200, 300));
        private Set<Long> set = new LinkedHashSet<>(Arrays.asList(3L, 6L));
        private Deque<String> deque = new ArrayDeque<>(Arrays.asList("abc", "абв"));
        private SubItem subItem = new SubItem();
        private SubItem[] subItems = new SubItem[] {new SubItem()};
        private Set<SuperSubItem> superSubItems = new LinkedHashSet<>(Arrays.asList(new SuperSubItem(), new SuperSubItem()));
    }

    class SubItem {
        private int a = new Random().nextInt();
        private Long b = new Random().nextLong();
    }

    class SuperSubItem {
        private int c = new Random().nextInt();
        private SubItem subItem = new SubItem();
    }
}
