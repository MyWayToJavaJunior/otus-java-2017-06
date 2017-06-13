package otus;

public class Weighter {
    private final int size = 1_000_000;
    private Runtime runtime = Runtime.getRuntime();

    interface Action {
        Object exec();
    }

    public int run(Action action) throws InterruptedException {
        int n = 10;
        float result = 0.0f;

        for (int i = 0; i < n; i++) {
            Object array[] = new Object[size];

            long startMem = runtime.totalMemory() - runtime.freeMemory();
            for (int j = 0; j < size; j++) array[j] = action.exec();
            long endMem = runtime.totalMemory() - runtime.freeMemory();

            result += (float)(endMem - startMem) / size;

            System.gc();
            Thread.sleep(10);
        }

        return Math.round(result / n);
    }
}
