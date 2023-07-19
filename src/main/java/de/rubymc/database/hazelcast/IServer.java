package de.rubymc.database.hazelcast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hazelcast.core.EntryListener;
import com.hazelcast.topic.ITopic;

import java.util.Map;
import java.util.Set;

/**
 * Interface representing a Hazelcast server providing distributed map operations.
 *
 * @param <K> The type of keys in the distributed maps.
 * @param <V> The type of values in the distributed maps.
 */
public interface IServer<K, V> {

    final Gson gson = new GsonBuilder().
            serializeNulls().
            disableHtmlEscaping().
            setPrettyPrinting().
            create();

    /**
     * Checks if the specified key exists in the distributed map.
     *
     * @param field The name of the distributed map.
     * @param key   The key to check for existence.
     * @return true if the key exists in the map, false otherwise.
     */
    boolean containsKey(String field, K key);

    /**
     * Sets a key-value pair in the specified distributed map.
     *
     * @param field The name of the distributed map.
     * @param key   The key of the pair.
     * @param value The value of the pair.
     */
    void setMap(String field, K key, V value);

    /**
     * Sets a key-value pair with a specified expiration time (in seconds) in the specified distributed map.
     *
     * @param field   The name of the distributed map.
     * @param key     The key of the pair.
     * @param value   The value of the pair.
     * @param seconds The expiration time in seconds.
     */
    void setCacheMap(String field, K key, V value, int seconds);

    /**
     * Sets a key-value pair with a specified expiration time (in seconds) in the specified distributed map
     * and adds an EntryListener to the map.
     *
     * @param field         The name of the distributed map.
     * @param key           The key of the pair.
     * @param value         The value of the pair.
     * @param seconds       The expiration time in seconds.
     * @param entryListener The EntryListener to register.
     */
    void setCacheMap(String field, K key, V value, int seconds, EntryListener<K, V> entryListener);

    /**
     * Removes a key-value pair from the specified distributed map.
     *
     * @param field The name of the distributed map.
     * @param key   The key of the pair to remove.
     */
    void removeMap(String field, K key);

    /**
     * Retrieves the value associated with the specified key from the specified distributed map.
     *
     * @param field The name of the distributed map.
     * @param key   The key to retrieve the value for.
     * @param clazz The class type of the value to retrieve.
     * @return The retrieved value, or null if no value is found.
     */
    Object mapGet(String field, K key, Class<?> clazz);

    /**
     * Retrieves the value associated with the specified key from the specified distributed map.
     *
     * @param field The name of the distributed map.
     * @param key   The key to retrieve the value for.
     * @return The retrieved value, or null if no value is found.
     */
    Object mapGet(String field, K key);

    /**
     * Registers a topic for the specified distributed map.
     *
     * @param field The name of the distributed map.
     * @return The registered ITopic instance.
     */
    ITopic<String> topicRegister(String field);

    /**
     * Retrieves a set of keys from the specified distributed map.
     *
     * @param field The name of the distributed map.
     * @return A set of keys in the map.
     */
    Set<K> mapKeys(String field);

    /**
     * Retrieves a map of key-value pairs from the specified distributed map.
     *
     * @param field The name of the distributed map.
     * @return A map of key-value pairs in the map.
     */
    Map<K, V> mapValue(String field);

    /**
     * Shuts down the server.
     */
    void shutdown();
}