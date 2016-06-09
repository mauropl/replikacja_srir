package pl.edu.agh.metal.replication.sockets.server.primary;

import pl.edu.agh.metal.replication.sockets.Config;
import pl.edu.agh.metal.replication.sockets.server.DataHolder;
import pl.edu.agh.metal.replication.sockets.server.ServerPropagator;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientsConnector implements Runnable {

    private DataHolder dataHolder;
    private ServerPropagator serverPropagator;

    public ClientsConnector(DataHolder dataHolder, ServerPropagator serverPropagator) {
        this.dataHolder = dataHolder;
        this.serverPropagator = serverPropagator;
    }

    public void run() {
        System.out.println("ClientsConnector::run Started!");
        try {
            ServerSocket receivingSocket = new ServerSocket(Config.PRIMARY_CLIENT_PORT);
            while (true) {
                Socket clientSocket = receivingSocket.accept();
                System.out.println("ClientsConnector::run Client accepted on port: " + clientSocket.getPort());

                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                String command = reader.readLine();
                System.out.println("ClientsConnector::start Reading from " + clientSocket.getPort() + ": " + command);

                if (Config.WRITE_PATTERN.matcher(command).matches()) {
                    String[] splited = command.split(" ");
                    String index = splited[1];
                    String value = splited[2];
                    dataHolder.write(Integer.parseInt(index), Integer.parseInt(value));
                }

                serverPropagator.propagate(command);


            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        } finally {
            System.out.println("BackupsConnector::run Stopped!");
        }

    }

}
