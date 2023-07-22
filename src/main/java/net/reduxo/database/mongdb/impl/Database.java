package de.rubymc.mongodb.impl;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import de.rubymc.mongodb.IDatabase;
/**
 * Represents a MongoDB database connection.
 */
public class Database implements IDatabase {


    private final String database;
    private final ConnectionString connectionString;

    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;

    /**
     * Constructs a new Database object with the specified connection details.
     *
     * @param address  the address of the MongoDB server
     * @param port     the port number of the MongoDB server
     * @param username the username for authentication
     * @param password the password for authentication
     * @param database the name of the database
     */
    public Database(String address, Integer port,String username, String password, String database) {
        this.database = database;
        this.connectionString = new ConnectionString("mongodb://" + username + ":" + password + "@" + address + ":" + port + "/" + database);
    }

    /**
     * Creates a new Database object with the specified connection details.
     *
     * @param address  the address of the MongoDB server
     * @param port     the port number of the MongoDB server
     * @param username the username for authentication
     * @param password the password for authentication
     * @param database the name of the database
     * @return the created Database object
     */
    public static Database create(String address, Integer port,String username, String password, String database) {
        return new Database(address, port, username, password, database);
    }

    /**
     * Connects to the MongoDB database using the provided connection details.
     */
    @Override
    public void connect() {
        this.mongoClient = MongoClients.create(connectionString);
        this.mongoDatabase = mongoClient.getDatabase(database);
    }

    /**
     * Disconnects from the MongoDB database.
     */
    @Override
    public void disconnect() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }

    /**
     * Retrieves the currently connected MongoDatabase object.
     *
     * @return the MongoDatabase object
     */
    @Override
    public MongoDatabase getDatabase() {
        return mongoDatabase;
    }

    /**
     * Retrieves a specific MongoDatabase object by its key.
     *
     * @param key the key of the database to retrieve
     * @return the MongoDatabase object
     */
    @Override
    public MongoDatabase getDatabaseByKey(String key) {
        return mongoClient.getDatabase(key);
    }

    /**
     * Creates a new DatabaseCollection object for the specified table in the current database.
     *
     * @param table the name of the collection (table) to create
     * @return the created DatabaseCollection object
     */
    @Override
    public DatabaseCollection createDatabaseCollection(String table) {
        return new DatabaseCollection(mongoDatabase.getCollection(table));
    }

    /**
     * Creates a new DatabaseCollection object for the specified table in the specified database.
     *
     * @param database the name of the database
     * @param table    the name of the collection (table) to create
     * @return the created DatabaseCollection object
     */
    @Override
    public DatabaseCollection createDatabaseCollection(String database, String table) {
        return new DatabaseCollection(mongoClient.getDatabase(database).getCollection(table));
    }

}
