package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import org.junit.Test;

import structures.QuickQueue;

public class QuickQueueTest {
	
	@Test
	public void testAdd() {
		QuickQueue<String> q = new QuickQueue<String>();
		
		assertTrue(q.isEmpty());
		
		assertEquals(null, q.peek());
		
		try {
			q.element();
			fail();
		} catch (NoSuchElementException e){
			;;
		}
		
		assertEquals(null, q.poll());
		
		try {
			q.remove();
			fail();
		} catch (NoSuchElementException e){
			;;
		}
		
		
		
		
		q.add("A");
		q.add("really");
		q.add("big");
		q.add("foo");
		q.offer("bar");

		
		assertFalse(q.isEmpty());
		assertEquals(5, q.size());
		
		q.clear();
		assertTrue(q.isEmpty());
		assertEquals(0, q.size());
		
		ArrayList<String> arr = new ArrayList<String>();
		arr.add("A");
		arr.add("really");
		arr.add("big");
		arr.add("foo");
		arr.add("bar");
		
		q.addAll(arr);
		assertFalse(q.isEmpty());
		assertEquals(5, q.size());
		
		assertTrue(q.containsAll(arr));
		
		String liststring = "[A, really, big, foo, bar]";
		assertEquals(liststring, q.toString());
		assertEquals(5, q.size());
		
		q.add("really");		
		liststring = "[A, big, foo, bar, really]";
		assertEquals(liststring, q.toString());
		assertEquals(5, q.size());
		
		q.add("A");		
		liststring = "[big, foo, bar, really, A]";
		assertEquals(liststring, q.toString());
		assertEquals(5, q.size());
		
		q.offer("A");		
		liststring = "[big, foo, bar, really, A]";
		assertEquals(liststring, q.toString());
		assertEquals(5, q.size());
		
		String[] strings = {"big", "foo","bar", "really", "A"};		
		Object[] objarr = q.toArray();
		
		assertEquals(strings.length, objarr.length);
		for (int i  = 0; i < objarr.length; i++){
			assertEquals(strings[i], objarr[i]);
		}
		
		String[] stringarr = (String[]) q.toArray(new String[0]);
		
		
		assertEquals(strings.length, stringarr.length);
		for (int i  = 0; i < stringarr.length; i++){
			assertEquals(strings[i], stringarr[i]);
		}
				
		assertEquals("big", q.element());
		assertEquals("big", q.peek());
		assertEquals(5, q.size());
		
		assertEquals("big", q.remove());
		assertEquals(4, q.size());
		
		assertFalse(q.containsAll(arr));
		
		assertEquals("foo", q.poll());
		assertEquals(3, q.size());
				
		try {
			q.update("nope");
			fail();
		} catch (NoSuchElementException e){
			;;
		}
		
		liststring = "[bar, really, A]";
		assertEquals(liststring, q.toString());
		assertEquals(3, q.size());
		
		assertFalse(q.remove("whoops"));
		
		q.add("whoops");
		
		assertTrue(q.removeAll(arr));
		
		assertEquals("[whoops]", q.toString());
		assertEquals(1, q.size());
		
		assertFalse(q.removeAll(arr));
		
		assertTrue(q.add("big"));
		assertTrue(q.offer("foo"));
		
		assertTrue(q.retainAll(arr));
		assertEquals("[big, foo]", q.toString());
		assertEquals(2, q.size());
		
		q.offer("whoops");
		assertEquals("[big, foo, whoops]", q.toString());
		assertEquals(3, q.size());
		
		assertTrue(q.contains("whoops"));
		
		assertTrue(q.remove("whoops"));
		
		assertFalse(q.contains("whoops"));
		
	}
	
	

