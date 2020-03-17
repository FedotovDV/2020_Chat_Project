
import java.io.*;
import java.net.Socket;

public class Main {

    public final static String IP = "localhost";
    public final static int PORT = 8290;

    public static void main(String[] args) {
        System.out.println("Hi, Client!");
        try (BufferedReader console = new BufferedReader(new InputStreamReader(System.in))) {
            Socket socket = new Socket(IP, PORT);
            Client client = new Client();
            System.out.println("Введите логин:");
            client.setUserName(console.readLine());
            System.out.println(client.getUserName());
            System.out.println("Введите пароль:");
            client.setUserPassword(console.readLine().toCharArray());
            System.out.println(client.toString());


            new Thread(() -> {
                try {
                    ObjectOutputStream writer = new ObjectOutputStream(socket.getOutputStream());
                    writer.writeObject(client);
                    writer.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
