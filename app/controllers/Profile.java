package controllers;

import play.*;
import play.db.jpa.Blob;
import play.mvc.*;

import java.util.*;

import models.*;

public class Profile extends Controller {
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
		render(me);
	}

	public static void changeStatus(String statusText) {
		User user = Accounts.getLoggedInUser();
		if (statusText.length() < 20) {
			user.statusText = statusText;
		} else {
			user.statusText = statusText.substring(0, 20);
		}
		user.save();
		Logger.info("Status changed to " + statusText);
		index();
	}

	public static void getPicture(Long id) {
		User user = User.findById(id);
		Blob picture = user.profilePicture;
		if (picture.exists()) {
			response.setContentTypeIfNotSet(picture.type());
			renderBinary(picture.get());
		}
	}

	public static void uploadPicture(Long id, Blob picture) {
		User user = User.findById(id);
		user.profilePicture = picture;
		user.save();
		Logger.info("saving picture");
		index();
	}

	public static void getThumbnail(Long id) {
		User user = User.findById(id);
		Blob picture = user.thumbnailPicture;
		if (picture.exists()) {
			response.setContentTypeIfNotSet(picture.type());
			renderBinary(picture.get());
		}
	}

	public static void uploadThumbnail(Long id, Blob picture) {
		User user = User.findById(id);
		user.thumbnailPicture = picture;
		user.save();
		index();
	}
}