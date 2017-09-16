package otus.hw14;

import java.util.Arrays;

public class Sorter {
    private final int[] arr;

    private int[] arr1;
    private int[] arr2;
    private int[] arr3;
    private int[] arr4;

    public Sorter(int[] arr) {
        this.arr = arr;
    }

    public int[] sort() {
        int part = arr.length / 4;

        Thread worker1 = new Thread(() -> {
            Arrays.sort(arr, 0, part);
            arr1 = Arrays.copyOfRange(arr, 0, part);
        });
        Thread worker2 = new Thread(() -> {
            Arrays.sort(arr, part, part*2);
            arr2 = Arrays.copyOfRange(arr, part, part*2);
        });
        Thread worker3 = new Thread(() -> {
            Arrays.sort(arr, part*2, part*3);
            arr3 = Arrays.copyOfRange(arr, part*2, part*3);
        });
        Thread worker4 = new Thread(() -> {
            Arrays.sort(arr, part*3, arr.length);
            arr4 = Arrays.copyOfRange(arr, part*3, arr.length);
        });

        worker1.start();
        worker2.start();
        worker3.start();
        worker4.start();

        try {
            worker1.join();
            worker2.join();
            worker3.join();
            worker4.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return new Merger().getResult();
    }
    
    private class Merger {
        int[] result = new int[arr.length];
        
        int pointer1 = 0;
        int pointer2 = 0;
        int pointer3 = 0;
        int pointer4 = 0;

        public int[] getResult() {
            for (int i = 0; i < result.length; i++) {
                result[i] = getValue();
            }
            return result;
        }

        private int getValue() {
            return getMin(pointer1 < arr1.length ? arr1[pointer1] : Integer.MAX_VALUE,
                    pointer2 < arr2.length ? arr2[pointer2] : Integer.MAX_VALUE,
                    pointer3 < arr3.length ? arr3[pointer3] : Integer.MAX_VALUE,
                    pointer4 < arr4.length ? arr4[pointer4] : Integer.MAX_VALUE);
        }
        
        private int getMin(int val1, int val2, int val3, int val4) {
            if (pointer1 < arr1.length && val1 < val2 && val1 < val3 && val1 < val4) {
                pointer1++;
                return val1;
            } else if (pointer2 < arr2.length && val2 < val1 && val2 < val3 && val2 < val4) {
                pointer2++;
                return val2;
            } else if (pointer3 < arr3.length && val3 < val1 && val3 < val2 && val3 < val4) {
                pointer3++;
                return val3;
            }
            else {
                pointer4++;
                return val4;
            }
        }
    }

    private Thread getWorker(int fromIndex, int toIndex) {
        return new Thread(() -> {
            Arrays.sort(arr, fromIndex, toIndex);
            arr1 = Arrays.copyOfRange(arr, fromIndex, toIndex);
        });
    }
}
