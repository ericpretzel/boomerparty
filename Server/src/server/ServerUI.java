package server;

import util.Logger;

import javax.swing.*;
import java.awt.*;

public class ServerUI extends JPanel {

    public ServerUI(ServerThread server) {
        super(new GridBagLayout());

        JButton startGameButton = new JButton("Start Game");
        startGameButton.addActionListener(l -> {
            if (server.gm.game.players.size() < 2) {
                JOptionPane.showMessageDialog(null, "You need at least two players to start!");
            } else {
                new Thread(server.gm).start();
            }
        });

        JScrollPane pane = Logger.scrollPane();

        GridBagConstraints c = new GridBagConstraints();

        c.gridx = c.gridy = 0;
        c.weightx = c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;

        this.add(pane, c);

        c.gridx = 1;
        c.weighty = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        this.add(startGameButton, c);
    }
}