	@Test
	public void testCapacityAdd() {
		QuickQueue<String> q = new QuickQueue<String>(5);
		
		assertTrue(q.isEmpty());
		
		assertEquals(null, q.peek());
		
		try {
			q.element();
			fail();
		} catch (NoSuchElementException e){
			;;
		}
		
		assertEquals(null, q.poll());
		
		try {
			q.remove();
			fail();
		} catch (NoSuchElementException e){
			;;
		}
		
		
		
		
		q.add("A");
		q.add("really");
		q.add("big");
		q.add("foo");
		q.offer("bar");
		
		assertEquals(false, q.offer("pass"));
		
		try {
			q.add("nope");
			fail();
		} catch (IllegalStateException e){
			;;
		}
		
		assertFalse(q.isEmpty());
		assertEquals(5, q.size());
		
		q.clear();
		assertTrue(q.isEmpty());
		assertEquals(0, q.size());
		
		ArrayList<String> arr = new ArrayList<String>();
		arr.add("A");
		arr.add("really");
		arr.add("big");
		arr.add("foo");
		arr.add("bar");
		
		q.addAll(arr);
		assertFalse(q.isEmpty());
		assertEquals(5, q.size());
		
		assertTrue(q.containsAll(arr));
		
		String liststring = "[A, really, big, foo, bar]";
		assertEquals(liststring, q.toString());
		assertEquals(5, q.size());
		
		q.add("really");		
		liststring = "[A, big, foo, bar, really]";
		assertEquals(liststring, q.toString());
		assertEquals(5, q.size());
		
		q.add("A");		
		liststring = "[big, foo, bar, really, A]";
		assertEquals(liststring, q.toString());
		assertEquals(5, q.size());
		
		q.offer("A");		
		liststring = "[big, foo, bar, really, A]";
		assertEquals(liststring, q.toString());
		assertEquals(5, q.size());
		
		String[] strings = {"big", "foo","bar", "really", "A"};		
		Object[] objarr = q.toArray();
		
		assertEquals(strings.length, objarr.length);
		for (int i  = 0; i < objarr.length; i++){
			assertEquals(strings[i], objarr[i]);
		}
		
		String[] stringarr = (String[]) q.toArray(new String[0]);
		
		
		assertEquals(strings.length, stringarr.length);
		for (int i  = 0; i < stringarr.length; i++){
			assertEquals(strings[i], stringarr[i]);
		}
				
		assertEquals("big", q.element());
		assertEquals("big", q.peek());
		assertEquals(5, q.size());
		
		assertEquals("big", q.remove());
		assertEquals(4, q.size());
		
		assertFalse(q.containsAll(arr));
		
		assertEquals("foo", q.poll());
		assertEquals(3, q.size());
				
		try {
			q.update("nope");
			fail();
		} catch (NoSuchElementException e){
			;;
		}
		
		liststring = "[bar, really, A]";
		assertEquals(liststring, q.toString());
		assertEquals(3, q.size());
		
		assertFalse(q.remove("whoops"));
		
		q.add("whoops");
		
		assertTrue(q.removeAll(arr));
		
		assertEquals("[whoops]", q.toString());
		assertEquals(1, q.size());
		
		assertFalse(q.removeAll(arr));
		
		assertTrue(q.add("big"));
		assertTrue(q.offer("foo"));
		
		assertTrue(q.retainAll(arr));
		assertEquals("[big, foo]", q.toString());
		assertEquals(2, q.size());
		
		q.offer("whoops");
		assertEquals("[big, foo, whoops]", q.toString());
		assertEquals(3, q.size());
		
		assertTrue(q.contains("whoops"));
		
		assertTrue(q.remove("whoops"));
		
		assertFalse(q.contains("whoops"));
		
		q.clear();
		q.add("A");
		q.add("really");
		q.add("big");
		q.add("foo");
		q.offer("bar");
		liststring = "[A, really, big, foo, bar]";
		assertEquals(liststring, q.toString());
		assertEquals(5, q.size());
		
		q.offer("push", true);
		liststring = "[really, big, foo, bar, push]";
		assertEquals(liststring, q.toString());
		assertEquals(5, q.size());
		
		q.offer("push", true);
		liststring = "[really, big, foo, bar, push]";
		assertEquals(liststring, q.toString());
		assertEquals(5, q.size());
		
		q.offer("push", false);
		liststring = "[really, big, foo, bar, push]";
		assertEquals(liststring, q.toString());
		assertEquals(5, q.size());
		
		q.offer("nope", false);
		liststring = "[really, big, foo, bar, push]";
		assertEquals(liststring, q.toString());
		assertEquals(5, q.size());
		
		q.offer("really", true);
		liststring = "[big, foo, bar, push, really]";
		assertEquals(liststring, q.toString());
		assertEquals(5, q.size());
		
		q.offer("big", false);
		liststring = "[foo, bar, push, really, big]";
		assertEquals(liststring, q.toString());
		assertEquals(5, q.size());
		
		
		
	}
	
	

}
