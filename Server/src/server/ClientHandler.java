package server;

import util.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler {

    public final String username;
    private final Socket socket;
    private final ObjectOutputStream out;
    private final ObjectInputStream in;

    ClientHandler(Socket socket) throws IOException {
        this.socket = socket;
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
        //this.send(new Game());
        this.username = this.receive();
    }

    public void disconnect() {
        Logger.log("Client \"" + username + "\" disconnected");
        try {
            out.close();
            in.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String receive() {
        try {
            return in.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void send(Object o) {
        try {
            out.writeObject(o);
            Logger.log("Sent: " + o);
            out.reset();
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}