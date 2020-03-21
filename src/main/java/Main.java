
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
            Thread t = new Thread(() -> {
                try (BufferedWriter writer = new BufferedWriter(new PrintWriter(socket.getOutputStream()))) {

                    while (true) {
                        String message = console.readLine();
                        System.out.println("I will send " + message);
                        writer.write(message);
                        writer.newLine();
                        writer.flush();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            });


            Thread r = new Thread(() -> {
                try (
                        BufferedReader serverReader = new BufferedReader(
                                new InputStreamReader(socket.getInputStream()))) {

                    while (true) {
                        System.out.println(serverReader.readLine());
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            t.start();
            r.start();
            t.sleep(1000);
            t.sleep(1000);
            t.join();
            r.join();
            socket.close();
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }

    }
}