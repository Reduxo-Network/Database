package de.rubymc.database.hazelcast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hazelcast.topic.ITopic;

import java.util.Map;
import java.util.Set;

public interface IServer {

    final Gson GSON = new GsonBuilder().
            serializeNulls().
            disableHtmlEscaping().
            setPrettyPrinting().
            create();

    void setMap(String table, String key, Object object);

    void setMap(String table, String key, String value);

    void replace(String table, String key, Object object);

    void removeMap(String table,String key);

    Object mapGet(String table, String key, Class<?> clazz);

    String mapGet(String table, String key);

    ITopic<String> topicRegister(String table);

    Set<String> mapKeys(String table);

    Map<String, Object> mapValue(String table);

    void shutdown();

}
