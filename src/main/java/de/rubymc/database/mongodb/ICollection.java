package de.rubymc.database.mongodb;

import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

/**

 The ICollection interface represents a collection within a database and provides methods for interacting with the collection.
 */
public interface ICollection {

    /**

     Creates a new document in the collection.
     @param document The document to create.
     */
    void createDocument(final Document document);
    /**

     Creates a new document in the collection asynchronously.
     @param document The document to create.
     */
    void createDocumentAsync(final Document document);
    /**

     Deletes a document from the collection based on the specified key-value pair.
     @param key The key to match.
     @param value The value to match.
     @return True if the document was successfully deleted, false otherwise.
     */
    boolean deleteDocument(final String key, final Object value);
    /**

     Deletes a document from the collection asynchronously based on the specified key-value pair.
     @param key The key to match.
     @param value The value to match.
     */
    void deleteDocumentAsync(final String key, final Object value);
    /**

     Retrieves a document from the collection based on the specified key-value pair.
     @param key The key to match.
     @param value The value to match.
     @return The retrieved document, or null if no matching document is found.
     */
    Document getDocument(final String key, final Object value);
    /**

     Retrieves a document from the collection asynchronously based on the specified key-value pair.
     @param key The key to match.
     @param value The value to match.
     @param consumer The consumer to handle the retrieved document.
     */
    void getDocumentAsync(final String key, final Object value, Consumer<Document> consumer);
    /**

     Retrieves two documents from the collection based on the specified key-value pairs.
     @param firstKey The key to match for the first document.
     @param firstValue The value to match for the first document.
     @param secondKey The key to match for the second document.
     @param secondValue The value to match for the second document.
     @return A Pair containing the two retrieved documents.
     */
    Pair<Document, Document> getDocuments(final String firstKey, final Object firstValue, final String secondKey, final Object secondValue);
    /**

     Retrieves all documents in the collection.
     @return A list of all documents in the collection.
     */
    List<Document> collection();
    /**

     Retrieves the underlying MongoCollection instance.
     @return The MongoCollection instance.
     */
    MongoCollection<Document> mongoCollection();
    /**

     Updates a document in the collection based on the specified key-value pair.
     @param key The key to match.
     @param value The value to match.
     @param document The updated document.
     */
    void updateDocument(final String key, final Object value, final Document document);
    /**

     Updates a specific element within a document in the collection based on the specified key-value pair.
     @param key The key to match.
     @param value The value to match.
     @param updateKey The key of the element to update.
     @param updateValue The new value of the element.
     */
    void updateElement(final String key, final Object value, String updateKey, Object updateValue);
    /**

     Calculates the sum of all integer values in the specified field across all documents in the collection.
     @param field The field to calculate the sum for.
     @return The total sum of the integer values.
     */
    int sumTotalInt(String field);
    /**

     Calculates the sum of all long values in the specified field across all documents in the collection.
     @param field The field to calculate the sum for.
     @return The total sum of the long values.
     */
    long sumTotalLong(String field);
    /**

     Retrieves the rank of a document based on the value of a specified field and a unique identifier.
     @param field The field to compare.
     @param uniqueId The unique identifier of the document.
     @return The rank of the document.
     */
    int rank(String field, UUID uniqueId);
    /**

     Retrieves a list of documents from the collection sorted by a specified field in descending order, limited by the specified limit.
     @param field The field to sort by.
     @param limit The maximum number of documents to retrieve.
     @return A list of the top documents based on the specified field.
     */
    List<Document> topWall(String field, int limit);
}