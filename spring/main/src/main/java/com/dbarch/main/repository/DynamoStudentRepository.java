package com.dbarch.main.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.dbarch.main.model.Student;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanResponse;

@Repository
public class DynamoStudentRepository{
    private final DynamoDbClient dynamoDbClient;
    @Value("${aws.dynamodb.table-name}")
    private String tableName;
    public DynamoStudentRepository(DynamoDbClient dynamoDbClient) {
        this.dynamoDbClient = dynamoDbClient;
    }
    public void save(Student student) {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("roll", AttributeValue.builder().s(student.getRoll()).build());
        item.put("name", AttributeValue.builder().s(student.getName()).build());
        item.put("branch", AttributeValue.builder().s(student.getBranch()).build());
        PutItemRequest request = PutItemRequest.builder().tableName(tableName).item(item).build();
        dynamoDbClient.putItem(request);
    }
    public Student findByRoll(String roll) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("roll", AttributeValue.builder().s(roll).build());
        GetItemRequest request = GetItemRequest.builder().tableName(tableName).key(key).build();
        Map<String, AttributeValue> item = dynamoDbClient.getItem(request).item();
        if (item == null || item.isEmpty()) {
            return null;
        }
        return new Student(item.get("name").s(), item.get("roll").s(), item.get("branch").s());
    }
    public List<Student> findAll(){
        ScanRequest request = ScanRequest.builder().tableName(tableName).build();
        ScanResponse response = dynamoDbClient.scan(request);
        List<Student> students = new ArrayList<>();
        for (Map<String, AttributeValue> item : response.items()) {
            students.add(new Student(item.get("name").s(), item.get("roll").s(), item.get("branch").s()));
        }
        return students;
    }
    public boolean hasItem(String roll){
        return findByRoll(roll) != null;
    }
    public void delete(String roll){
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("roll", AttributeValue.builder().s(roll).build());
        DeleteItemRequest request = DeleteItemRequest.builder().tableName(tableName).key(key).build();
        dynamoDbClient.deleteItem(request);
    }
}