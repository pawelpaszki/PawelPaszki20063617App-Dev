package models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Entity;

import controllers.Accounts;
import play.db.jpa.Model;

@Entity
public class Comment extends Model
{
	public String commentText;
	public String fromPerson;
	public String date;
	
	public Comment(String commentText, String fromPerson, String date) {
		this.commentText = commentText;
		this.fromPerson = fromPerson;  
		this.date = date;
	}
	
	public String toString(){
		return "\n" + commentText + ": " + fromPerson;
	}
}

