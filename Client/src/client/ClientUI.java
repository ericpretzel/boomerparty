package client;

import game.Player;
import images.ImageManager;
import util.Globals;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class ClientUI extends JPanel {

    private final ClientManager client;

    private final JPanel playersPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

    private final JProgressBar timer = new JProgressBar(0, 10);

    private final JPanel textPanel;

    public ClientUI(ClientManager client) {
        super(new GridBagLayout());
        setPreferredSize(new Dimension(500, 400));
        setFocusable(true);

        this.client = client;

        this.addKeyListener(client.im);

        textPanel = client.im.getTextPanel();


        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = c.gridy = 0;
        c.weightx = 1;

        this.add(timer, c);

        c.gridy = 1;
        c.gridwidth = 2;
        c.weightx = c.weighty = 1;
        c.fill = GridBagConstraints.HORIZONTAL;

        this.add(playersPanel, c);

        c.gridy = 2;

        this.add(textPanel, c);

    }

    public void updateInfo() {
        if (client.game.isRunning && client.game.currentPlayer().username.equals(client.username)) {
            textPanel.setBorder(new TitledBorder("Your prompt: " + client.game.prompt));
        } else {
            textPanel.setBorder(null);
        }

        timer.setValue(client.game.timer);

        playersPanel.removeAll();
        for (Player player : client.game.players) {
            JPanel playerPanel = new JPanel(new GridBagLayout());
            playerPanel.setBorder(new EtchedBorder(EtchedBorder.RAISED));


            JLabel username = new JLabel(player.username +
                    (player.username.equals(client.username) ? " (You)" : ""));
            if (client.game.isRunning && player.equals(client.game.currentPlayer())) {
                username.setForeground(Color.green);
            } else {
                username.setForeground(Color.black);
            }

            JPanel health = new JPanel(new FlowLayout(FlowLayout.CENTER));
            health.setBorder(new TitledBorder("Health"));
            for (int i = 1; i <= 3; i++) {
                String path = (player.health >= i ? Globals.FULL_HEART_IMAGE : Globals.EMPTY_HEART_IMAGE);
                JLabel label = new JLabel(new ImageIcon(ImageManager.getImage(path)));
                health.add(label);
            }

            JLabel playedWord = new JLabel(player.playedWord.toUpperCase());

            GridBagConstraints c = new GridBagConstraints();
            c.gridx = c.gridy = 0;
            c.weightx = c.weighty = 1;
            c.fill = GridBagConstraints.BOTH;

            playerPanel.add(username, c);

            c.gridy = 1;
            c.weighty = 0;
            c.fill = GridBagConstraints.HORIZONTAL;

            playerPanel.add(health, c);

            c.gridy = 2;

            playerPanel.add(playedWord, c);

            playersPanel.add(playerPanel);
        }
        repaint();
        revalidate();
    }
}
