package de.rubymc.database.mongodb.impl;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import de.rubymc.database.mongodb.IDatabase;

public class Database implements IDatabase {


    private final String database;
    private final ConnectionString connectionString;

    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;

    public Database(String address, Integer port,String username, String password, String database) {
        this.database = database;
        this.connectionString = new ConnectionString("mongodb://" + username + ":" + password + "@" + address + ":" + port);
    }

    //<editor-fold desc="create">
    public static Database create(String address, Integer port,String username, String password, String database) {
        return new Database(address, port, username, password, database);
    }
    //</editor-fold>

    //<editor-fold desc="connect">
    @Override
    public void connect() {
        this.mongoClient = MongoClients.create(connectionString);
        this.mongoDatabase = mongoClient.getDatabase(database);
    }
    //</editor-fold>

    //<editor-fold desc="disconnect">
    @Override
    public void disconnect() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }
    //</editor-fold>

    //<editor-fold desc="getDatabase">
    @Override
    public MongoDatabase getDatabase() {
        return mongoDatabase;
    }
    //</editor-fold>

    //<editor-fold desc="getDatabaseByKey">
    @Override
    public MongoDatabase getDatabaseByKey(String key) {
        return mongoClient.getDatabase(key);
    }
    //</editor-fold>

    //<editor-fold desc="createDatabaseCollection">
    @Override
    public DatabaseCollection createDatabaseCollection(String table) {
        return new DatabaseCollection(mongoDatabase.getCollection(table));
    }
    //</editor-fold>

    //<editor-fold desc="createDatabaseCollection">
    @Override
    public DatabaseCollection createDatabaseCollection(String database, String table) {
        return new DatabaseCollection(mongoClient.getDatabase(database).getCollection(table));
    }
    //</editor-fold>

}
