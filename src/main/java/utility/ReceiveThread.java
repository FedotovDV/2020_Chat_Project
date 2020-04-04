package utility;

import client.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;
import frame.GUIClient;

import lombok.extern.log4j.Log4j;

@Log4j
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
//            while (!socket.isClosed()){
            while (!thread.isInterrupted()) {
                log.info(thread.getName() + " has been started");
                String message = serverReader.readLine();

                if (message.startsWith(TypeMessage.INFO.name())) {
                    message = message.replaceFirst(TypeMessage.INFO.name(), " ");
                    String clientsInChat = "Members in chat = ";
                    guiClient.setNumberOfClient(message);
                } else if (message.startsWith(TypeMessage.LOGIN.name())) {
                    log.info("clientMessage = " + message);
                    Gson gson = new Gson();
                    String jsonClient = message.replaceFirst(TypeMessage.LOGIN.name(), "");
                    client = gson.fromJson(jsonClient, Client.class);

                    guiClient.setTextAreaMessage("Сведения о клиенте: ");
                    guiClient.setTextAreaMessage(client.getUserName());
                    guiClient.setTextAreaMessage("\n");
                } else if (message.startsWith(TypeMessage.MESSAGE.name())) {
                    message = message.replaceFirst(TypeMessage.MESSAGE.name(), " ");
                    guiClient.setTextAreaMessage(message);
                    guiClient.setTextAreaMessage("\n");
                } else if(message.equals(TypeMessage.WRONGPASS.name())) {
                    guiClient.setTextAreaMessage(" Введен неверный логин/пароль, \nзакройте приложение и запустите заново");

                } else if (message.equals(TypeMessage.LOGOUT.name())) {
                    thread.interrupt();
                }

                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    log.info(thread.getName() + " has been interrupted");
                    thread.interrupt();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
