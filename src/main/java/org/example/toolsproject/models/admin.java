package org.example.toolsproject.models;


import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("Admin")
public class admin extends User {

    public admin() {}

    public admin(String name, String email, String password, String bio) {
        super( name,  email,  password,  bio);
    }

}
