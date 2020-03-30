package frame;

import client.Client;
import lombok.SneakyThrows;
import utility.SendThread;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;


public class ClientWindow extends JFrame {
    public final static String SERVER_HOST = "localhost";
    public final static int SERVER_PORT = 8290;

    private Client client;
    private Socket clientSocket;
    private Scanner inMessage;
    private PrintWriter outMessage;
    private JLabel jlNumberOfClients;
    private JTextField jtfMessage;
    private JLabel jlUserName;
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
        setTitle("client.Client");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jtaTextAreaMessage = new JTextArea();
        jtaTextAreaMessage.setEditable(false);
        jtaTextAreaMessage.setLineWrap(true);
        JScrollPane jsp = new JScrollPane(jtaTextAreaMessage);
        add(jsp, BorderLayout.CENTER);

        jlNumberOfClients = new JLabel("Members in chat: ");
        add(jlNumberOfClients, BorderLayout.NORTH);
        JPanel bottomPanel = new JPanel(new BorderLayout());
        add(bottomPanel, BorderLayout.SOUTH);
        JButton jbSendMessage = new JButton("Send");
        bottomPanel.add(jbSendMessage, BorderLayout.EAST);
        jtfMessage = new JTextField("Enter your message: ");
        bottomPanel.add(jtfMessage, BorderLayout.CENTER);
        jlUserName = new JLabel("  " + client.getUserName() + "    ");
        bottomPanel.add(jlUserName, BorderLayout.WEST);

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
       utility.ReceiveThread receiveThread = new utility.ReceiveThread(client, clientSocket, this);
        sendThread.start();
        receiveThread.start();

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

    public  void setTextAreaMessage(String message) {
        jtaTextAreaMessage.append(message);
    }

    public  void setNumberOfClient(String message) {
        jlNumberOfClients.setText(message);
    }


    @SneakyThrows
    private void SendMessage(String message) {
        BufferedWriter writer = new BufferedWriter(outMessage);
        writer.write(message);
        writer.newLine();
        writer.flush();
    }

}
