package client;

import game.Player;
import util.Globals;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class ClientUI extends JPanel {

    private final ClientManager client;

    private final JPanel playersPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

    private final JProgressBar timer = new JProgressBar(0, 10);

    private final JLabel promptLabel = new JLabel();

    private final JPanel bonusLettersPanel = new JPanel(new GridLayout(0, 2));

    public ClientUI(ClientManager client) {
        super(new GridBagLayout());
        setPreferredSize(new Dimension(500, 400));
        setFocusable(true);

        this.client = client;

        this.addKeyListener(client.im);

        JPanel textPanel = client.im.getTextPanel();

        playersPanel.setBorder(new EtchedBorder(EtchedBorder.RAISED));

        promptLabel.setBorder(new TitledBorder("Current Prompt"));
        promptLabel.setHorizontalAlignment(SwingConstants.CENTER);

        bonusLettersPanel.setBorder(new TitledBorder("Bonus Letters"));
        Globals.BONUS_LETTERS.chars().forEachOrdered(c -> {
            JLabel label = new JLabel(String.valueOf((char)c));
            label.setOpaque(true);
            label.setBackground(Color.lightGray);
            label.setForeground(Color.black);
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setBorder(new EtchedBorder(EtchedBorder.RAISED));
            bonusLettersPanel.add(label);
        });

        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = c.gridy = 0;
        c.weightx = 1;
        c.weighty = 0;

        this.add(timer, c);

        c.gridy = 1;
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.BOTH;
        c.weighty = 1;

        this.add(promptLabel, c);

        c.gridy = 2;

        this.add(playersPanel, c);

        c.gridy = 3;

        this.add(textPanel, c);

        c.gridx = 1;
        c.gridy = 0;
        c.gridheight = 4;

        this.add(bonusLettersPanel, c);
    }

    public void updateInfo() {
        final Player myPlayer = client.game.getPlayer(client.username);

        if (client.game.isRunning && myPlayer.myTurn) {
            client.im.enable();
        } else {
            client.im.disable();
        }

        timer.setValue(client.game.timer);

        promptLabel.setText(client.game.prompt);

        playersPanel.removeAll();
        for (Player player : client.game.players) {
            playersPanel.add(player.getPanel());
            player.updatePanel();
        }

        for (int i = 0; i < Globals.BONUS_LETTERS.length(); i++) {
            String letter = String.valueOf(Globals.BONUS_LETTERS.charAt(i));
            boolean containsLetter = myPlayer.bonusLetters.contains(letter);
            Component label = bonusLettersPanel.getComponent(i);
            label.setBackground(containsLetter ? Color.darkGray : Color.lightGray);
            label.setForeground(containsLetter ? Color.lightGray : Color.black);
        }

        repaint();
        revalidate();
    }
}
