package server;

import util.Logger;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerUI extends JPanel {

    public ServerUI(ServerThread server) {
        super(new GridBagLayout());

        ExecutorService gameThread = Executors.newSingleThreadExecutor();

        JButton startGameButton = new JButton("Start Game");
        JButton hintButton = new JButton("Show Hint");
        JLabel hintLabel = new JLabel();
        hintLabel.setHorizontalAlignment(SwingConstants.CENTER);
        hintLabel.setBorder(new TitledBorder(""));

        startGameButton.addActionListener(l -> {
            if (server.gm.clients.getKeys().size() < 2) {
                JOptionPane.showMessageDialog(null, "You need at least two clients connected to start!");
            } else if (server.gm.game.isRunning) {
                JOptionPane.showMessageDialog(null, "The game is already running!");
            } else {
                gameThread.execute(server.gm);
            }
        });

        hintButton.addActionListener(l -> {
            if (server.gm.game.isRunning) {
                hintLabel.setText(server.gm.getHint());
                ((TitledBorder) hintLabel.getBorder()).setTitle("Hint for prompt: " + server.gm.game.prompt);
            }
        });


        JScrollPane pane = Logger.scrollPane();

        GridBagConstraints c = new GridBagConstraints();

        c.gridx = c.gridy = 0;
        c.weightx = c.weighty = 1;
        c.gridheight = 3;
        c.fill = GridBagConstraints.BOTH;

        this.add(pane, c);


        c.gridheight = 1;
        c.gridx = 1;
        c.weighty = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        this.add(startGameButton, c);

        c.gridy = 1;
        this.add(hintButton, c);

        c.weightx = c.weighty = 1;
        c.gridy = 2;
        this.add(hintLabel, c);
    }
}
