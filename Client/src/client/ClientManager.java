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
                    System.exit(0);
                }
            }
        });
        fr.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        fr.pack();
        fr.setLocationRelativeTo(null);
        fr.setVisible(true);

        game.players.add(new Player(this.username));
    }

    public void connect() throws IOException {
        Socket server = new Socket(ip, portNumber);
        Logger.log("Connected");

        out = new ObjectOutputStream(server.getOutputStream());
        in = new ObjectInputStream(server.getInputStream());
        send(username);

        Thread receiving = new Thread(() -> {
            while (true) {
                Logger.log("Waiting for game...");
                Game game = (Game) receive();
                Logger.log(game.toString());
                //update game info
                this.game.players = game.players;
                this.game.prompt = game.prompt;
                this.game.timer = game.timer;
                this.game.isRunning = game.isRunning;

                SoundManager.playSound(game.soundToPlay);

                sc.updateInfo();
            }
        });
        receiving.start();
    }

    public void disconnect() {
        try {
            out.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(String word) {
        try {
            out.writeUTF(word);
            Logger.log("Sent " + word);
            out.reset();
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Object receive() {
        try {
            return in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
