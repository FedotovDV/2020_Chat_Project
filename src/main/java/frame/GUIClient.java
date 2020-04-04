package frame;


import client.Client;
import frame.LoginForm;
import lombok.Getter;
import lombok.SneakyThrows;
import utility.PasswordEncoding;
import utility.ReceiveThread;
import utility.SendThread;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import lombok.extern.log4j.Log4j;
import utility.TypeMessage;

@Getter
@Log4j
public class GUIClient extends JFrame {

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


    public GUIClient() {
        log.info("Client start");

        try {
            clientSocket = new Socket(SERVER_HOST, SERVER_PORT);
            inMessage = new Scanner(clientSocket.getInputStream());
            outMessage = new PrintWriter(clientSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        client = enterLoginPassword();
        drawClientWindow();

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
        SendThread sendThread = new SendThread(client, clientSocket, this);
        ReceiveThread receiveThread = new utility.ReceiveThread(client, clientSocket, this);
        sendThread.start();
        receiveThread.start();


    }

    public JTextField getJtfMessage() {
        return jtfMessage;
    }


    @SneakyThrows
    private Client enterLoginPassword() {
        client = new Client();
        String result = JOptionPane.showInputDialog("<html><h2>Введите логин");
        client.setUserName(result);
        String passwordFieldText = JOptionPane.showInputDialog("<html><h2>Введите пароль");
        client.setPassword(passwordFieldText.toCharArray());
        PasswordEncoding hashPass = new PasswordEncoding();
        client.setHashPass(hashPass.hashPassword(client.getPassword()));
        return client;
    }

    private void drawClientWindow() {
        setBounds(600, 300, 600, 500);
        setTitle("Client");
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
    }


    public void setTextAreaMessage(String message) {
        jtaTextAreaMessage.append(message);
    }

    public void setNumberOfClient(String message) {
        jlNumberOfClients.setText(message);
    }


    @SneakyThrows
    private void SendMessage(String message) {
        String sendMessage;
        BufferedWriter writer = new BufferedWriter(outMessage);
        if (message.equalsIgnoreCase("Exit")) {
            sendMessage = TypeMessage.LOGOUT.name();

        }else {
            sendMessage = TypeMessage.MESSAGE.name() + message;
        }
        log.info("sendMessage = " + sendMessage);
        writer.write(sendMessage);
        writer.newLine();
        writer.flush();

    }

}
