package tests;

import static org.junit.Assert.*;

import java.util.ListIterator;

import org.junit.Test;

import structures.DoubleLinkedList;

public class DoubleLinkedListTest {

	@Test
	public void testAdd() {
		DoubleLinkedList<String> dll = new DoubleLinkedList<String>();
		dll.add("foo");
		assertEquals(1, dll.size());
		assertEquals("foo", dll.get(0));
		assertFalse(dll.isEmpty());

		try {
			dll.get(1);
		} catch (IndexOutOfBoundsException e) {
		}
		try {
			dll.get(-1);
		} catch (IndexOutOfBoundsException e) {
		}

		dll.add("bar");
		assertEquals(2, dll.size());
		assertEquals("foo", dll.get(0));
		assertEquals("bar", dll.get(1));

		try {
			dll.get(2);
		} catch (IndexOutOfBoundsException e) {
		}

		dll.add(0, "A");
		assertEquals(3, dll.size());
		assertEquals("A", dll.get(0));
		assertEquals("foo", dll.get(1));
		assertEquals("bar", dll.get(2));

		dll.add(1, "big");
		assertEquals(4, dll.size());
		assertEquals("A", dll.get(0));
		assertEquals("big", dll.get(1));
		assertEquals("foo", dll.get(2));
		assertEquals("bar", dll.get(3));

		dll.add(1, "really");
		assertEquals(5, dll.size());
		assertEquals("A", dll.get(0));
		assertEquals("really", dll.get(1));
		assertEquals("big", dll.get(2));
		assertEquals("foo", dll.get(3));
		assertEquals("bar", dll.get(4));

		dll.clear();
		assertEquals(0, dll.size());
		assertTrue(dll.isEmpty());

		dll.add("new");
		assertEquals(1, dll.size());
		assertEquals("new", dll.get(0));

		dll.add("list");
		assertEquals(2, dll.size());
		assertEquals("new", dll.get(0));
		assertEquals("list", dll.get(1));

		DoubleLinkedList<String> other = new DoubleLinkedList<String>();
		other.add("A");
		other.add("really");
		other.add("big");
		other.add("foo");
		other.add("bar");

		dll.addAll(1, other);

		assertEquals("new", dll.get(0));
		assertEquals("list", dll.get(6));
		assertEquals("A", dll.get(1));
		assertEquals("really", dll.get(2));
		assertEquals("big", dll.get(3));
		assertEquals("foo", dll.get(4));
		assertEquals("bar", dll.get(5));
	}

	@Test
	public void testIter() {
		DoubleLinkedList<String> dll = new DoubleLinkedList<String>();
		dll.add("A");
		dll.add("really");
		dll.add("big");
		dll.add("foo");
		dll.add("bar");

		ListIterator<String> iter = dll.listIterator(dll.size());
		assertTrue(iter.hasPrevious());
		assertFalse(iter.hasNext());
		assertEquals("bar", iter.previous());
		assertTrue(iter.hasPrevious());
		assertTrue(iter.hasNext());
		assertEquals("bar", iter.next());
		assertTrue(iter.hasPrevious());
		assertFalse(iter.hasNext());
		assertEquals(dll.size() - 1, iter.previousIndex());
		assertEquals(dll.size(), iter.nextIndex());
		assertEquals("bar", iter.previous());
		assertEquals("foo", iter.previous());
		assertEquals("big", iter.previous());
		assertEquals("really", iter.previous());
		assertEquals("A", iter.previous());
		assertFalse(iter.hasPrevious());
		assertTrue(iter.hasNext());
		assertEquals(-1, iter.previousIndex());
		assertEquals(0, iter.nextIndex());
		assertEquals("A", iter.next());
	}

	@Test
	public void testIndexOf() {
		DoubleLinkedList<String> dll = new DoubleLinkedList<String>();

		dll.add("A");
		dll.add("really");
		dll.add("big");
		dll.add("foo");
		dll.add("bar");

		dll.add("A");
		dll.add("really");
		dll.add("big");
		dll.add("foo");
		dll.add("bar");

		assertTrue(dll.contains("A"));
		assertTrue(dll.contains("really"));
		assertTrue(dll.contains("big"));
		assertTrue(dll.contains("foo"));
		assertTrue(dll.contains("bar"));

		assertFalse(dll.contains("nope"));

		assertEquals(0, dll.indexOf("A"));
		assertEquals(1, dll.indexOf("really"));
		assertEquals(2, dll.indexOf("big"));
		assertEquals(3, dll.indexOf("foo"));
		assertEquals(4, dll.indexOf("bar"));

		assertEquals(5, dll.lastIndexOf("A"));
		assertEquals(6, dll.lastIndexOf("really"));
		assertEquals(7, dll.lastIndexOf("big"));
		assertEquals(8, dll.lastIndexOf("foo"));
		assertEquals(9, dll.lastIndexOf("bar"));

	}

