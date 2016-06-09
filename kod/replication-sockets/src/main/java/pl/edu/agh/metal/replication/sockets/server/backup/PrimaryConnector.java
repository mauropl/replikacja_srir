package pl.edu.agh.metal.replication.sockets.server.backup;

import pl.edu.agh.metal.replication.sockets.Config;
import pl.edu.agh.metal.replication.sockets.server.DataHolder;
import pl.edu.agh.metal.replication.sockets.server.ServerPropagator;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class PrimaryConnector implements Runnable {

    private DataHolder dataHolder;

    public PrimaryConnector(DataHolder dataHolder) {
        this.dataHolder = dataHolder;
    }

    public void run() {
        System.out.println("PrimaryConnector::run Started!");
        try {
            Socket receivingSocket = new Socket(Config.PRIMARY_HOST, Config.PRIMARY_BACKUP_PORT);
            BufferedReader reader = new BufferedReader(new InputStreamReader(receivingSocket.getInputStream()));

            String command;
            while ((command = reader.readLine()) != null) {
                System.out.println("PrimaryConnector::start Reading from " + receivingSocket.getPort() + ": " + command);
                if (Config.WRITE_PATTERN.matcher(command).matches()) {
                    write(command);
                }

                if (Config.SYNCHRONIZE_PATTERN.matcher(command).matches()) {
                    synchronize(command);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        } finally {
            System.out.println("PrimaryConnector::run Stopped!");
        }
    }

    private void write(String command) {
        String[] splited = command.split(" ");
        String index = splited[1];
        String value = splited[2];
        dataHolder.write(Integer.parseInt(index), Integer.parseInt(value));
    }

    private void synchronize(String command) {
        String[] splited = command.split(" ");

        for (int i = 1; i < 11; i++) {
            dataHolder.write(i - 1, Integer.parseInt(splited[i]));
        }
    }

}
