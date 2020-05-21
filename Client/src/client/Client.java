package client;

import game.Instructions;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.io.IOException;

public class Client {
    public static void main(String[] args) throws IOException {
        Instructions.show();

        JPanel clientPanel = new JPanel();

        JTextField usernameInput = new JTextField(15);
        usernameInput.setBorder(new TitledBorder("Username"));
        clientPanel.add(usernameInput);

        JTextField portInput = new JTextField("1024", 15);
        portInput.setBorder(new TitledBorder("Port"));
        clientPanel.add(portInput);

        JTextField ipInput = new JTextField("localhost", 15);
        ipInput.setBorder(new TitledBorder("IP"));
        clientPanel.add(ipInput);

        if (JOptionPane.showConfirmDialog(null, clientPanel, "client", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            new ClientManager(usernameInput.getText(), ipInput.getText(), Integer.parseInt(portInput.getText())).connect();
        } else {
            System.exit(0);
        }
    }
}
