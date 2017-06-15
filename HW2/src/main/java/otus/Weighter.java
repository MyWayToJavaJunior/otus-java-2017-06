package otus;

import java.util.function.Supplier;

public class Weighter {
    private final int size = 1_000_000;
    private Runtime runtime = Runtime.getRuntime();
    Object[] A;

    public int run(Supplier<Object> supplier) throws InterruptedException {
        int n = 10;
        float result = 0.0f;

        for (int i = 0; i < n; i++) {
            A = new Object[size];

            long startMem = getMem();
            for (int j = 0; j < size; j++) A[j] = supplier.get();
            long endMem = getMem();

            result += (float)(endMem - startMem) / size;
        }

        return Math.round(result / n);
    }

    private long getMem() {
        System.gc();
        return runtime.totalMemory() - runtime.freeMemory();
    }
}
