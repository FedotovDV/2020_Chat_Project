package utility;

import client.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.TimeUnit;
import frame.ClientWindow;

public class ReceiveThread implements Runnable {
    private Thread thread;
    private Client client;
    private Socket socket;
    private ClientWindow clientWindow;

    public ReceiveThread(Client client, Socket socket, ClientWindow clientWindow) {
        thread = new Thread(this);
        this.client = client;
        this.socket = socket;
        this.clientWindow = clientWindow;

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
                if (message.indexOf(clientsInChat) == 0) {
                    clientWindow.setNumberOfClient(message);
                } else {
                    clientWindow.setTextAreaMessage(message);
                    clientWindow.setTextAreaMessage("\n");
                }
                if (message.equalsIgnoreCase("Exit")) {
                    thread.interrupt();
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
