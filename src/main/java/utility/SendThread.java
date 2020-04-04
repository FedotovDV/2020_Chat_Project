package utility;

import client.Client;
import com.google.gson.Gson;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

import frame.GUIClient;
import lombok.extern.log4j.Log4j;

@Log4j
public class SendThread implements Runnable {
    private Thread thread;
    private Client client;
    private Socket socket;
    private GUIClient guiClient;

    public SendThread(Client client, Socket socket, GUIClient guiClien) {
        thread = new Thread(this);
        this.client = client;
        this.socket = socket;
        this.guiClient = guiClien;
    }

    public void start() {
        thread.start();
    }

    public void interrupt() {
        thread.interrupt();
    }


    @Override
    public void run() {
        try (BufferedWriter writer = new BufferedWriter(new PrintWriter(socket.getOutputStream()));
             BufferedReader console = new BufferedReader(new InputStreamReader(System.in))) {

            Gson gson = new Gson();
            String sendJson = TypeMessage.LOGIN.name();
            String jsonClient = gson.toJson(client);
            sendJson += jsonClient;
            SendMessage(writer, sendJson);
            log.info("Client send "+ sendJson);
            while (!thread.isInterrupted()) {
                String message = console.readLine();
                String sendMessage;
                if (message.equalsIgnoreCase("Exit")) {
                    sendMessage = TypeMessage.LOGOUT.name();
                    SendMessage(writer, sendMessage);
                    log.info("Client send "+ sendMessage);
                    thread.interrupt();
                }
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    thread.interrupt();
                }
                sendMessage = TypeMessage.MESSAGE.name() + message;
                SendMessage(writer, sendMessage);
                log.info("Client send "+ sendMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void SendMessage(BufferedWriter writer, String message) throws IOException {
        log.info("SendMessage " + message);
        writer.write(message);
        writer.newLine();
        writer.flush();
    }


}
