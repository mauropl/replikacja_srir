package pl.edu.agh.metal.replication.rest.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.AsyncRestTemplate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Klasa odpowiedzialna za pamiętanie listy zarejestowanych serwerów typu BACKUP, oraz propagowanie do nich żądań zapisu.
 *
 * @see org.springframework.web.client.RestTemplate
 */
@Component
@ConditionalOnProperty(value = "instance.type", havingValue = "primary")
public class PrimaryPropagator {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public static final String BACKUP_HEADER = "X-Backup";

    private Set<String> backupHosts = new HashSet<>();

    public void registerBackup(String host) {
        backupHosts.add(host);
    }

    public void propagateAllData(List<Integer> data) {

        List<ListenableFuture<?>> features = new ArrayList<>();

        for (String host : backupHosts) {
            AsyncRestTemplate template = new AsyncRestTemplate();
            String uri = "http://" + host + "/data";
            features.add(template.postForEntity(uri, new HttpEntity<>(data), List.class));
            logger.info("Propagating all data to {}", uri);
        }

        while (!isAllFeaturesDone(features)) {
        }
    }

    public void propagateOneData(Integer index, Integer value) {

        List<ListenableFuture<?>> features = new ArrayList<>();

        for (String host : backupHosts) {
            AsyncRestTemplate template = new AsyncRestTemplate();
            String uri = "http://" + host + "/data/" + index;
            features.add(template.postForEntity(uri, new HttpEntity<>(value), Integer.class));
            logger.info("Propagating one data to {}", uri);
        }

        while (!isAllFeaturesDone(features)) {
        }

    }

    public void unregisterBackup(String host) {
        backupHosts.remove(host);
    }

    private boolean isAllFeaturesDone(List<ListenableFuture<?>> features) {
        return features.stream().allMatch(ListenableFuture::isDone);
    }

}
