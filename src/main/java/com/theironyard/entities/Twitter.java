package com.theironyard.entities;

import javax.persistence.*;

/**
 * Created by dlocke on 1/2/17.
 */

@Entity
@Table(name="tweets")
public class Twitter {

    @Id
    @GeneratedValue
    int id;

    @Column(nullable = false)
    String author;

    @Column(nullable = false)
    String post;

    //many tweets to one user
    @ManyToOne
    User user;

    public Twitter(int id, String author, String post, User user) {
        this.id = id;
        this.author = author;
        this.post = post;
        this.user = user;
    }

    public Twitter(String author, String post){
        this.author = author;
        this.post = post;
    }

    public Twitter(String author){
        this.author = author;
    }

    public Twitter (){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}//end class Twitter