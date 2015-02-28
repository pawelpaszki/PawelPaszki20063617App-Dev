package controllers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import models.Comment;
import models.Post;
import models.User;
import play.Logger;
import play.mvc.Controller;

public class Blog extends Controller {
	public static void index() {
		User user = Accounts.getLoggedInUser();
		List<User> users = User.findAll();
		List<User> loggedInUsers = new ArrayList<>();
		for (User aUser : users) {
			if (aUser.loggedIn) {
				loggedInUsers.add(aUser);
			}
		}
		// when application is stopped - the session is not cleared
		// to prevent accessing this view following test is implemented:
		if (loggedInUsers.size() == 0) {
			Accounts.login();
		}
		Logger.info("LoggedIn " + loggedInUsers);
		render(user);
	}

	public static void newPost(String title, String content) {
		User user = Accounts.getLoggedInUser();

		Post post = new Post(title, content);
		post.save();
		user.posts.add(0, post);
		user.save();

		Logger.info("title:" + title + " content:" + content);
		index();
	}

	public static void view(Long id) {
		User user = User.findById(id);
		String userId = session.get("logged_in_userid");
		User me = null;
		if (userId != null) {
			me = User.findById(Long.parseLong(userId));
			Logger.info("me: " + me.firstName + ", user: " + user.firstName);
		}
		Logger.info("user: " + user.firstName);
		render(user, me);
	}

	public static void deletePost(Long postid) {
		User user = Accounts.getLoggedInUser();

		Post post = Post.findById(postid);
		if (post.comments.size() > 0) {
			for (Comment comment: post.comments) {
				post.comments.remove(comment);
			}
		}
		user.posts.remove(post);

		user.save();
		post.delete();

		index();
	}

	public static void addComment(String comment, Long postid) {
		User user = Accounts.getLoggedInUser();
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		Date date = new Date();
		String fromUser = user.firstName ;
		Comment newComment = new Comment(comment, fromUser, dateFormat.format(date));
		newComment.save();
		Post post = Post.findById(postid);
		post.comments.add(0, newComment);
		post.save();
		viewPost(postid);
	}
	
	public static void addCommentToUsersPost(String comment, Long postid) {
		User user = Accounts.getLoggedInUser();
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		Date date = new Date();
		String fromUser = user.firstName;
		Comment newComment = new Comment(comment, fromUser, dateFormat.format(date));
		newComment.save();
		Post post = Post.findById(postid);
		post.comments.add(0, newComment);
		post.save();
		viewUsersPost(postid);
	}
	
	public static void viewPost(Long postid) {
		User user = Accounts.getLoggedInUser();
		
		Post post = Post.findById(postid);
		render(user,post);
	}
	
	public static void viewUsersPost(Long postid) {
		List<User> users = User.findAll();
		String userId = session.get("logged_in_userid");
		User me = null;
		if (userId != null) {
			me = User.findById(Long.parseLong(userId));
			Logger.info("me: " + me.firstName);
		}
		Post post = Post.findById(postid);
		render(post,me,users);
	}
}