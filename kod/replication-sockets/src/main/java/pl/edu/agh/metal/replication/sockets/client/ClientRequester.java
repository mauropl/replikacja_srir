package pl.edu.agh.metal.replication.sockets.client;

import pl.edu.agh.metal.replication.sockets.Config;

import java.io.*;
import java.net.Socket;
/**
 Klasa wyświetla w konsoli zapytanie do uzytkownika jaką komendę chce wysłać na serwer.
 write, read lub exit. Gdy użytkownik wpisze komendę tworzony jest socket i komenda wysyłana do serwera Primary.
 */
public class ClientRequester implements Runnable {

    private Socket socket;

    public void run() {

        try {
            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                System.out.println("Podaj komendę: (exit kończy)");
                socket = new Socket(Config.PRIMARY_HOST, Config.PRIMARY_CLIENT_PORT);
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                try {
                    String command = consoleReader.readLine();
                    if (Config.EXIT_PATTERN.matcher(command).matches()) {
                        exit();
                    }

                    if (Config.WRITE_PATTERN.matcher(command).matches()) {
                        send(writer, command);
                    }

                    if (Config.READ_PATTERN.matcher(command).matches()) {
                        send(writer, command);
                    }
                socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    System.exit(1);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void exit() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    private void send(BufferedWriter writer, String command) throws IOException {
        writer.write(command);
        writer.flush();
    }

}
