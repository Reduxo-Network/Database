package de.rubymc.database.mongodb.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import de.rubymc.database.mongodb.ICollection;
import de.rubymc.database.mongodb.Pair;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class DatabaseCollection implements ICollection {

    private final ExecutorService service = Executors.newSingleThreadExecutor();
    private final MongoCollection<Document> collection;

    public DatabaseCollection(MongoCollection<Document> collection) {
        this.collection = collection;
    }

    //<editor-fold desc="createDocument">
    @Override
    public void createDocument(Document document) {
        this.collection.insertOne(document);
    }

    @Override
    public void createDocumentAsync(Document document) {
        this.service.submit(() -> collection.insertOne(document));
    }
    //</editor-fold>

    //<editor-fold desc="deleteDocument">
    @Override
    public boolean deleteDocument(String key, Object value) {
        return collection.deleteMany(Filters.eq(key, value)).wasAcknowledged();
    }

    @Override
    public void deleteDocumentAsync(String key, Object value) {
        this.service.submit(() -> collection.deleteMany(Filters.eq(key, value)));
    }
    //</editor-fold>

    //<editor-fold desc="getDocument">
    @Override
    public Document getDocument(String key, Object value) {
        return collection.find(Filters.eq(key, value)).first();
    }

    @Override
    public void getDocumentAsync(String key, Object value, Consumer<Document> consumer) {
        this.service.submit(() -> consumer.accept(getDocument(key, value)));
    }
    //</editor-fold>

    //<editor-fold desc="getDocuments">
    @Override
    public Pair<Document, Document> getDocuments(String firstKey, Object firstValue, String secondKey, Object secondValue) {
        return new Pair<>(getDocument(firstKey, firstValue), getDocument(secondKey, secondValue));
    }
    //</editor-fold>

    //<editor-fold desc="collection">
    @Override
    public List<Document> collection() {
        return collection.find().into(new ArrayList<>());
    }
    //</editor-fold>

    //<editor-fold desc="updateDocument">
    @Override
    public void updateDocument(String key, Object value, Document document) {
        this.collection.replaceOne(Filters.eq(key, value), document);
    }
    //</editor-fold>

    //<editor-fold desc="updateElement">
    @Override
    public void updateElement(String key, Object value, String updateKey, Object updateValue) {
        this.collection.findOneAndUpdate(Filters.eq(key, value),
                new BasicDBObject().append("$set", new BasicDBObject().append(updateKey, updateValue)));
    }
    //</editor-fold>
}
