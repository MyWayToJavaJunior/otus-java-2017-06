package hw16;

import org.eclipse.jetty.util.thread.ScheduledExecutorScheduler;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    private static final int DELAY_SEC = 2;

    public static void main(String[] args) throws Exception {
        String mode = args.length >= 1 ? args[0] : "default";
        switch (mode) {
            case "frontend":
                FrontendMain.main(args);
                break;
            case "db":
                DBServerMain.main(null);
                break;
            case "message_system":
                MessageServerMain.main(null);
                break;
            default:
                runProcesses();
                MessageServerMain.main(null);
                break;
        }
    }

    private static void runProcesses() throws IOException, URISyntaxException, InterruptedException {
        String jarPath = Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();

        Executors.newSingleThreadScheduledExecutor().schedule(() -> {
            try {
                new ProcessBuilder("java", "-jar", jarPath, "frontend", "7070").start();
                new ProcessBuilder("java", "-jar", jarPath, "frontend", "6060").start();
                new ProcessBuilder("java", "-jar", jarPath, "db").start();
                Thread.sleep(3000); // doesn't work, without it
                new ProcessBuilder("java", "-jar", jarPath, "db").start();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, DELAY_SEC, TimeUnit.SECONDS);

    }
}
