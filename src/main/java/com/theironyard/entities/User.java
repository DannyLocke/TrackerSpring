package com.theironyard.entities;

import javax.persistence.*;

/**
 * Created by dlocke on 1/2/17.
 */

@Entity
@Table(name="users")

public class User {

    @Id
    @GeneratedValue
    int id;

    @Column(nullable = false, unique = true)
    public String name;

    @Column(nullable = false)
    public String password;

    public User(int id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public User (){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}//end class User
