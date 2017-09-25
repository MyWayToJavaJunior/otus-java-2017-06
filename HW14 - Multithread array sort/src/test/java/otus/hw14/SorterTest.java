package otus.hw14;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.Assert.assertTrue;

public class SorterTest {
    @Test
    public void manualTest() {
        int[] data1 = new int[] {5, 4, 3, 2, 1, 0, -1};
        int[] exp1 = new int[] {-1, 0, 1, 2, 3, 4, 5};
        assertTrue(Arrays.equals(exp1, new Sorter(data1).sort()));

        int[] data2 = new int[] {3, 6, 7, 34, 0, 4, 31, 56, 1, 11};
        int[] exp2 = new int[] {0, 1, 3, 4, 6, 7, 11, 31, 34, 56};
        assertTrue(Arrays.equals(exp2, new Sorter(data2).sort()));
    }

    @Test
    public void autoTest() {
        Random random = new Random();
        int[] data = new int[50];
        for (int i = 0; i < 50; i++) data[i] = random.nextInt();

        int[] actual = new Sorter(data).sort();
        Arrays.sort(data);
        assertTrue(Arrays.equals(data, actual));
    }

    @Test
    public void largeAutoTest() {
        Random random = new Random();
        int[] data = new int[1000];
        for (int i = 0; i < 1000; i++) data[i] = random.nextInt();

        int[] actual = new Sorter(data).sort();
        Arrays.sort(data);
        assertTrue(Arrays.equals(data, actual));
    }

    @Test
    public void largeAutoTestImp() {
        Random random = new Random();
        int[] data = new int[10000];
        for (int i = 0; i < 10000; i++) data[i] = random.nextInt();

        int[] actual = new Sorter(data, 30).sort();
        Arrays.sort(data);
        assertTrue(Arrays.equals(data, actual));
    }
}
