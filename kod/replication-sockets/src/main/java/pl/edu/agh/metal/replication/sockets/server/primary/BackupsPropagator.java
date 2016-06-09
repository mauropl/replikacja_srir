package pl.edu.agh.metal.replication.sockets.server.primary;

import pl.edu.agh.metal.replication.sockets.server.ServerPropagator;

import java.net.Socket;
import java.util.List;

/**
 * Klasa pozwala propagować komendę od klienta do innych BACKUPÓW. Dla każdego BACKUPU tworzone są osobne wątki.
 *
 * @see Propagation
 */
public class BackupsPropagator implements ServerPropagator {

    private List<Socket> sockets;

    public BackupsPropagator(List<Socket> sockets) {
        this.sockets = sockets;
    }

    public void propagate(String command) {
        for (Socket socket : sockets) {
            new Thread(new Propagation(socket, command)).start();
        }
    }
}
