
import com.google.gson.Gson;
import lombok.extern.java.Log;

import java.io.*;
import java.net.Socket;

public class Main {

    public final static String IP = "localhost";
    public final static int PORT = 8290;

    public static void main(String[] args) {
        String message;
        System.out.println("Hi, Client!");
        try (BufferedReader console = new BufferedReader(
                new InputStreamReader(System.in));
        ) {
            Socket socket = new Socket(IP, PORT);

            try (BufferedWriter writer = new BufferedWriter(new PrintWriter(socket.getOutputStream()));
                 BufferedReader serverReader = new BufferedReader(
                         new InputStreamReader(socket.getInputStream()));) {
                while (!(message = console.readLine()).equals("Exit")) {

                    System.out.println("I will send " + message);
                    writer.write(message);
                    writer.newLine();
                    writer.flush();
                    System.out.println(serverReader.readLine());
                }
            } catch (IOException e) {
                e.printStackTrace();


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}