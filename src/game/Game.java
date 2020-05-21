package game;

import util.DLList;

public class Game implements java.io.Serializable {

    private static final long serialVersionUID = 1203810L;

    public boolean isRunning = false;
    public DLList<Player> players = new DLList<>();
    public String prompt = "";
    public int timer = 0;

    public String soundToPlay = "";

    public Player getPlayer(String username) {
        for (Player p : players) {
            if (p.username.equals(username))
                return p;
        }
        return null;
    }

    public Player currentPlayer() {
        return players.get(0);
    }

    @Override
    public String toString() {
        return "Game{" +
                "isRunning=" + isRunning +
                ", players=" + players +
                ", prompt='" + prompt + '\'' +
                ", timer=" + timer +
                '}';
    }
}
