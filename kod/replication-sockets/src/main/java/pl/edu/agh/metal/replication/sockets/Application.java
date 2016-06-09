package pl.edu.agh.metal.replication.sockets;

import pl.edu.agh.metal.replication.sockets.client.ClientRequester;
import pl.edu.agh.metal.replication.sockets.server.backup.PrimaryConnector;
import pl.edu.agh.metal.replication.sockets.server.primary.ClientsConnector;
import pl.edu.agh.metal.replication.sockets.server.DataHolder;
import pl.edu.agh.metal.replication.sockets.server.primary.BackupsConnector;
import pl.edu.agh.metal.replication.sockets.server.primary.BackupsPropagator;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Application {

    public static final void main(String[] args) {

        DataHolder dataHolder = new DataHolder();

        if (args[0].equals("primary")) {

            List<Socket> backupSockets = Collections.synchronizedList(new ArrayList());

            BackupsConnector backupsConnector = new BackupsConnector(backupSockets, dataHolder);
            new Thread(backupsConnector).start();

            ClientsConnector clientsConnector = new ClientsConnector(dataHolder, new BackupsPropagator(backupSockets));
            new Thread(clientsConnector).start();

        } else if (args[0].equals("backup")) {

            PrimaryConnector primaryConnector = new PrimaryConnector(dataHolder);
            new Thread(primaryConnector).start();

        } else if (args[0].equals("client")) {

            ClientRequester clientRequester = new ClientRequester();
            new Thread(clientRequester).start();

        }


    }

}
