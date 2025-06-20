package org.example;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.*;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Sorts.ascending;
import static com.mongodb.client.model.Sorts.descending;

public class EmployeeService {
    private final MongoCollection<Document> collection;

    public EmployeeService() {
        this.collection = MongoUtil.getDatabase().getCollection("employees");
    }

    // Add
    public void addEmployee(Employee e) {
        if (collection.find(eq("email", e.getEmail())).first() != null) {
            System.out.println("Employee with email already exists.");
            return;
        }
        Document doc = new Document("name", e.getName())
                .append("email", e.getEmail())
                .append("department", e.getDepartment())
                .append("skills", e.getSkills())
                .append("joiningDate", e.getJoiningDate());
        collection.insertOne(doc);
        System.out.println("Employee added successfully.");
    }

    // Update
    public void updateEmployee(String email, Map<String, Object> updates) {
        List<Bson> updateList = new ArrayList<>();
        for (Map.Entry<String, Object> entry : updates.entrySet()) {
            updateList.add(Updates.set(entry.getKey(), entry.getValue()));
        }
        collection.updateOne(eq("email", email), Updates.combine(updateList));
        System.out.println("Employee updated.");
    }

    // Delete by email
    public void deleteByEmail(String email) {
        collection.deleteOne(eq("email", email));
        System.out.println("Employee deleted by email.");
    }

    // Delete by ID
    public void deleteById(String id) {
        collection.deleteOne(eq("_id", new org.bson.types.ObjectId(id)));
        System.out.println("Employee deleted by ID.");
    }

    // Search by name (regex)
    public void searchByName(String namePart) {
        try (MongoCursor<Document> cursor = collection.find(regex("name", namePart, "i")).iterator()) {
            while (cursor.hasNext()) {
                System.out.println(cursor.next().toJson());
            }
        }
    }

    // Filter by department
    public void filterByDepartment(String dept) {
        try (MongoCursor<Document> cursor = collection.find(eq("department", dept)).iterator()) {
            while (cursor.hasNext()) {
                System.out.println(cursor.next().toJson());
            }
        }
    }

    // Search by skill
    public void searchBySkill(String skill) {
        try (MongoCursor<Document> cursor = collection.find(Filters.in("skills", skill)).iterator()) {
            while (cursor.hasNext()) {
                System.out.println(cursor.next().toJson());
            }
        }
    }

    // Filter by joining date range
    public void filterByJoiningDate(Date from, Date to) {
        try (MongoCursor<Document> cursor = collection.find(and(gte("joiningDate", from), lte("joiningDate", to))).iterator()) {
            while (cursor.hasNext()) {
                System.out.println(cursor.next().toJson());
            }
        }
    }

    // Pagination and sorting
    public void paginateAndSort(int page, int size, String sortBy, boolean ascendingOrder) {
        Bson sort = ascendingOrder ? ascending(sortBy) : descending(sortBy);
        try (MongoCursor<Document> cursor = collection.find()
                .sort(sort)
                .skip((page - 1) * size)
                .limit(size)
                .iterator()) {
            while (cursor.hasNext()) {
                System.out.println(cursor.next().toJson());
            }
        }
    }

    // Department aggregation
    public void departmentStats() {
        List<Bson> pipeline = Arrays.asList(
                Aggregates.group("$department", Accumulators.sum("count", 1))
        );

        try (MongoCursor<Document> cursor = collection.aggregate(pipeline).iterator()) {
            while (cursor.hasNext()) {
                System.out.println(cursor.next().toJson());
            }
        }
    }
}