	@Test
	public void testRemove() {
		DoubleLinkedList<String> dll = new DoubleLinkedList<String>();

		dll.add("A");
		dll.add("really");
		dll.add("big");
		dll.add("foo");
		dll.add("bar");

		dll.remove("big");
		
		assertEquals(4, dll.size());
		assertEquals("A", dll.get(0));
		assertEquals("really", dll.get(1));
		assertEquals("foo", dll.get(2));
		assertEquals("bar", dll.get(3));
		
		dll.remove("A");
		
		assertEquals(3, dll.size());
		assertEquals("really", dll.get(0));
		assertEquals("foo", dll.get(1));
		assertEquals("bar", dll.get(2));
		
		dll.remove("bar");
		
		assertEquals(2, dll.size());
		assertEquals("really", dll.get(0));
		assertEquals("foo", dll.get(1));
		
		dll.remove("really");
		assertEquals(1, dll.size());
		assertEquals("foo", dll.get(0));
		
		dll.remove("foo");
		
		assertEquals(0, dll.size());
		assertTrue(dll.isEmpty());
		
		dll.add("A");
		dll.add("really");
		dll.add("big");
		dll.add("foo");
		dll.add("bar");
		
		dll.remove(2);
		
		assertEquals(4, dll.size());
		assertEquals("A", dll.get(0));
		assertEquals("really", dll.get(1));
		assertEquals("foo", dll.get(2));
		assertEquals("bar", dll.get(3));
		
		dll.remove(0);
		
		assertEquals(3, dll.size());
		assertEquals("really", dll.get(0));
		assertEquals("foo", dll.get(1));
		assertEquals("bar", dll.get(2));
		
		dll.remove(2);
		
		assertEquals(2, dll.size());
		assertEquals("really", dll.get(0));
		assertEquals("foo", dll.get(1));
		
		dll.remove(0);
		assertEquals(1, dll.size());
		assertEquals("foo", dll.get(0));
		
		dll.remove(0);
		
		assertEquals(0, dll.size());
		assertTrue(dll.isEmpty());
		
		dll.add("A");
		dll.add("really");
		dll.add("big");
		dll.add("foo");
		dll.add("bar");
		
		DoubleLinkedList<String> other = new DoubleLinkedList<String>();

		other.add("goof");
		other.add("bar");
		other.add("rather");
		other.add("big");
		other.add("A");
		
		
		dll.removeAll(other);
		
		assertEquals(2, dll.size());
		assertEquals("really", dll.get(0));
		assertEquals("foo", dll.get(1));
		
		dll.clear();
		dll.add("A");
		dll.add("really");
		dll.add("big");
		dll.add("foo");
		dll.add("bar");
		
		dll.retainAll(other);
		
		assertEquals(3, dll.size());
		assertEquals("A", dll.get(0));
		assertEquals("big", dll.get(1));
		assertEquals("bar", dll.get(2));
		
		dll.clear();
		dll.add("A");
		dll.add("really");
		dll.add("big");
		dll.add("foo");
		dll.add("bar");
		
		other.clear();
		other.add("foo");
		other.add("nonsense");
		other.add("really");
		other.add("bar");
		
		dll.removeAll(other);
		
		assertEquals(2, dll.size());
		assertEquals("A", dll.get(0));
		assertEquals("big", dll.get(1));
		
		dll.clear();
		dll.add("A");
		dll.add("really");
		dll.add("big");
		dll.add("foo");
		dll.add("bar");
		
		dll.retainAll(other);
		
		assertEquals(3, dll.size());
		assertEquals("really", dll.get(0));
		assertEquals("foo", dll.get(1));
		assertEquals("bar", dll.get(2));
		
		

	}
	
	

}
