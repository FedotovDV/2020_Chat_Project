
import com.google.gson.Gson;
import utility.PasswordEncoding;
import sun.security.util.Password;

import java.io.*;
import java.net.Socket;

public class Main {

    public final static String IP = "localhost";
    public final static int PORT = 8290;

    public static void main(String[] args) {

        System.out.println("Hi, Client!");
        try (Socket socket = new Socket(IP, PORT);
             BufferedReader console = new BufferedReader(new InputStreamReader(System.in))) {
            Client client = EnterLoginPassword(console);
            SendThread t = new SendThread(client, socket);
            ReceiveThread r = new ReceiveThread(client, socket);
//            Gson gson = new Gson();
//            String jsonClient = gson.toJson(client);



//            Thread t = new Thread(() -> {
//                try (BufferedWriter writer = new BufferedWriter(new PrintWriter(socket.getOutputStream()))) {
//                    SendMessage(writer, jsonClient);
//                    while (true) {
//                        String message = console.readLine();
//                        SendMessage(writer, message);
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            });
//            Thread r = new Thread(() -> {
//                try (BufferedReader serverReader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
//                    while (true) {
//                        System.out.println(serverReader.readLine());
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            });
            t.start();
            r.start();
//            Thread.sleep(1999);
            t.join();
            r.join();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

//    private static void SendMessage(BufferedWriter writer, String message) throws IOException {
//        writer.write(message);
//        writer.newLine();
//        writer.flush();
//    }

    private static Client EnterLoginPassword(BufferedReader console) throws Exception {
        Client client = new Client();
        System.out.println("Введите логин:");
        client.setUserName(console.readLine());
        System.out.println("Введите пароль:");

        client.setPassword(Password.readPassword(System.in));
        PasswordEncoding hashPass = new PasswordEncoding();
        client.setHashPass(hashPass.hashPassword(client.getPassword()));

        return client;
    }
}