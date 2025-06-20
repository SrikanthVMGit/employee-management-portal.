package org.example;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoUtil {
    private static final MongoClient client = MongoClients.create("mongodb://localhost:27017");

    public static MongoDatabase getDatabase() {
        return client.getDatabase("employee_portal");
    }
}
