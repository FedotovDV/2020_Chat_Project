package utility;

import client.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;
import frame.GUIClient;


public class ReceiveThread implements Runnable {
    private Thread thread;
    private Client client;
    private Socket socket;
    private GUIClient guiClient;

    public ReceiveThread(Client client, Socket socket, GUIClient guiClien) {
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
        try (BufferedReader serverReader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            while (!thread.isInterrupted()) {
                String message = serverReader.readLine();
                String clientsInChat = "Members in chat = ";
                if (message.startsWith("{")) {
                    Gson gson = new Gson();
                    client = gson.fromJson(message, Client.class);
                    guiClient.setTextAreaMessage("Сведения о клиенте: ");
                    guiClient.setTextAreaMessage(message);
                    guiClient.setTextAreaMessage("\n");
                } else {
                    if (message.indexOf(clientsInChat) == 0) {
                        guiClient.setNumberOfClient(message);
                    } else {
                        guiClient.setTextAreaMessage(message);
                        guiClient.setTextAreaMessage("\n");
                    }
                    if (message.equalsIgnoreCase("Exit")) {
                        thread.interrupt();
                    }
                }
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    thread.interrupt();
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
