package controllers;

import play.*;
import play.mvc.*;

import java.util.*;

import models.*;

public class PublicProfile extends Controller {
	public static void visit(Long id) {
		User user = User.findById(id);
		User me = Accounts.getLoggedInUser();
		
		Logger.info("Just visiting the page for " + user.firstName + ' '
				+ user.lastName);
		render(user, me);
	}

	public static void sendMessage(Long id, String messageText) {
		User fromUser = Accounts.getLoggedInUser();

		User toUser = User.findById(id);

		Logger.info("Message from user " + fromUser.firstName + ' '
				+ fromUser.lastName + " to " + toUser.firstName + ' '
				+ toUser.lastName + ": " + messageText);

		fromUser.sendMessage(toUser, messageText);
		visit(id);
	}
}