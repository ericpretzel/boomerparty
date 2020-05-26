package client;

import game.Player;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class ClientUI extends JPanel {

    private final ClientManager client;

    private final JPanel playersPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

    private final JProgressBar timer = new JProgressBar(0, 10);

    private final JPanel textPanel;

    private final JLabel promptLabel;

    public ClientUI(ClientManager client) {
        super(new GridBagLayout());
        setPreferredSize(new Dimension(500, 400));
        setFocusable(true);

        this.client = client;

        this.addKeyListener(client.im);

        this.textPanel = client.im.getTextPanel();

        promptLabel = new JLabel();


        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = c.gridy = 0;
        c.weightx = 1;
        c.weighty = 0;

        this.add(timer, c);

        c.gridy = 1;
        c.anchor = GridBagConstraints.CENTER;
        c.weighty = 1;

        this.add(promptLabel, c);

        c.gridy = 2;
        c.fill = GridBagConstraints.BOTH;

        this.add(playersPanel, c);

        c.gridy = 3;

        this.add(textPanel, c);

    }

    public void updateInfo() {
        if (client.game.isRunning && client.game.currentPlayer().username.equals(client.username)) {
            textPanel.setBorder(new TitledBorder("Type an English word containing: " + client.game.prompt));
        } else {
            textPanel.setBorder(null);
        }

        timer.setValue(client.game.timer);

        promptLabel.setText(client.game.prompt);

        playersPanel.removeAll();
        for (Player player : client.game.players) {
            playersPanel.add(player.getPanel());
            player.updatePanel();
        }
        repaint();
        revalidate();
    }
}
