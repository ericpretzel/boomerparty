package server;

import javax.swing.*;

public class Server {
    public static void main(String[] args) {
        JPanel portPanel = new JPanel();

        portPanel.add(new JLabel("Port:"));

        JTextField portInput = new JTextField(5);
        portInput.setText("1024");
        portPanel.add(portInput);

        if (JOptionPane.showConfirmDialog(null, portPanel,
                "Enter server port number", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            int portNumber = Integer.parseInt(portInput.getText());
            new ServerThread(portNumber).start();
        } else {
            System.exit(0);
        }
    }
}