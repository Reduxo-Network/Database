package de.rubymc.database.hazelcast.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
    /**
     * Checks whether the specified key exists in the map associated with the given field.
     *
     * @param field The name of the map to check.
     * @param key   The key to be checked for existence.
     * @return True if the key is present in the map, otherwise false.
     */
    @Override
    public boolean containsKey(String field, K key) {
        return hazelcastInstance.getMap(field).containsKey(key);
    }

    /**
     * Sets the value for the given key in the map associated with the specified field.
     *
     * @param field The name of the map to set the value.
     * @param key   The key for which the value is to be set.
     * @param value The value to be set in the map for the given key.
     */
    @Override
    public void setMap(String field, K key, V value) {
        IMap<K, V> map = hazelcastInstance.getMap(field);
        map.put(key, (V) gson.toJson(value));
    }

    /**
     * Sets the value for the given key in the map associated with the specified field with an expiration time.
     *
     * @param field   The name of the map to set the value.
     * @param key     The key for which the value is to be set.
     * @param value   The value to be set in the map for the given key.
     * @param seconds The duration in seconds after which the key-value pair will be automatically removed from the map.
     */
    @Override
    public void setCacheMap(String field, K key, V value, int seconds) {
        IMap<K, V> map = hazelcastInstance.getMap(field);
        map.put(key, (V) gson.toJson(value), seconds, TimeUnit.SECONDS);
    }

    /**
     * Sets the value for the given key in the map associated with the specified field with an expiration time and adds an entry listener to receive events related to this entry.
     *
     * @param field         The name of the map to set the value.
     * @param key           The key for which the value is to be set.
     * @param value         The value to be set in the map for the given key.
     * @param seconds       The duration in seconds after which the key-value pair will be automatically removed from the map.
     * @param entryListener An entry listener to receive events related to this entry.
     */
    @Override
    public void setCacheMap(String field, K key, V value, int seconds, EntryListener<K, V> entryListener) {
        IMap<K, V> map = hazelcastInstance.getMap(field);
        map.put(key, (V) gson.toJson(value), seconds, TimeUnit.SECONDS);
        map.addEntryListener(entryListener, true);
    }

    /**
     * Removes the key-value pair associated with the given key from the map with the specified field.
     *
     * @param field The name of the map to remove the key-value pair.
     * @param key   The key for which the key-value pair is to be removed.
     */
    @Override
    public void removeMap(String field, K key) {
        IMap<K, V> map = hazelcastInstance.getMap(field);
        map.remove(key);
    }

    /**
     * Retrieves the value associated with the given key from the map with the specified field and converts it to the specified class type using Gson (Google's JSON library).
     *
     * @param field The name of the map to retrieve the value.
     * @param key   The key for which the value is to be retrieved.
     * @param clazz The class type to which the value should be converted.
     * @return The value associated with the given key, converted to the specified class type.
     */
    @Override
    public Object mapGet(String field, K key, Class<?> clazz) {
        IMap<K, V> map = hazelcastInstance.getMap(field);
        String jsonValue = String.valueOf(map.get(key));
        return gson.fromJson(jsonValue, clazz);
    }

    /**
     * Retrieves the value associated with the given key from the map with the specified field.
     *
     * @param field The name of the map to retrieve the value.
     * @param key   The key for which the value is to be retrieved.
     * @return The value associated with the given key.
     */
    @Override
    public Object mapGet(String field, K key) {
        IMap<K, V> map = hazelcastInstance.getMap(field);
        return map.get(key);
    }

    /**
     * Returns an ITopic (Hazelcast Topic) for the given field.
     *
     * @param field The name of the topic to be registered.
     * @return The ITopic instance associated with the given field.
     */
    @Override
    public ITopic<String> topicRegister(String field) {
        return hazelcastInstance.getTopic(field);
    }

    /**
     * Returns a set containing all the keys in the map associated with the given field.
     *
     * @param field The name of the map to retrieve the keys.
     * @return A set containing all the keys in the map.
     */
    @Override
    public Set<K> mapKeys(String field) {
        IMap<K, V> map = hazelcastInstance.getMap(field);
        return map.keySet();
    }

    /**
     * Returns an immutable copy of the map associated with the given field.
     *
     * @param field The name of the map to retrieve the value.
     * @return An immutable copy of the map.
     */
    @Override
    public Map<K, V> mapValue(String field) {
        IMap<K, V> map = hazelcastInstance.getMap(field);
        return Map.copyOf(map);
    }

    /**
     * Shuts down the Hazelcast instance.
     */
    @Override
    public void shutdown() {
        this.hazelcastInstance.shutdown();
    }
}