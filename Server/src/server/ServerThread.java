package server;

import game.GameManager;
import game.Player;
import util.Logger;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerThread extends Thread {

    protected final int portNumber;
    protected final GameManager gm = new GameManager();
    private final ServerUI display = new ServerUI(this);

    public ServerThread(int portNumber) {
        this.portNumber = portNumber;

        JFrame fr = new JFrame("Server (port " + portNumber + ")");
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

        Logger.log("New server started on port " + portNumber);
    }

    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                Logger.log("New connection...");
                ClientHandler client = new ClientHandler(socket);

                //check for duplicate usernames.
                boolean duplicate = false;
                for (Player player : gm.game.players) {
                    if (player.username.equals(client.username)) {
                        duplicate = true;
                        Logger.log("Client has duplicate username: \"" + client.username + "\"");
                        break;
                    }
                }
                if (!duplicate) {
                    Logger.log("Player joined as \"" + client.username + "\"");
                    gm.addPlayer(client);
                } else {
                    client.disconnect();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
