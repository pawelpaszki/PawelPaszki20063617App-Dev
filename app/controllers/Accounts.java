package controllers;

import java.util.ArrayList;
import java.util.List;

import models.User;
import play.Logger;
import play.mvc.Controller;

public class Accounts extends Controller {
	public static void signup() {
		render();
	}

	public static void login() {
		render();
	}

	public static void logout() {
		User user = getLoggedInUser();
		user.loggedIn = false;
		user.save();
		session.clear();
		index();
	}

	public static void index() {
		List<User> users = User.findAll();
		render(users);
	}

	public static User getLoggedInUser() {
		User user = null;
		if (session.contains("logged_in_userid")) {
			String userId = session.get("logged_in_userid");
			user = User.findById(Long.parseLong(userId));
		} else {
			login();
		}
		return user;
	}

	public static void register(String firstName, String lastName, int age,
			String nationality, String email, String password, String password2) {
		Logger.info(firstName + " " + lastName + " " + email + " " + password);
		User user = new User(firstName, lastName, email, password, age,
				nationality);
		user.save();
		index();
	}

	public static void authenticate(String email, String password) {
		Logger.info("Attempting to authenticate with " + email + ":" + password);

		User user = User.findByEmail(email);
		if ((user != null) && (user.checkPassword(password) == true)) {
			user.loggedIn = true;
			Logger.info("Authentication successful: " + user.firstName + " "+user.loggedIn);
			session.put("logged_in_userid", user.id);
			user.save();
			Home.index();
		} else {
			Logger.info("Authentication failed");
			login();
		}
	}
}