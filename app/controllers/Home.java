package controllers;

import java.util.ArrayList;
import java.util.List;

import models.User;
import play.Logger;
import play.mvc.Controller;

public class Home extends Controller {
	public static void index() {
		User me = Accounts.getLoggedInUser();
		List<User> users = User.findAll();
		List<User> loggedInUsers = new ArrayList<>();
		for (User user : users) {
			if (user.loggedIn) {
				loggedInUsers.add(user);
			}
		}
		// when application is stopped - the session is not cleared
		// to prevent accessing this view following test is implemented:
		if (loggedInUsers.size() == 0) {
			Accounts.login();
		}
		Logger.info("LoggedIn " + loggedInUsers);
		render(me, loggedInUsers);

	}

	public static void drop(Long id) {
		User user = Accounts.getLoggedInUser();
		User friend = User.findById(id);
		user.unfriend(friend);
		Logger.info("Dropping " + friend.email);
		index();
	}

}