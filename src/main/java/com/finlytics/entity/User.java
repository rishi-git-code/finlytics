package com.finlytics.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;



@Data
@Document(collection = "user")
public class User {

    @Id // This annotation is used to specify the primary key of the document(_id).
    private String id;
    private String userId;
    private String username;
    private String password;
    private String status;
    private String email;
    private String role;
    private String phoneNumber;

    /*
        If we are using MongoDB, we can use the @Document annotation to specify the collection name.
        if we are using lambok, we can use the @Data annotation to generate getters and setters for the fields.
     */
}
