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

    //path for HOME
    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String home (HttpSession session, Model model, String author) throws Exception {

        String loginName = (String) session.getAttribute("loginName");

        if (loginName != null) {
            User user = users.findFirstByName(loginName);
            model.addAttribute("user", user);
        }

        List<Twitter> twitterList;

        if (author != null) {
            twitterList = (List<Twitter>) tweets.findByAuthor(author);
        } else{
            twitterList = (List<Twitter>) tweets.findAll();
        }
        model.addAttribute("tweets", twitterList);
        return "home";
    }//end home()

    //path for login
    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String login (HttpSession session, String loginName, String loginPassword) throws Exception {
        User user = users.findFirstByName(loginName);
        if(user == null){
            user = new User(loginName, PasswordStorage.createHash(loginPassword));
            users.save(user);
        }
        else if (!PasswordStorage.verifyPassword(loginPassword, user.password)){
            throw new Exception("Incorrect Password");
        }
        session.setAttribute("loginName", loginName);
        return "redirect:/";
    }//end login()

    @RequestMapping(path = "/logout", method = RequestMethod.POST)
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/";
    }

    //path for new post
    @RequestMapping(path = "/createPost", method = RequestMethod.POST)
    public String createPost (HttpSession session, String author, String post) throws Exception {

        String loginName = (String) session.getAttribute("loginName");
        User user = users.findFirstByName(loginName);
        int id = user.getId();

        if(author != null && post != null) {

            Twitter twitter = new Twitter(id, author, post, user);
            tweets.save(twitter);
        }

        return "redirect:/";

    }//end createPost()

    //path for deleting post
    @RequestMapping(path = "/deletePost", method = RequestMethod.POST)
    public String deletePost (Integer id) throws Exception {

        tweets.delete(id);

        return "redirect:/";
    }//end deletePost()

//    <form action = "/deletePost" method = "post">
//    <input type = "text" placeholder = "Which post do you want to delete?" name = "deletePost"/>
//    <button type="submit">Delete Post</button>
//    </form>

    //path for updating post
    @RequestMapping(path = "/updatePost", method = RequestMethod.POST)
    public String updatePost (Integer id, String updatePost) throws Exception {

        Twitter twitter = tweets.findOne(id);
        twitter.setPost(updatePost);
        tweets.save(twitter);

        return "redirect:/";
    }//end updatePost()

//    <form action = "/updatePost" method = "post">
//    <input type="hidden" placeholder="id" name="idNum"/>
//    <input type = "text" placeholder = "Post #" name = "num"/>
//    <input type = "text" placeholder="Edit post" name = "updatePost"/>
//    <button type="submit">Update Post</button>
//    </form>


    @PostConstruct
    public void init() throws PasswordStorage.CannotPerformOperationException {
        if(users.count() == 0){
            User user =  new User();
            user.name = "Danny";
            user.password = PasswordStorage.createHash("loginPassword");
            users.save(user);
        }
    }//end init()

}//end class TrackerSpringController