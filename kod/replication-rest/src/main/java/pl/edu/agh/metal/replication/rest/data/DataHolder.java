package pl.edu.agh.metal.replication.rest.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Klasa odpowiada za przetrzymywanie danych, które będą replkowane. Dodatkowo udostępnia interfejs do dostępu do nich.
 * W naszym przypadku jest to tablica 10 liczb.
 */
@Component
public class DataHolder {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private List<Integer> data = Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0);

    public Integer read(Integer index) {
        return data.get(index);
    }

    public void write(Integer index, Integer value) {
        data.set(index, value);
        logger.info("{}", data);
    }

    public void writeAll(List<Integer> data) {
        this.data = new ArrayList<>(data);
        logger.info("{}", data);
    }

    public List<Integer> readAll() {
        return new ArrayList<>(data);
    }

}
