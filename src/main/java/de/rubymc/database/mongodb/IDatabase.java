package de.rubymc.database.mongodb;

import com.mongodb.client.MongoDatabase;
import de.rubymc.database.mongodb.impl.DatabaseCollection;

public interface IDatabase {

    void connect();

    void disconnect();

    MongoDatabase getDatabase();

    MongoDatabase getDatabaseByKey(String key);

    DatabaseCollection createDatabaseCollection(String collection);

    DatabaseCollection createDatabaseCollection(String database, String table);


}
