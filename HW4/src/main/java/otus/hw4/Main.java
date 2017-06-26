package otus.hw4;

import com.sun.management.GarbageCollectionNotificationInfo;

import javax.management.*;
import javax.management.openmbean.CompositeData;
import java.lang.management.GarbageCollectorMXBean;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main implements Runnable{
    private long startTime = System.currentTimeMillis();
    private ArrayList<String> gcNames = new ArrayList<>();
    private Map<String, Integer> gcCollections = new HashMap<>();
    private Map<String, Long> gcDurations = new HashMap<>();

    public static void main(String[] args) throws Exception {
        Main main = new Main();

        main.installGCMonitoring();
        new Thread(main).start();
        main.start();
    }

    private void start() throws InterruptedException {
        int msToWait = 1300;
        int size = 1_000_000;
        List<Object> list = new ArrayList<>();

        while(true) {
            int stopSize = size/10;
            List<Object> garbage = new ArrayList<>();
            for (int i = 0; i < stopSize; i++) {
                list.add(new Object());
            }
            for (int i = stopSize; i < size; i++) {
                garbage.add(new Object());
            }

            Thread.sleep(msToWait);
        }
    }

    private void installGCMonitoring() {
        List<GarbageCollectorMXBean> gcbeans = java.lang.management.ManagementFactory.getGarbageCollectorMXBeans();

        for (GarbageCollectorMXBean gcbean : gcbeans) {
            System.out.println("GC: " + gcbean.getName());

            gcNames.add(gcbean.getName());
            gcCollections.put(gcbean.getName(), 0);
            gcDurations.put(gcbean.getName(), 0L);

            NotificationEmitter emitter = (NotificationEmitter) gcbean;
            NotificationListener listener = (notification, handback) -> {
                if (notification.getType().equals(GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION)) {

                    GarbageCollectionNotificationInfo info = GarbageCollectionNotificationInfo.from((CompositeData) notification.getUserData());

                    String name = info.getGcName();
                    long duration = info.getGcInfo().getDuration();

                    gcCollections.put(name, gcCollections.get(name) + 1);
                    gcDurations.put(name, gcDurations.get(name) + duration);

//                    System.out.println("[" + curTime() + "] " + name + " - " + duration + "ms (" + info.getGcAction() + ")");
                }
            };

            emitter.addNotificationListener(listener, null, null);
        }
    }

    @Override
    public void run() {
        for (int i = 1; i <= 5; i++) {
            try { Thread.sleep(60 * 1000); } catch (Exception e) {}
            System.out.print(i + " minute: ");
            System.out.print(gcNames.get(0) + " - " + gcCollections.get(gcNames.get(0)) + " collections, " + ((double) gcDurations.get(gcNames.get(0)) / 1000) + "s. ");
            System.out.println(gcNames.get(1) + " - " + gcCollections.get(gcNames.get(1)) + " collections, " + ((double) gcDurations.get(gcNames.get(1)) / 1000) + "s");
        }
    }

    private String curTime() {
        long time = System.currentTimeMillis() - startTime;
        return new SimpleDateFormat("mm:ss").format(new Date(time));
    }
}
