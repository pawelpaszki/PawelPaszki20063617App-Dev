import java.util.List;

import models.Friendship;
import models.User;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import play.test.Fixtures;
import play.test.UnitTest;

public class FriendshipTest extends UnitTest { 

	private User bob, chris, mark, jake;
	
	@BeforeClass
	public static void loadDB() {
		Fixtures.deleteAllModels();
	}

	@Before
	public void setup() {
		bob = new User("bob", "jones", "bob@jones.com", "secret", 20, "irish");
		chris = new User("chris", "downey", "chris@downey.com", "secret", 28, "irish");
		mark = new User("mark", "rowe", "mark@rowe.com", "secret", 25, "irish");
		jake = new User("jake", "ross", "jake@ross.com", "secret", 31, "irish");
		bob.save();
		chris.save();
		mark.save();
		jake.save();
	}

	@After
	public void teardown() {
		bob.delete();
		chris.delete();
		mark.delete();
		jake.delete();
	}
	
	@Test
	public void testNoFriendships() {
		User user = User.findByEmail("bob@jones.com");
		assertEquals(user.friendships.size(), 0);
	}
	
	@Test
	public void testAddOneFriend() {
		User user = User.findByEmail("bob@jones.com");
		User anotherUser = User.findByEmail("chris@downey.com");
		
		List<Friendship> friendships = user.friendships;
		Friendship friendship = new Friendship(user, anotherUser);
		friendships.add(friendship);
		
		assertEquals(friendships.size(), 1);
		assertEquals(friendships.get(0).targetUser, anotherUser);
	}
	
	@Test
	public void testAddingMultipleFriends(){
		User userOne = User.findByEmail("bob@jones.com");
		User userTwo = User.findByEmail("chris@downey.com");
		User userThree = User.findByEmail("mark@rowe.com");
		
		List<Friendship> friendships = userOne.friendships;
		Friendship friendship1 = new Friendship(userOne,userTwo);
		friendships.add(friendship1);
		assertEquals(friendships.size(), 1);
		Friendship friendship2 = new Friendship(userOne,userThree);
		friendships.add(friendship2);
		
		assertEquals(friendships.size(), 2);
		assertEquals(friendships.get(0).targetUser, userTwo);
		assertEquals(friendships.get(1).targetUser, userThree);
		assertNotSame(userTwo, friendships.get(1).targetUser);
		assertNotSame(userThree, friendships.get(0).targetUser);
	}
	
	@Test
	public void testFindFriendships() {
		User user = User.findByEmail("bob@jones.com");
		User anotherUser = User.findByEmail("chris@downey.com");
		
		List<Friendship> friendships = user.friendships;
		Friendship friendship = new Friendship(user, anotherUser);
		friendships.add(friendship);
		
		assertNotNull(friendships);
	}
	
	@Test
	public void testUpdateFriendship() {
		User user = User.findByEmail("bob@jones.com");
		User anotherUser = User.findByEmail("chris@downey.com");
		User yetAnotherUser = User.findByEmail("mark@rowe.com");
		
		List<Friendship> friendships = user.friendships;
		Friendship friendship = new Friendship(user, anotherUser);
		friendships.add(friendship);
		
		assertEquals(friendships.get(0).targetUser, anotherUser);
		
		friendship.targetUser = yetAnotherUser;
		assertEquals(friendships.get(0).targetUser, yetAnotherUser);
		assertNotSame(anotherUser, friendships.get(0).targetUser);
	}
	
	@Test
	public void testDeleteFriendship() {
		User userOne = User.findByEmail("bob@jones.com");
		User userTwo = User.findByEmail("chris@downey.com");
		User userThree = User.findByEmail("mark@rowe.com");
		
		List<Friendship> friendships = userOne.friendships;
		Friendship friendship1 = new Friendship(userOne,userTwo);
		friendships.add(friendship1);
		assertEquals(friendships.size(), 1);
		Friendship friendship2 = new Friendship(userOne,userThree);
		friendships.add(friendship2);
		
		assertEquals(friendships.size(), 2);
		assertEquals(friendships.get(0).targetUser, userTwo);
		assertEquals(friendships.get(1).targetUser, userThree);
		
		friendships.remove(friendship1);
		assertEquals(friendships.size(), 1);
		friendships.remove(friendship2);
		assertEquals(friendships.size(), 0);
	}
}
