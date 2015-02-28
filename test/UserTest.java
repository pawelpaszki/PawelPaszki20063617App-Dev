import java.util.ArrayList;
import java.util.List;

import models.Friendship;
import models.User;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import play.test.Fixtures;
import play.test.UnitTest;

public class UserTest extends UnitTest {
	private User bob;
	private User chris;

	@BeforeClass
	public static void loadDB() {
		Fixtures.deleteAllModels();
	}

	@Before
	public void setup() {
		bob = new User("bob", "jones", "bob@jones.com", "secret", 20, "irish");
		bob.save();
		chris = new User("chris", "jones", "chris@jones.com", "secret", 28, "british");
		chris.save();
		
	}

	@After
	public void teardown() {
		bob.delete();
		chris.delete();
	}

	@Test
	public void testNoUsers() {
		List<User> users = new ArrayList<User>();
		assertEquals(users.size(), 0);
	}
	
	@Test
	public void testCreateUser() {
		User testUser = User.findByEmail("bob@jones.com");
		assertNotNull(testUser);
	}
	
	@Test
	public void testCreateMultipleUsers() {
		User userOne = User.findByEmail("bob@jones.com");
		User userTwo = User.findByEmail("chris@jones.com");
		assertNotNull(userOne);
		assertNotNull(userTwo);
		List<User> users = new ArrayList<User>();
		users.add(userOne);
		users.add(userTwo);
		assertEquals(users.size(), 2);
		
	}
	
	@Test
	public void testFindUser() {
		User testUser = User.findByEmail("bob@jones.com");
		assertNotNull(testUser);
	}
	
	@Test
	public void deleteUsers() {
		List<User> users = new ArrayList<User>();
		users.add(bob);
		users.add(chris);
		assertEquals(users.size(), 2);
		users.remove(1);
		assertEquals(users.size(),1);
		users.remove(0);
		assertEquals(users.size(), 0);	
	}
	
	@Test
	public void updateUser(){
		User user = new User("new", "user", "new@user.com", "password", 0, "irish");
		user.save();
		user.firstName = "Tom";
		user.lastName = "Jones";
		assertEquals(user.firstName, "Tom");
		assertEquals(user.lastName, "Jones");
		user.delete();
	}
}