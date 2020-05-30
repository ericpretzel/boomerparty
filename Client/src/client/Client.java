package client;

import game.ClientInstructions;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.io.IOException;

public class Client {
    public static void main(String[] args) throws IOException {
        ClientInstructions.show();

        JPanel clientPanel = new JPanel();

        JTextField usernameInput = new JTextField(15);
        usernameInput.setBorder(new TitledBorder("Username"));
        clientPanel.add(usernameInput);

        JTextField portInput = new JTextField("1024", 15);
        portInput.setBorder(new TitledBorder("Port"));
        clientPanel.add(portInput);

        JTextField ipInput = new JTextField("127.0.0.1", 15);
        ipInput.setBorder(new TitledBorder("IP"));
        clientPanel.add(ipInput);

        if (JOptionPane.showConfirmDialog(null, clientPanel, "client", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            String username = usernameInput.getText();
            String ip = ipInput.getText();
            int port = -1;
            try {
                port = Integer.parseInt(portInput.getText());
            } catch (Exception e) {
                showErrorPane();
                e.printStackTrace();
            }

            if (!ip.matches("^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$")) {
                showErrorPane();
            }

            new ClientManager(username, ip, port).connect();
        } else {
            System.exit(0);
        }
    }

    private static void showErrorPane() {
        JOptionPane.showMessageDialog(null, "Make sure the port and IP are valid.");
    }
}
