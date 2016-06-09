package pl.edu.agh.metal.replication.sockets.server;

import java.util.Arrays;
import java.util.List;

public class DataHolder {

    private List<Integer> integers = Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0);

    public Integer read(Integer index) {
        return integers.get(index);
    }

    public void write(Integer index, Integer value) {
        integers.set(index, value);
        System.out.println(integers);
    }
}
