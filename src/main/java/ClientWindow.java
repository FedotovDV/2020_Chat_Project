import lombok.SneakyThrows;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class ClientWindow extends JFrame {
    public final static String SERVER_HOST = "localhost";
    public final static int SERVER_PORT = 8290;
    private Thread thread;
    private Client client;
    private Socket clientSocket;
    private Scanner inMessage;
    private PrintWriter outMessage;

    private JTextField jtfMessage;
    private JLabel userName;
    private JTextArea jtaTextAreaMessage;


    public ClientWindow(Client client) {
        this.client = client;


        try {
            clientSocket = new Socket(SERVER_HOST, SERVER_PORT);
            inMessage = new Scanner(clientSocket.getInputStream());
            outMessage = new PrintWriter(clientSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }


        setBounds(600, 300, 600, 500);
        setTitle("Client");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jtaTextAreaMessage = new JTextArea();
        jtaTextAreaMessage.setEditable(false);
        jtaTextAreaMessage.setLineWrap(true);
        JScrollPane jsp = new JScrollPane(jtaTextAreaMessage);
        add(jsp, BorderLayout.CENTER);

        JLabel jlNumberOfClients = new JLabel("Members in chat: ");
        add(jlNumberOfClients, BorderLayout.NORTH);
        JPanel bottomPanel = new JPanel(new BorderLayout());
        add(bottomPanel, BorderLayout.SOUTH);
        JButton jbSendMessage = new JButton("Send");
        bottomPanel.add(jbSendMessage, BorderLayout.EAST);
        jtfMessage = new JTextField("Enter your message: ");
        bottomPanel.add(jtfMessage, BorderLayout.CENTER);
        userName = new JLabel(client.getUserName() + "    ");
        bottomPanel.add(userName, BorderLayout.WEST);

        jbSendMessage.addActionListener(e -> {
            if (!jtfMessage.getText().trim().isEmpty()) {
                SendMessage(jtfMessage.getText());
                jtfMessage.grabFocus();
            }
        });

        jtfMessage.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                jtfMessage.setText("");
            }
        });

        SendThread sendThread = new SendThread(client, clientSocket);
//        ReceiveThread receiveThread = new ReceiveThread(client, clientSocket);
        sendThread.start();
//        receiveThread.start();
        new Thread(() -> {
            try (BufferedReader serverReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
                while (true) {
                    String message = serverReader.readLine();
                    String clientsInChat = "members in chat = ";
                    if (message.indexOf(clientsInChat) == 0) {
                        jlNumberOfClients.setText(message);
                    } else {
                        jtaTextAreaMessage.append(message);
                        jtaTextAreaMessage.append("\n");
                    }
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
        }).start();


        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                try {
                    outMessage.println("Exit");
                    outMessage.flush();
                    outMessage.close();
                    inMessage.close();
                    clientSocket.close();
                } catch (IOException exc) {

                }
            }
        });

        setVisible(true);


    }


    @SneakyThrows
    private void SendMessage(String message) {
        BufferedWriter writer = new BufferedWriter(outMessage);
        writer.write(message);
        writer.newLine();
        writer.flush();
    }

}
