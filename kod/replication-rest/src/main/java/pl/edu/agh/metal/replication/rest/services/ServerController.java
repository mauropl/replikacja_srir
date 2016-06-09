package pl.edu.agh.metal.replication.rest.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import pl.edu.agh.metal.replication.rest.data.DataHolder;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/data")
@ConditionalOnProperty(value = "instance.type", havingValue = "backup")
public class ServerController {

    protected DataHolder dataHolder;

    protected Environment environment;

    @Autowired
    public ServerController(DataHolder dataHolder, Environment environment) {
        this.dataHolder = dataHolder;
        this.environment = environment;
    }

    @PostConstruct
    public void register() {
        RestTemplate template = new RestTemplate();

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
        headers.add("X-Backup", "127.0.0.1:" + environment.getProperty("server.port"));

        HttpEntity entity = new HttpEntity(headers);

        HttpEntity<List> response = template.exchange("http://127.0.0.1:10000/data", HttpMethod.GET, entity, List.class);

        dataHolder.writeAll(response.getBody());
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Integer> getData() {
        return dataHolder.readAll();
    }

    @RequestMapping(path = "/{index}", method = RequestMethod.GET)
    public Integer getData(@PathVariable Integer index) {
        return dataHolder.read(index);
    }

    @RequestMapping(method = RequestMethod.POST)
    public void postData(@RequestBody List<Integer> data) {
        dataHolder.writeAll(data);
    }

    @RequestMapping(path = "/{index}", method = RequestMethod.POST)
    public void getData(@PathVariable Integer index, @RequestBody Integer value) {
        dataHolder.write(index, value);
    }

}
