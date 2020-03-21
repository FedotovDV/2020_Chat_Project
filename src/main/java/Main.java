
import com.google.gson.Gson;
import core.Password;
import lombok.extern.java.Log;

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
            Gson gson = new Gson();
            String jsonClient = gson.toJson(client);
            Password password = new Password();
            String jsonPassword = gson.toJson(client);
            password.setPassword(client.getUserPassword().getBytes());
            System.out.println("Введите пароль:");
            String pass = console.readLine();
            System.out.println("password.checkPassword(pass.getBytes()) = " + password.checkPassword(pass.getBytes()));
            String loginPassword = "#$#pass#/" + jsonClient + "/" + jsonPassword;
            System.out.println(password);

            Thread t = new Thread(() -> {
                try (BufferedWriter writer = new BufferedWriter(new PrintWriter(socket.getOutputStream()))) {
                    SendMessage(writer, loginPassword);
                    while (true) {
                        String message = console.readLine();
                        SendMessage(writer, message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            Thread r = new Thread(() -> {
                try (BufferedReader serverReader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                    while (true) {
                        System.out.println(serverReader.readLine());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            t.start();
            r.start();
            t.sleep(1999);
            r.sleep(1999);
            t.join();
            r.join();

        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void SendMessage(BufferedWriter writer, String message) throws IOException {
        writer.write(message);
        writer.newLine();
        writer.flush();
    }

    private static Client EnterLoginPassword(BufferedReader console) throws IOException {
        Client client = new Client();
        System.out.println("Введите логин:");
        client.setUserName(console.readLine());
        System.out.println("Введите пароль:");
        client.setUserPassword(console.readLine());
        return client;
    }
}