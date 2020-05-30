package game;

import util.DLList;

public class Game implements java.io.Serializable {

    private static final long serialVersionUID = 1203810L;

    public boolean isRunning = false;
    public final DLList<Player> players = new DLList<>();
    public String prompt = "";
    public int timer = 0;

    public String soundToPlay = "";

    public void copy(Game game) {
        this.isRunning = game.isRunning;
        this.prompt = game.prompt;
        this.timer = game.timer;

        //remove extra players
        DLList<Player> toRemove = new DLList<>();
        for (Player player : this.players) {
            if (!game.players.contains(player)) {
                toRemove.add(player);
            }
        }
        for (Player player : toRemove)
            this.players.remove(player);
        //add missing players and copy the rest.
        //this is pretty bad.
        for (Player player : game.players) {
            if (!this.players.contains(player)) {
                this.players.add(player);
            } else {
                this.players.get(this.players.indexOf(player)).copy(player);
            }
        }
    }

    public Player getPlayer(String username) {
        for (Player player : players) {
            if (player.username.equals(username))
                return player;
        }
        return new Player("");
    }

    public Player currentPlayer() {
        for (Player p : players)
            if (p.myTurn)
                return p;
        return null;
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
