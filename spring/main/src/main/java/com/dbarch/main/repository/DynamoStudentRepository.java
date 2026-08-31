package com.dbarch.main.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.dbarch.main.model.Student;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

@Repository
public class DynamoStudentRepository {

    private final DynamoDbClient dynamoDbClient;

    @Value("${aws.dynamodb.table}")
    private String tableName;

    public DynamoStudentRepository(DynamoDbClient dynamoDbClient) {
        this.dynamoDbClient = dynamoDbClient;
    }

    public void save(Student student) {

        Map<String, AttributeValue> item = Map.of(
                "roll", AttributeValue.builder()
                        .s(student.getRoll())
                        .build(),

                "name", AttributeValue.builder()
                        .s(student.getName())
                        .build(),

                "branch", AttributeValue.builder()
                        .s(student.getBranch())
                        .build()
        );

        dynamoDbClient.putItem(
                PutItemRequest.builder()
                        .tableName(tableName)
                        .item(item)
                        .build()
        );
    }

    public Student findByRoll(String roll) {

        GetItemResponse response = dynamoDbClient.getItem(
                GetItemRequest.builder()
                        .tableName(tableName)
                        .key(
                                Map.of(
                                        "roll",
                                        AttributeValue.builder()
                                                .s(roll)
                                                .build()
                                )
                        )
                        .build()
        );

        if (!response.hasItem()) {
            return null;
        }

        Map<String, AttributeValue> item = response.item();

        return new Student(
                item.get("roll").s(),
                item.get("name").s(),
                item.get("branch").s()
        );
    }

    public List<Student> findAll() {

        ScanResponse response = dynamoDbClient.scan(
                ScanRequest.builder()
                        .tableName(tableName)
                        .build()
        );

        List<Student> students = new ArrayList<>();

        for (Map<String, AttributeValue> item : response.items()) {

            students.add(
                    new Student(
                            item.get("roll").s(),
                            item.get("name").s(),
                            item.get("branch").s()
                    )
            );
        }

        return students;
    }

    public void delete(String roll) {

        dynamoDbClient.deleteItem(
                DeleteItemRequest.builder()
                        .tableName(tableName)
                        .key(
                                Map.of(
                                        "roll",
                                        AttributeValue.builder()
                                                .s(roll)
                                                .build()
                                )
                        )
                        .build()
        );
    }
}