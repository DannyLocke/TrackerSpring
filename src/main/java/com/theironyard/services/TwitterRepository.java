package com.theironyard.services;

import com.theironyard.entities.Twitter;
import com.theironyard.entities.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by dlocke on 1/2/17.
 */
public interface TwitterRepository extends CrudRepository <Twitter, Integer> {
    Twitter findByAuthor(String author);
}
