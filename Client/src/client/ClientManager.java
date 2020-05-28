package client;

import game.Game;
import game.Player;
import sounds.SoundManager;
import util.Logger;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientManager {

    protected final String username;
    protected final Game game = new Game();
    protected final InputManager im = new InputManager(this);
    private final String ip;
    private final int portNumber;
    private final ClientUI sc = new ClientUI(this);

    private ObjectOutputStream out;
    private ObjectInputStream in;

    public ClientManager(String username, String ip, int portNumber) {
        this.username = username;
        this.ip = ip;
        this.portNumber = portNumber;

        JFrame fr = new JFrame("BoomerParty (" + username + ")");
        fr.add(sc);
        fr.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if (JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?",
                        "Exit", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    disconnect();
                }
            }
        });
        fr.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        fr.pack();
        fr.setLocationRelativeTo(null);
        fr.setVisible(true);

        //game.players.add(new Player(this.username));
    }

    public void connect() throws IOException {
        Socket server = null;
        try {
            server = new Socket(ip, portNumber);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Could not connect to server.");
            System.exit(0);
        }

        Logger.log("successfully connected to server " + ip + " on port " + portNumber);

        out = new ObjectOutputStream(server.getOutputStream());
        in = new ObjectInputStream(server.getInputStream());

        Logger.log("sending username");
        send(username);

        Thread receiving = new Thread(() -> {
            while (true) {
                Game game = (Game) receive();
                //update game info
                this.game.copy(game);

                SoundManager.playSound(game.soundToPlay);

                SwingUtilities.invokeLater(sc::updateInfo);
            }

        });
        receiving.start();
    }

    public void disconnect() {
        try {
            out.close();
            in.close();
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(String word) {
        try {
            out.writeUTF(word);
            Logger.log("sent: " + word);
            out.reset();
            out.flush();
        } catch (IOException e) {
            Logger.log("IO error occurred when trying to send word");
            e.printStackTrace();
        }
    }

    public Object receive() {
        try {
            Object obj = in.readObject();
            Logger.log("received: " + obj.toString());
            return obj;
        } catch (IOException e) {
            Logger.log("IO error occurred when trying to receive game");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            Logger.log("can't find class");
            e.printStackTrace();
        }
        return null;
    }
}
