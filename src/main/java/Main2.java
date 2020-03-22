
import com.google.gson.Gson;
import lombok.extern.java.Log;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;

public class Main2 {

    public final static String IP = "localhost";
    public final static int PORT = 8290;

    public static void main(String[] args) {
        byte[] hashVal1 = new byte[]{22, 55, 11};
        byte[] hashVal2 = new byte[]{22, 55, 11};
        boolean arraysEqual = Arrays.equals(hashVal1, hashVal2);
        System.out.println(arraysEqual);
    }
//        System.out.println("Hi, Client!");
//        try (BufferedReader console = new BufferedReader(new InputStreamReader(System.in))) {
//            Socket socket = new Socket(IP, PORT);
////            Client client = new Client();
////            System.out.println("Введите логин:");
////            client.setUserName(console.readLine());
////            System.out.println(client.getUserName());
////            System.out.println("Введите пароль:");
////            client.setUserPassword(console.readLine().toCharArray());
////
////            Gson gson = new Gson();
////            String json = gson.toJson(client);
////            System.out.println(json);
//            while (true) {
//
////            client = gson.fromJson(json, Client.class);
////            System.out.println(client.toString());
//
//
////            Client finalClient = client;
//                Thread t = new Thread(() -> {
//                    try {
////                    ObjectOutputStream writer = new ObjectOutputStream(socket.getOutputStream());
////                    writer.writeObject(finalClient);
////                    writer.flush();
//                        PrintWriter writerClient = new PrintWriter(socket.getOutputStream());
//                        writerClient.println("REGISTRATIONuser:123456");
////                        writerClient.println(json);
////                        writerClient.write(client.toJSON());
//                        writerClient.flush();
////
////                        String message = console.readLine();
////
////                        writerClient.write(message);
////                        writerClient.flush();
////
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                });
//                t.start();
//                t.join();
//            }
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        }
//
//    }
}
