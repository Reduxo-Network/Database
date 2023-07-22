package de.rubymc.mongodb.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.*;
import de.rubymc.mongodb.ICollection;
import de.rubymc.mongodb.Pair;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import static com.mongodb.client.model.Accumulators.sum;
import static com.mongodb.client.model.Aggregates.group;
import static com.mongodb.client.model.Filters.eq;

public class DatabaseCollection implements ICollection {

    private final ExecutorService service = Executors.newSingleThreadExecutor();
    private final MongoCollection<Document> collection;

    public DatabaseCollection(MongoCollection<Document> collection) {
        this.collection = collection;
    }
    /**
     * Inserts a document into the collection.
     *
     * @param document the document to be inserted
     */
    @Override
    public void createDocument(Document document) {
        this.collection.insertOne(document);
    }
    /**
     * Inserts a document into the collection asynchronously.
     *
     * @param document the document to be inserted
     */
    @Override
    public void createDocumentAsync(Document document) {
        this.service.submit(() -> collection.insertOne(document));
    }
    /**
     * Deletes a document from the collection based on the specified key-value pair.
     *
     * @param key   the key to match against
     * @param value the value to match against
     * @return true if the deletion was acknowledged, false otherwise
     */
    @Override
    public boolean deleteDocument(String key, Object value) {
        return collection.deleteMany(eq(key, value)).wasAcknowledged();
    }
    /**
     * Deletes a document from the collection asynchronously based on the specified key-value pair.
     *
     * @param key   the key to match against
     * @param value the value to match against
     */
    @Override
    public void deleteDocumentAsync(String key, Object value) {
        this.service.submit(() -> collection.deleteMany(eq(key, value)));
    }
    /**
     * Retrieves a document from the collection based on the specified key-value pair.
     *
     * @param key   the key to match against
     * @param value the value to match against
     * @return the matched document, or null if no document was found
     */
    @Override
    public Document getDocument(String key, Object value) {
        return collection.find(eq(key, value)).first();
    }
    /**
     * Retrieves a document from the collection asynchronously based on the specified key-value pair,
     * and invokes the provided consumer with the matched document.
     *
     * @param key      the key to match against
     * @param value    the value to match against
     * @param consumer the consumer to accept the matched document
     */
    @Override
    public void getDocumentAsync(String key, Object value, Consumer<Document> consumer) {
        this.service.submit(() -> consumer.accept(getDocument(key, value)));
    }

    /**
     * Retrieves two documents from the collection based on the specified key-value pairs.
     *
     * @param firstKey    the key to match against for the first document
     * @param firstValue  the value to match against for the first document
     * @param secondKey   the key to match against for the second document
     * @param secondValue the value to match against for the second document
     * @return a Pair of the matched documents, or null if either document was not found
     */
    @Override
    public Pair<Document, Document> getDocuments(String firstKey, Object firstValue, String secondKey, Object secondValue) {
        return new Pair<>(getDocument(firstKey, firstValue), getDocument(secondKey, secondValue));
    }

    /**
     * Retrieves all documents in the collection.
     *
     * @return a List containing all the documents in the collection
     */
    @Override
    public List<Document> collection() {
        return collection.find().into(new ArrayList<>());
    }

    /**
     * Retrieves the underlying MongoCollection object.
     *
     * @return the MongoCollection object
     */
    @Override
    public MongoCollection<Document> mongoCollection() {
        return collection;
    }

    /**
     * Updates a document in the collection based on the specified key-value pair.
     *
     * @param key      the key to match against
     * @param value    the value to match against
     * @param document the new document to replace the matched document
     */
    @Override
    public void updateDocument(String key, Object value, Document document) {
        this.collection.replaceOne(eq(key, value), document);
    }

    /**
     * Updates an element within a document in the collection based on the specified key-value pair,
     * and the key-value pair of the element to be updated.
     *
     * @param key         the key to match against
     * @param value       the value to match against
     * @param updateKey   the key of the element to be updated
     * @param updateValue the new value for the element
     */
    @Override
    public void updateElement(String key, Object value, String updateKey, Object updateValue) {
        this.collection.findOneAndUpdate(eq(key, value),
                new BasicDBObject().append("$set", new BasicDBObject().append(updateKey, updateValue)));

    }

    /**
     * Calculates the sum of integer values in the specified field across all documents in the collection.
     *
     * @param field the field to calculate the sum for
     * @return the sum of integer values, or -1 if no documents were found
     */
    @Override
    public int sumTotalInt(String field) {
        List<Bson> pipeline = List.of(group(null, sum("total", eq("$toInt", "$" + field))));
        AggregateIterable<Document> result = collection.aggregate(pipeline);
        Document document = result.first();
        if(document != null) {
            return document.getInteger("total");
        }
        return -1;
    }

    /**
     * Calculates the sum of long values in the specified field across all documents in the collection.
     *
     * @param field the field to calculate the sum for
     * @return the sum of long values, or -1 if no documents were found
     */
    @Override
    public long sumTotalLong(String field) {
        List<Bson> pipeline = List.of(group(null, sum("total", eq("$toLong", "$" + field))));
        AggregateIterable<Document> result = collection.aggregate(pipeline);
        Document document = result.first();
        if(document != null) {
            return document.getLong("total");
        }
        return -1;
    }

    /**
     * Retrieves the rank of a document based on the specified field and unique identifier.
     * The rank represents the position of the document when sorted in descending order of the field.
     * The rank starts from 1 for the highest value and increases for lower values.
     *
     * @param field     the field to rank against
     * @param uniqueId  the unique identifier of the document to rank
     * @return the rank of the document, or -1 if the document was not found
     */
    @Override
    public int rank(String field, UUID uniqueId) {
        List<Bson> pipeline = List.of(
                Aggregates.sort(Sorts.descending(field)),
                Aggregates.group(null, Accumulators.push("documents", "$$ROOT")),
                Aggregates.project(Projections.computed("rank", new Document("$indexOfArray", List.of("$documents.uniqueId", uniqueId)))),
                Aggregates.match(Filters.gte("rank", 0))
        );

        AggregateIterable<Document> result = collection.aggregate(pipeline);
        List<Document> resultList = new ArrayList<>();
        result.into(resultList);

        if (!resultList.isEmpty()) {
            Document rankDocument = resultList.get(0);
            int rank = rankDocument.getInteger("rank");
            return rank + 1;
        }

        return -1;
    }

    /**
     * Retrieves the top documents from the collection based on the specified field and limit.
     * The documents are sorted in ascending order of the field.
     *
     * @param field the field to sort and retrieve the top documents
     * @param limit the maximum number of documents to retrieve
     * @return a List containing the top documents
     */
    @Override
    public List<Document> topWall(String field, int limit) {
        List<Bson> pipeline = Arrays.asList(
                Aggregates.sort(Sorts.descending(field)),
                Aggregates.limit(limit)
        );
        AggregateIterable<Document> iterable = collection.aggregate(pipeline);
        List<Document> resultList = new ArrayList<>();
        iterable.into(resultList);
        return resultList;
    }
}
