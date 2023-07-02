package de.rubymc.database.mongodb;

import org.bson.Document;

import java.util.List;
import java.util.function.Consumer;

public interface ICollection {


    void createDocument(final Document document);

    void createDocumentAsync(final Document document);

    boolean deleteDocument(final String key, final Object value);

    void deleteDocumentAsync(final String key, final Object value);

    Document getDocument(final String key, final Object value);

    void getDocumentAsync(final String key, final Object value, Consumer<Document> consumer);

    Pair<Document, Document> getDocuments(final String firstKey, final Object firstValue, final String secondKey, final Object secondValue);

    List<Document> collection();

    void updateDocument(final String key, final Object value, final Document document);

    void updateElement(final String key, final Object value, String updateKey, Object updateValue);

}
