package com.theironyard.controller;

import com.theironyard.entities.Twitter;
import com.theironyard.entities.User;
import com.theironyard.services.TwitterRepository;
import com.theironyard.services.UserRepository;
import com.theironyard.utilities.PasswordStorage;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Created by dlocke on 1/2/17.
 */

@Controller
public class TrackerSpringController {

    @Autowired
    UserRepository users;

    @Autowired
    TwitterRepository tweets;

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String login (HttpSession session, String userName, String password) throws Exception {
        User user = users.findFirstByName(userName);
        if(user == null){
            user = new User(userName, PasswordStorage.createHash(password));
            users.save(user);
        }
        else if (!PasswordStorage.verifyPassword(password, user.password)){
            throw new Exception("Incorrect Password");
        }
        session.setAttribute("userName", userName);
        return "redirect:/";
    }//end login()

    @RequestMapping(path = "/logout", method = RequestMethod.POST)
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/";
    }

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String home (HttpSession session, Model model, String author, String post) throws Exception {

        String userName = (String) session.getAttribute("loginName");

        List<Twitter> tweetList;

        if (userName != null) {
            User user = users.findFirstByName(userName);
            model.addAttribute("user", user);
        }

        if (author != null) {
            tweetList = tweets.findByAuthor(author);
        } else if (post != null) {
            tweetList = tweets.findByPost(post);
        } else{
            tweetList = (List<Twitter>) tweets.findAll();
        }
        model.addAttribute("tweets", tweetList);
        return "home";
    }//end home()

    @RequestMapping(path = "/createPost", method = RequestMethod.POST)
    public String createPost (HttpSession session, String author, String post) throws Exception {

        String userName = (String) session.getAttribute("loginName");
        User user = users.findFirstByName(userName);

        Twitter twitter = new Twitter(author, post);
        tweets.save(twitter);

        return "redirect:/";

    }//end createPost()

    @RequestMapping(path = "/deletePost", method = RequestMethod.POST)
    public String deletePost (HttpSession session, String author, String post) throws Exception {

        String userName = (String) session.getAttribute("loginName");
        User user = users.findFirstByName(userName);

        String deletePost = request.queryParams("deletePost");

        int x = Integer.parseInt(deletePost);
        deletePosts(x);

        return "redirect:/";
    }//end deletePost()

    @RequestMapping(path = "/updatePost", method = RequestMethod.POST)
    public String updatePost (HttpSession session, String author, String post) throws Exception {

        String userName = (String) session.getAttribute("loginName");
        User user = users.findFirstByName(userName);

        String num = request.queryParams("num");
        int x = Integer.parseInt(num);

        String updatePost = request.queryParams("updatePost");

        updatePosts(x, updatePost);

        response.redirect("/");
        return "";
    }//end updatePost()

    @PostConstruct
    public void init() throws PasswordStorage.CannotPerformOperationException {
        if(users.count() == 0){
            User user =  new User();
            user.name = "Danny";
            user.password = PasswordStorage.createHash("password");
            users.save(user);
        }
    }//end init()

}//end class TrackerSpringController