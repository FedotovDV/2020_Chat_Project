import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class ReceiveThread implements Runnable {
    private Thread thread;
    private Client client;
    private Socket socket;

    public ReceiveThread(Client client, Socket socket) {
        thread = new Thread(this);
        this.client = client;
        this.socket = socket;

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
                String clientsInChat = "members in chat = ";
//                if (message.indexOf(clientsInChat) == 0) {
//                    jlNumberOfClients.setText(message);
//                } else {
//                    setJtaTextAreaMessage(message);
//                    jtaTextAreaMessage.append("\n");
//                }
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
