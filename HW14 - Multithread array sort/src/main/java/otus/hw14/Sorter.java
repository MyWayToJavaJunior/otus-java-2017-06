package otus.hw14;

import java.util.*;

public class Sorter {
    private final int[] arr;
    private final int threadCount;

    private List<int[]> arrays = new ArrayList<>();

    public Sorter(int[] arr) {
        this.arr = arr;
        this.threadCount = 4;
    }

    public Sorter(int[] arr, int threadCount) {
        this.arr = arr;
        this.threadCount = threadCount;
    }

    public int[] sort() {
        int part = arr.length / threadCount;

        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < threadCount; i++) {
            arrays.add(new int[1]);

            int to = i == threadCount - 1 ? arr.length : part * (i+1);
            Thread worker = getThread(i,part * i, to);

            worker.start();
            threads.add(worker);
        }

        try {
            for (Thread worker : threads) worker.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return new Merger(arrays).getResult();
    }

    private Thread getThread(int index, int from, int to) {
        return new Thread(() -> {
            Arrays.sort(arr, from, to);
            arrays.set(index, Arrays.copyOfRange(arr, from, to));
        });
    }
    
    private class Merger {
        int[] result = new int[arr.length];
        List<Integer> pointers = new ArrayList<>();

        Queue<int[]> queue;

        public Merger(List<int[]> list) {
            this.queue = new LinkedList<>(list);
        }

        public int[] getResult() {
            while(queue.size() != 1) {
                queue.add(sort(queue.poll(), queue.poll()));
            }

            return queue.poll();
        }

        private int[] sort(int[] a, int[] b) {
            int pointer1 = 0;
            int pointer2 = 0;

            int[] result = new int[a.length + b.length];

            for (int i = 0; i < result.length; i++) {
                if (pointer1 == a.length) {
                    result[i] = b[pointer2];
                    pointer2++;
                    continue;
                }

                if (pointer2 == b.length) {
                    result[i] = a[pointer1];
                    pointer1++;
                    continue;
                }

                int min = Math.min(a[pointer1], b[pointer2]);

                if (min == a[pointer1]) pointer1++;
                else pointer2++;

                result[i] = min;
            }

            return result;
        }
    }
}
