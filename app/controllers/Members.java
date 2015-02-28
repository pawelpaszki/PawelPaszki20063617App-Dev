package controllers;

import play.*;
import play.mvc.*;

import java.util.*;

import models.*;

public class Members extends Controller {

	public static void index() {
		User me = Accounts.getLoggedInUser();
		List<User> users = User.findAll();
		users.remove(me);
		users.add(me);
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
		render(me, users, loggedInUsers);

	}

	public static void follow(Long id) {
		User user = Accounts.getLoggedInUser();
		User friend = User.findById(id);
		user.befriend(friend);
		Home.index();
	}

}