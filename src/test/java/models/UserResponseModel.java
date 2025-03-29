package models;

import lombok.Data;

@Data
public class UserResponseModel {

    String name;
    String job;
    String id;
    String createdAt;
    String updatedAt;
}
