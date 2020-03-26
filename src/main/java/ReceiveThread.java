import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ReceiveThread implements Runnable {
    private Thread thread;
    private Client client;
    private Socket socket;

    public ReceiveThread(Client client, Socket socket) {
        thread = new Thread(this);
        this.client =client;
        this.socket =socket;

    }

    public void start() {
        thread.start();
    }

    public void interrupt() {
        thread.interrupt();
    }

    public void join() throws InterruptedException {
        thread.join();
    }
    @Override
    public void run() {
        try (BufferedReader serverReader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                    while (!thread.isInterrupted()) {
                        System.out.println(serverReader.readLine());
                        Thread.sleep(500);
                    }
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
    }
}
