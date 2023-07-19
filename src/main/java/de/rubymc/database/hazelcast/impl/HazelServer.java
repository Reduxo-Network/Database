package de.rubymc.database.hazelcast.impl;

import com.google.gson.Gson;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.EntryListener;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hazelcast.topic.ITopic;
import de.rubymc.database.hazelcast.IServer;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Implementation of the {@link IServer} interface using Hazelcast for distributed map operations.
 *
 * @param <K> The type of keys in the distributed maps.
 * @param <V> The type of values in the distributed maps.
 */
public class HazelServer<K, V> implements IServer<K, V> {

    private final HazelcastInstance hazelcastInstance;
    private final Gson gson;

    /**
     * Constructs a new HazelServer with the provided address and cluster name.
     *
     * @param address The address of the Hazelcast cluster to connect to.
     * @param field   The name of the cluster to join.
     */
    public HazelServer(String address, String field) {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setClusterName(field);
        clientConfig.getNetworkConfig().addAddress(address);
        this.hazelcastInstance = HazelcastClient.newHazelcastClient(clientConfig);
        this.gson = new Gson();
    }

    /**
     * Creates a new HazelServer instance with the provided address and cluster name.
     *
     * @param <K>     The type of keys in the distributed maps.
     * @param <V>     The type of values in the distributed maps.
     * @param address The address of the Hazelcast cluster to connect to.
     * @param field   The name of the cluster to join.
     * @return A new instance of HazelServer with the specified configuration.
     */
    public static <K, V> HazelServer<K, V> create(String address, String field) {
        return new <K, V>HazelServer<K, V>(address, field);
    }

    @Override
    public boolean containsKey(String field, K key) {
        return hazelcastInstance.getMap(field).containsKey(key);
    }

    @Override
    public void setMap(String field, K key, V value) {
        Map<K, V> map = hazelcastInstance.getMap(field);
        map.put(key, value);
    }

    @Override
    public void setCacheMap(String field, K key, V value, int seconds) {
        IMap<K, V> map = hazelcastInstance.getMap(field);
        map.put(key, value, seconds, TimeUnit.SECONDS);
    }

    @Override
    public void setCacheMap(String field, K key, V value, int seconds, EntryListener<K, V> entryListener) {
        IMap<K, V> map = hazelcastInstance.getMap(field);
        map.put(key, value, seconds, TimeUnit.SECONDS);
        map.addEntryListener(entryListener, true);
    }

    @Override
    public void removeMap(String field, K key) {
        Map<K, V> map = hazelcastInstance.getMap(field);
        map.remove(key);
    }

    @Override
    public Object mapGet(String field, K key, Class<?> clazz) {
        Map<K, V> map = hazelcastInstance.getMap(field);
        String jsonValue = String.valueOf(map.get(key));
        return gson.fromJson(jsonValue, clazz);
    }

    @Override
    public Object mapGet(String field, K key) {
        Map<K, V> map = hazelcastInstance.getMap(field);
        return map.get(key);
    }

    @Override
    public ITopic<String> topicRegister(String field) {
        return hazelcastInstance.getTopic(field);
    }

    @Override
    public Set<K> mapKeys(String field) {
        IMap<K, V> map = hazelcastInstance.getMap(field);
        return map.keySet();
    }

    @Override
    public Map<K, V> mapValue(String field) {
        IMap<K, V> map = hazelcastInstance.getMap(field);
        return Map.copyOf(map);
    }

    @Override
    public void shutdown() {
        this.hazelcastInstance.shutdown();
    }
}