package pl.edu.agh.metal.replication.sockets.server.primary;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Propagation implements Runnable {

    private final Socket backupSocket;
    private final String command;

    public Propagation(Socket backupSocket, String command) {
        this.backupSocket = backupSocket;
        this.command = command;
    }

    public void run() {
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(backupSocket.getOutputStream()));
            writer.write(command + "\n");
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
