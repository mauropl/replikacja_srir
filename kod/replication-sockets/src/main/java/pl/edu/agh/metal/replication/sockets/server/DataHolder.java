package pl.edu.agh.metal.replication.sockets.server;

import java.util.Arrays;
import java.util.List;
/**
 * Klasa odpowiada za przetrzymywanie danych, które będą replkowane. Dodatkowo udostępnia interfejs do dostępu do nich.
 * W naszym przypadku jest to tablica 10 liczb.
 */
public class DataHolder {

    private List<Integer> integers = Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0);

    public synchronized Integer read(Integer index) {
        return integers.get(index);
    }

    public synchronized void write(Integer index, Integer value) {
        integers.set(index, value);
        System.out.println(integers);
    }
}
