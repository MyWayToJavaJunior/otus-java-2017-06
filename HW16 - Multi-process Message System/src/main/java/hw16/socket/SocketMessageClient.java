package hw16.socket;

import com.google.gson.Gson;
import hw16.message_system.Message;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SocketMessageClient implements MessageClient {
    private static final Logger logger = Logger.getLogger(SocketMessageClient.class.getName());
    private static final int WORKERS_COUNT = 2;

    private final BlockingQueue<Message> output = new LinkedBlockingQueue<>();
    private final BlockingQueue<Message> input = new LinkedBlockingQueue<>();

    private final ExecutorService executor;
    private final Socket socket;

    public SocketMessageClient(Socket socket) {
        this.socket = socket;
        this.executor = Executors.newFixedThreadPool(WORKERS_COUNT);
        this.init();
    }

    @Override
    public void send(Message msg) {
        output.add(msg);
    }

    @Override
    public Message poll() {
        return  input.poll();
    }

    @Override
    public Message take() throws InterruptedException {
        return input.take();
    }

    private void init() {
        executor.execute(this::sendMessage);
        executor.execute(this::receiveMessage);
    }

    private void receiveMessage() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            String inputLine;
            StringBuilder stringBuilder = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                stringBuilder.append(inputLine);
                if (inputLine.isEmpty()) {
                    String json = stringBuilder.toString();
                    Message msg = Message.getMsgFromJSON(json);
                    input.add(msg);
                    stringBuilder = new StringBuilder();
                }
            }
        } catch (IOException | ParseException e) {
            logger.log(Level.SEVERE, e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage() {
        try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            while (socket.isConnected()) {
                Message msg = output.take();
                String json = new Gson().toJson(msg);
                out.println(json);
                out.println();
            }
        } catch (InterruptedException | IOException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }
}