package game;

import images.ImageManager;
import util.Globals;
import util.Tree;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.util.Objects;

public class Player implements java.io.Serializable {

    public final String username;
    public int health;
    public String playedWord;
    public boolean myTurn;
    public Tree<String> bonusLetters;

    private transient JPanel panel = null;
    private transient JLabel usernameLabel;
    private transient JPanel healthPanel;
    private transient JLabel playedWordLabel;

    public JPanel getPanel() {
        if (panel == null) {
            panel = new JPanel(new GridLayout(0, 1));
            panel.setBorder(new EtchedBorder(EtchedBorder.RAISED));

            usernameLabel = new JLabel(this.username);
            usernameLabel.setHorizontalAlignment(SwingConstants.CENTER);

            healthPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

            playedWordLabel = new JLabel(playedWord);
            playedWordLabel.setHorizontalAlignment(SwingConstants.CENTER);

            /*GridBagConstraints c = new GridBagConstraints();
            c.gridx = c.gridy = 0;
            c.weightx = c.weighty = 1;
            c.fill = GridBagConstraints.BOTH;*/

            panel.add(usernameLabel/*, c*/);

            /*c.gridy = 1;
            c.weighty = 0;
            c.fill = GridBagConstraints.HORIZONTAL;*/

            panel.add(healthPanel/*, c*/);

            /*c.gridy = 2;
            c.weighty = 1;*/
            panel.add(playedWordLabel/*, c*/);

            updatePanel();
        }
        return panel;
    }

    public void updatePanel() {
        if (myTurn) {
            usernameLabel.setForeground(Color.green);
        } else {
            usernameLabel.setForeground(Color.black);
        }

        healthPanel.removeAll();
        for (int i = 1; i <= 3; i++) {
            String path = (this.health >= i ? Globals.FULL_HEART_IMAGE : Globals.EMPTY_HEART_IMAGE);
            JLabel label = new JLabel(new ImageIcon(ImageManager.getImage(path)));
            healthPanel.add(label);
        }

        playedWordLabel.setText(this.playedWord);
    }

    public void copy(Player player) {
        this.health = player.health;
        this.playedWord = player.playedWord;
        this.myTurn = player.myTurn;
        this.bonusLetters = player.bonusLetters;
    }

    public String toString() {
        return "Player{" +
                "username='" + username + '\'' +
                ", health=" + health +
                ", playedWord='" + playedWord + '\'' +
                ", myTurn=" + myTurn +
                ", bonusLetters=" + bonusLetters +
                '}';
    }

    public Player(String username) {
        this.username = username;
        this.health = 2;
        this.playedWord = "";
        this.myTurn = false;
        bonusLetters = new Tree<>();
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(player.username, this.username);
    }

    public int hashCode() {
        return username.hashCode();
    }

}
