
import com.google.gson.Gson;
import lombok.extern.java.Log;

import java.io.*;
import java.net.Socket;

public class Main {

    public final static String IP = "localhost";
    public final static int PORT = 8290;

    public static void main(String[] args) {
        System.out.println("Hi, Client!");
        try (BufferedReader console = new BufferedReader(
                new InputStreamReader(System.in));
        ) {
            Socket socket = new Socket(IP, PORT);

            while (true) {
                String message = console.readLine();
                System.out.println("I will send " + message);

                Thread t = new Thread(() -> {
                    try {
                        BufferedWriter writer = new BufferedWriter(new PrintWriter(socket.getOutputStream()));
                        writer.write(message);
                        writer.newLine();
                        writer.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                t.start();

                Thread r = new Thread(() -> {
                    try {
                        BufferedReader serverReader = new BufferedReader(
                                new InputStreamReader(socket.getInputStream()));

                            System.out.println(serverReader.readLine());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                r.start();

                t.join();
                r.join();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}