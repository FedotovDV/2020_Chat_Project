package frame;

import client.Client;
import lombok.SneakyThrows;

import utility.PasswordEncoding;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class LoginForm extends JDialog implements ActionListener {

    private Client client;
    String userText;
    Container container = getContentPane();
    JLabel userLabel = new JLabel("USERNAME");
    JLabel passwordLabel = new JLabel("PASSWORD");
    JTextField userTextField = new JTextField();
    JPasswordField passwordField = new JPasswordField();
    JButton loginButton = new JButton("LOGIN");
    JButton resetButton = new JButton("RESET");
    JCheckBox showPassword = new JCheckBox("Show Password");

    public LoginForm(JFrame owner) throws HeadlessException {
        super(owner);

        this.setTitle("Login Form");
        this.setVisible(true);
        this.setBounds(100, 100, 360, 280);
        this.setLocationRelativeTo(owner);
        this.setModalityType(ModalityType.TOOLKIT_MODAL);
        this.setDefaultCloseOperation(HIDE_ON_CLOSE);
        this.setResizable(false);
        setLayoutManager();
        setLocationAndSize();
        addComponentsToContainer();
        addActionEvent();
    }

    public String getUserText() {
        return userText;
    }

    public Client getClient() {
        return client;
    }

    public void setLayoutManager() {
        container.setLayout(null);
    }

    public void setLocationAndSize() {

        userLabel.setBounds(50, 30, 100, 30);
        passwordLabel.setBounds(50, 100, 100, 30);
        userTextField.setBounds(150, 30, 150, 30);
        passwordField.setBounds(150, 100, 150, 30);
        showPassword.setBounds(150, 130, 150, 30);
        loginButton.setBounds(50, 180, 100, 30);
        resetButton.setBounds(200, 180, 100, 30);



    }

    public void addComponentsToContainer() {
        container.add(userLabel);
        container.add(passwordLabel);
        container.add(userTextField);
        container.add(passwordField);
        container.add(showPassword);
        container.add(loginButton);
        container.add(resetButton);
    }

    public void addActionEvent() {
        loginButton.addActionListener(this);
        resetButton.addActionListener(this);
        showPassword.addActionListener(this);
    }


    @SneakyThrows
    @Override
    public void actionPerformed(ActionEvent event) {

        client = new Client();
        if (event.getSource() == loginButton) {

            userText = userTextField.getText();
            client.setUserName(userText);
            String passwordFieldText =new String(passwordField.getPassword());
            client.setPassword(passwordFieldText.toCharArray());
            PasswordEncoding hashPass = new PasswordEncoding();
            client.setHashPass(hashPass.hashPassword(client.getPassword()));

            if (client.getUserName()!=null && client.getPassword()!=null) {
                JOptionPane.showMessageDialog(this, "Login Successful");

                this.setVisible(false);
//                ClientWindow clientWindow = new ClientWindow(client);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Username or Password");
            }
        }

        if (event.getSource() == resetButton) {
            userTextField.setText("");
            passwordField.setText("");
        }

        if (event.getSource() == showPassword) {
            if (showPassword.isSelected()) {
                passwordField.setEchoChar((char) 0);
            } else {
                passwordField.setEchoChar('*');
            }

        }
    }


}
