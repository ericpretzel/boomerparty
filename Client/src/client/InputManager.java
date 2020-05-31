package client;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class InputManager extends KeyAdapter {

    private final JPanel textPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    private final ClientManager client;
    private String word = "";
    private boolean enabled = false;

    public InputManager(ClientManager client) {
        textPanel.setBorder(new EtchedBorder(EtchedBorder.RAISED));
        textPanel.setPreferredSize(textPanel.getSize());
        this.client = client;
    }

    public void keyPressed(KeyEvent e) {
        //check if you should be able to type...
        if (!enabled) return;

        //on letter press: add to the current play.
        if (Character.isAlphabetic(e.getKeyChar())) {
            String letter = String.valueOf(e.getKeyChar()).toUpperCase();
            word = word.concat(letter);

            //create label
            JLabel label = new JLabel(letter);
            label.setFont(new Font(Font.DIALOG, Font.BOLD, 15));
            label.setHorizontalAlignment(JLabel.CENTER);

            textPanel.add(label);
        }

        //on enter: sends the word for the game to check if it is valid or not.
        else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            word = word.concat("!");
        }

        //on backspace: remove letter
        else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            if (word.length() > 0) {
                word = word.substring(0, word.length() - 1);
                textPanel.remove(textPanel.getComponents().length - 1);
            }
        }

        client.send(word);
        if (word.length() > 0 && word.charAt(word.length() - 1) == '!') {
            word = "";
            textPanel.removeAll();
        }

        //make the letters containing the prompt turn green.
        int promptIndex = word.indexOf(client.game.prompt);
        for (int i = 0; i < word.length(); i++) {
            if (promptIndex >= 0 && i < promptIndex + client.game.prompt.length() && i >= promptIndex) {
                textPanel.getComponent(i).setForeground(Color.green);
            } else {
                textPanel.getComponent(i).setForeground(Color.black);
            }
        }
        textPanel.revalidate();
        textPanel.repaint();
    }

    public void disable() {
        if (enabled) {
            enabled = false;
            textPanel.setBorder(null);
            textPanel.removeAll();
            word = "";
        }
    }

    public void enable() {
        if (!enabled) {
            enabled = true;
            textPanel.setBorder(new TitledBorder("Type an English word containing: " + client.game.prompt));
        }
    }

    public JPanel getTextPanel() {
        return textPanel;
    }
}
