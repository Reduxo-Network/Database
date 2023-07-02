package de.rubymc.database.hazelcast.impl;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.topic.ITopic;
import de.rubymc.database.hazelcast.IServer;

import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class HazelServer implements IServer {

    private final HazelcastInstance hazelcastInstance;

    public HazelServer() {
        this.hazelcastInstance = HazelcastClient.newHazelcastClient();
    }

    public HazelServer(String address) {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.getNetworkConfig().addAddress(address);
        this.hazelcastInstance = HazelcastClient.newHazelcastClient(clientConfig);
    }

    //<editor-fold desc="create">
    public static HazelServer create(){
        return new HazelServer();
    }

    public static HazelServer create(String address){
        return new HazelServer(address);
    }
    //</editor-fold>

    //<editor-fold desc="setMap">
    @Override
    public void setMap(String table, String key, Object object) {
        Map<String, String> map = hazelcastInstance.getMap(table);
        map.put(key, GSON.toJson(object));
    }

    @Override
    public void setMap(String table, String key, String value) {
        Map<String, String> map = hazelcastInstance.getMap(table);
        map.put(key, value);
    }
    //</editor-fold>

    //<editor-fold desc="contain">
    public boolean contain(String table, String key) {
        return hazelcastInstance.getMap(table).containsKey(key);
    }
    //</editor-fold>

    //<editor-fold desc="replace">
    @Override
    public void replace(String table, String key, Object object) {
        hazelcastInstance.getMap(table).replace(key, GSON.toJson(object));
    }
    //</editor-fold>

    //<editor-fold desc="removeMap">
    @Override
    public void removeMap(String table, String key) {
        Map<String, String> map = hazelcastInstance.getMap(table);
        map.remove(key);
    }
    //</editor-fold>

    //<editor-fold desc="mapGet">
    @Override
    public Object mapGet(String table,String key, Class<?> clazz) {
        Map<String, String> map = hazelcastInstance.getMap(table);
        return GSON.fromJson(map.get(key), clazz);
    }

    @Override
    public String mapGet(String table, String key) {
        Map<String, String> map = hazelcastInstance.getMap(table);
        return map.get(key);
    }

    public void mapGetAsync(String table, String key, Class<?> cls, Consumer<Object> consumer) {
        hazelcastInstance.getMap(table).getAsync(key).thenAccept(obj -> consumer.accept(GSON.fromJson(obj.toString(), cls)));
    }

    //</editor-fold>

    //<editor-fold desc="topicRegister">
    @Override
    public ITopic<String> topicRegister(String table) {
        return hazelcastInstance.getTopic(table);
    }
    //</editor-fold>

    //<editor-fold desc="mapKeys">
    @Override
    public Set<String> mapKeys(String table) {
        Map<String, String> map = hazelcastInstance.getMap(table);
        return map.keySet();
    }
    //</editor-fold>

    //<editor-fold desc="mapValue">
    @Override
    public Map<String, Object> mapValue(String table) {
        return Map.copyOf(hazelcastInstance.getMap(table));
    }
    //</editor-fold>

    //<editor-fold desc="shutdown">
    @Override
    public void shutdown() {
        this.hazelcastInstance.shutdown();
    }
    //</editor-fold>

}
