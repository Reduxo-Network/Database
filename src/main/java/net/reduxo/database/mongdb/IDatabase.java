package de.rubymc.mongodb;

import com.mongodb.client.MongoDatabase;
import de.rubymc.mongodb.impl.DatabaseCollection;

/**

 The IDatabase interface represents a database connection and provides methods for interacting with the database.
 */
public interface IDatabase {

    /**

     Connects to the database.
     */
    void connect();
    /**

     Disconnects from the database.
     */
    void disconnect();
    /**

     Retrieves the connected MongoDatabase instance.
     @return The connected MongoDatabase instance.
     */
    MongoDatabase getDatabase();
    /**

     Retrieves the MongoDatabase instance associated with the specified key.
     @param key The key used to identify the MongoDatabase instance.
     @return The MongoDatabase instance associated with the specified key.
     */
    MongoDatabase getDatabaseByKey(String key);
    /**

     Creates a new database collection.
     @param collection The name of the collection to create.
     @return The created DatabaseCollection instance.
     */
    DatabaseCollection createDatabaseCollection(String collection);
    /**

     Creates a new database collection within the specified database.
     @param database The name of the database.
     @param table The name of the collection to create.
     @return The created DatabaseCollection instance.
     */
    DatabaseCollection createDatabaseCollection(String database, String table);
}
