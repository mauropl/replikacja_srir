package pl.edu.agh.metal.replication.rest.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.metal.replication.rest.data.DataHolder;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * Kontroler udostępniający uslugi na serwerze typu PRIMARY. Najważniejszą jego cechą jest korzystanie z klasy
 * propagatora, dzięki czemu w raz z nadejściem żądania od klienta natychmiast jest ono propagowane do zarejestrowanych
 * wcześniej serwerów typu BACKUP. Rejestracja odbywa się poprzez sprawdzenie nagłówka X-Backup przy żądaniu synchronizacyjnym
 * od serwera typu BACKUP
 *
 * Bean jest tworzony zaleznie od parametru instance.type
 *
 * @see PrimaryPropagator
 * @see DataHolder
 */
@RestController
@RequestMapping(path = "/data")
@ConditionalOnProperty(value = "instance.type", havingValue = "primary")
public class PrimaryController {

    protected DataHolder dataHolder;

    protected PrimaryPropagator primaryPropagator;

    @Autowired
    public PrimaryController(DataHolder dataHolder, PrimaryPropagator primaryPropagator) {
        this.dataHolder = dataHolder;
        this.primaryPropagator = primaryPropagator;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Integer> getAllData(HttpServletRequest request) {
        if (request.getHeader(PrimaryPropagator.BACKUP_HEADER) != null) {
            primaryPropagator.registerBackup(request.getHeader(PrimaryPropagator.BACKUP_HEADER));
        }
        return dataHolder.readAll();
    }

    @RequestMapping(path = "/{index}", method = RequestMethod.GET)
    public Integer getOneData(@PathVariable Integer index) {
        return dataHolder.read(index);
    }

    @RequestMapping(method = RequestMethod.POST)
    public void postAllData(@RequestBody List<Integer> data) {
        primaryPropagator.propagateAllData(data);
        dataHolder.writeAll(data);
    }

    @RequestMapping(path = "/{index}", method = RequestMethod.POST)
    public void postOneData(@PathVariable Integer index, @RequestBody Integer value) {
        primaryPropagator.propagateOneData(index, value);
        dataHolder.write(index, value);
    }

}
