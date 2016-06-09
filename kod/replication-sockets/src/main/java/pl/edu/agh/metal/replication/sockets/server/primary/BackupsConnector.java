package pl.edu.agh.metal.replication.sockets.server.primary;

import pl.edu.agh.metal.replication.sockets.Config;
import pl.edu.agh.metal.replication.sockets.server.DataHolder;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class BackupsConnector implements Runnable {

    private ServerSocket receivingSocket;
    private List<Socket> backupSockets;
    private DataHolder dataHolder;

    public BackupsConnector(List<Socket> backupSockets, DataHolder dataHolder) {
        this.backupSockets = backupSockets;
        this.dataHolder = dataHolder;
    }

    public void run() {
        System.out.println("BackupsConnector::run Started!");
        try {
            receivingSocket = new ServerSocket(Config.PRIMARY_BACKUP_PORT);
            while (true) {
                Socket backupSocket = receivingSocket.accept();

                synchronize(backupSocket);

                backupSockets.add(backupSocket);
                System.out.println("BackupsConnector::run Backup accepted on port: " + backupSocket.getPort());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        } finally {
            System.out.println("BackupsConnector::run Stopped!");
        }

    }

    private void synchronize(Socket socket) throws IOException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        writer.append("synchronize ");
        for (int index = 0; index < 10; index++) {
            writer.append(dataHolder.read(index).toString() + " ");
        }
        writer.append("\n");
        writer.flush();
    }
}
