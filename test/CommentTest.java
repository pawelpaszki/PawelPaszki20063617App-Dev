import java.util.ArrayList;
import java.util.List;

import play.test.UnitTest;
import models.Comment;
import models.Post;
import models.User;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import play.test.Fixtures;
import play.test.UnitTest;

public class CommentTest extends UnitTest {

	private User bob, chris;
	private Post post1, post2;
	private Comment comment1, comment2;

	@BeforeClass
	public static void loadDB() {
		Fixtures.deleteAllModels();
	}
	
	@Before
	public void setup() {
		bob = new User("bob", "jones", "bob@jones.com", "secret", 20, "irish");
		chris = new User("chris", "downey", "chris@downey.com", "secret", 28, "irish");
		post1 = new Post("Post Title 1", "This is the first post content");
		post2 = new Post("Post Title 2", "This is the second post content");
		comment1 = new Comment("this is the first comment", bob.firstName, "10/02/2015");
		comment2 = new Comment("this is the second comment", chris.firstName, "11/02/2015");
		bob.save();
		chris.save();
		post1.save();
		post2.save();
		comment1.save();
		comment2.save();
	}
	
	@After
	public void teardown() {
		bob.delete();
		chris.delete();
		post1.delete();
		post2.delete();
		comment1.delete();
		comment2.delete();
	}
	
	@Test
	public void testFindComments() {
		User user = User.findByEmail("chris@downey.com");
		List<Post> posts = user.posts;
		posts.add(post1);
		Post post = posts.get(0);
		List<Comment> comments = post.comments;
		comments.add(comment1);
		assertNotNull(comments);
	}
	
	@Test
	public void testNoComments(){
		User user = User.findByEmail("chris@downey.com");
		List<Post> posts = user.posts;
		posts.add(post1);
		Post post = posts.get(0);
		List<Comment> comments = post.comments;
		assertEquals(comments.size() ,0);
	}
	
	@Test
	public void testAddSingleComment() {
		chris.posts.add(post2);
		chris.save();
		
		User user = User.findByEmail("chris@downey.com");
		List<Post> posts = user.posts;
		Post post = posts.get(0);
		post.comments.add(comment2);
		
		assertEquals(post.comments.get(0).commentText, "this is the second comment");
		assertEquals(post.comments.get(0).fromPerson, chris.firstName);
		assertEquals(post.comments.get(0).date, "11/02/2015");
		
	}
	
	@Test
	public void testAddMultipleComments() {
		bob.posts.add(post1);
		bob.save();
		
		User user = User.findByEmail("bob@jones.com");
		List<Post> posts = user.posts;
		Post post = posts.get(0);
		post.comments.add(comment1);
		post.comments.add(comment2);
		assertEquals(post.comments.get(0).commentText, "this is the first comment");
		assertEquals(post.comments.get(0).fromPerson, bob.firstName);
		assertEquals(post.comments.get(0).date, "10/02/2015");
		assertEquals(post.comments.get(1).commentText, "this is the second comment");
		assertEquals(post.comments.get(1).fromPerson, chris.firstName);
		assertEquals(post.comments.get(1).date, "11/02/2015");
	}
	
	@Test
	public void testDeleteComment() {
		bob.posts.add(post1);
		bob.save();
		
		User user = User.findByEmail("bob@jones.com");
		List<Post> posts = user.posts;
		Post post = posts.get(0);
		post.comments.add(comment1);
		
		user.save();
		assertEquals(1, post.comments.size());
		post.comments.remove(0);
		assertEquals(0, post.comments.size());
	}
	
	@Test
	public void testDeletePostAndComment() {
		bob.posts.add(post1);
		bob.save();
		
		User user = User.findByEmail("bob@jones.com");
		List<Post> posts = user.posts;
		Post post = posts.get(0);
		post.comments.add(comment1);
		
		user.save();
		assertEquals(1, post.comments.size());
		post.comments.remove(0);
		assertEquals(0, post.comments.size());
		user.posts.remove(0);
		assertTrue(posts.size() == 0);
	}
	
	@Test
	public void testUpdateComment() {
		comment1 = new Comment("this is the first comment", bob.firstName, "10/02/2015");
		comment1.commentText = "a comment";
		comment1.date = "28/02/2015";
		comment1.fromPerson = chris.firstName;
		assertEquals (comment1.commentText, "a comment");
		assertEquals (comment1.date, "28/02/2015");
		assertEquals (comment1.fromPerson, chris.firstName);
		assertNotSame("this is the first comment", comment1.commentText);
		assertNotSame(bob.firstName, comment1.fromPerson);
		assertNotSame("10/02/2015", comment1.date);
	}
}
