package controllers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import models.User;
import play.Logger;
import play.db.jpa.Blob;
import play.libs.MimeTypes;
import play.mvc.Controller;

public class ImageLoader extends Controller {
	
	public static void loadImage (User user) {
		String photoName = "/public/images/homer.gif";
		Blob blob = new Blob();
		try {
			blob.set(new FileInputStream(photoName),
					MimeTypes.getContentType(photoName));
		} catch (FileNotFoundException e) {
			Logger.info("file not loaded ");
		}
		User homer = User.findByEmail("homer@simpson.com");
		homer.profilePicture = blob;
		homer.save();
	}

}
