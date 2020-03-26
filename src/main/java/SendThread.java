import com.google.gson.Gson;
import sun.security.util.Password;
import utility.PasswordEncoding;

import java.io.*;
import java.net.Socket;


public class SendThread implements Runnable {
    private Thread thread;
    private Client client;
    private Socket socket;


    public SendThread(Client client, Socket socket) {
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
        try (BufferedWriter writer = new BufferedWriter(new PrintWriter(socket.getOutputStream()));
             BufferedReader console = new BufferedReader(new InputStreamReader(System.in))) {
            Gson gson = new Gson();
            String jsonClient = gson.toJson(client);
            SendMessage(writer, jsonClient);
            while (!thread.isInterrupted()) {
                String message = console.readLine();
                SendMessage(writer, message);
                Thread.sleep(500);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void SendMessage(BufferedWriter writer, String message) throws IOException {
        writer.write(message);
        writer.newLine();
        writer.flush();
    }


}
