import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import models.User;
import play.Logger;
import play.db.jpa.Blob;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.libs.MimeTypes;
import play.test.Fixtures;

@OnApplicationStart
public class Bootstrap extends Job {
	public void doJob() {
		if (User.count() == 0) {
			Fixtures.deleteDatabase();
			Fixtures.loadModels("data.yml");
			loadImages();
		}	
	}
	private void loadImages(){
		List<User> users = User.findAll();
		for (int i = 0; i < users.size(); i++) {
			StringBuilder photoName = new StringBuilder("public/images/" + users.get(i).getName() + ".gif");
			StringBuilder thumbnail = new StringBuilder("public/images/" + users.get(i).getName() + "T.gif");
			Blob blob = new Blob();
			Blob thumb = new Blob();
			try {
				blob.set(new FileInputStream(photoName.toString()),
						MimeTypes.getContentType(photoName.toString()));
				thumb.set(new FileInputStream(thumbnail.toString()),
						MimeTypes.getContentType(thumbnail.toString()));
			} catch (FileNotFoundException e) {
				Logger.info("file not loaded ");
			}
			users.get(i).profilePicture = blob;
			users.get(i).thumbnailPicture = thumb;
			users.get(i).save();
		}
	}
}