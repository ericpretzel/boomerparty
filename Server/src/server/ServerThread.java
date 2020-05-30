package server;

import game.GameManager;
import game.Player;
import util.Logger;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class ServerThread extends Thread {

    protected final int portNumber;
    protected final GameManager gm = new GameManager();

    public ServerThread(int portNumber) throws UnknownHostException {
        this.portNumber = portNumber;

        JFrame fr = new JFrame("Server (port " + portNumber + ")");

        ServerUI display = new ServerUI(this);
        fr.add(display);
        fr.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if (JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?",
                        "Exit", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });
        fr.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        fr.pack();
        fr.setLocationRelativeTo(null);
        fr.setVisible(true);

        Logger.log("new server started on port " + portNumber + " with ip " + InetAddress.getLocalHost().getHostAddress());
    }

    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                ClientHandler client = new ClientHandler(socket);

                //check for duplicate usernames.
                boolean duplicate = false;
                for (Player player : gm.clients.getKeys()) {
                    if (player.username.equals(client.username)) {
                        duplicate = true;
                        Logger.log("a client attempted to connect with duplicate username: \"" + client.username + "\"");
                        break;
                    }
                }
                if (!duplicate) {
                    Logger.log("new client connection: " + client.toString());
                    gm.addPlayer(client);
                } else {
                    client.send(gm.game);
                    client.disconnect();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
