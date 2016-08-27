package tests;

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import structures.DoubleLinkedList;
import structures.Node;

public class DoubleLinkedListTest {
	
	@Rule
    public ExpectedException thrown= ExpectedException.none();

	@Test
	public void testAdd() {
		DoubleLinkedList<String> dll = new DoubleLinkedList<String>();
		dll.add("foo");
		assertEquals(1, dll.size());
		assertEquals("foo", dll.get(0));
		assertFalse(dll.isEmpty());

		try {
			dll.get(1);
			fail();
		} catch (IndexOutOfBoundsException e) {
		}
		try {
			dll.get(-1);
			fail();
		} catch (IndexOutOfBoundsException e) {
		}

		dll.add("bar");
		assertEquals(2, dll.size());
		assertEquals("foo", dll.get(0));
		assertEquals("bar", dll.get(1));

		try {
			dll.get(2);
			fail();
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
		
		dll.clear();
		
		dll.add("A");
		dll.add("really");
		dll.add("big");
		dll.add("foo");
		dll.add("bar");
		
		Node<String> n = new Node<String>("fiddle");
		Node<String> n2 = new Node<String>("stick");
		Node<String> n3 = new Node<String>("pepper");
		Node<String> n4 = new Node<String>("giga");
		
		dll.add(0, n);
		
		String liststring = "[fiddle, A, really, big, foo, bar]";
		assertEquals(liststring, dll.toString());
		assertEquals(6, dll.size());
		
		dll.add(2, n2);
		
		liststring = "[fiddle, A, stick, really, big, foo, bar]";
		assertEquals(liststring, dll.toString());
		assertEquals(7, dll.size());
		
		
		dll.add(n3);
		
		liststring = "[fiddle, A, stick, really, big, foo, bar, pepper]";
		assertEquals(liststring, dll.toString());
		assertEquals(8, dll.size());
		
		dll.add(dll.size(), n4);
		
		liststring = "[fiddle, A, stick, really, big, foo, bar, pepper, giga]";
		assertEquals(liststring, dll.toString());
		assertEquals(9, dll.size());
		
		dll.add(dll.size(), "byte");
		
		liststring = "[fiddle, A, stick, really, big, foo, bar, pepper, giga, byte]";
		assertEquals(liststring, dll.toString());
		assertEquals(10, dll.size());
		
		
		dll.add(6, "deep");
		liststring = "[fiddle, A, stick, really, big, foo, deep, bar, pepper, giga, byte]";
		assertEquals(liststring, dll.toString());
		assertEquals(11, dll.size());
		
		
		dll.add(4, "small");
		liststring = "[fiddle, A, stick, really, small, big, foo, deep, bar, pepper, giga, byte]";
		assertEquals(liststring, dll.toString());
		assertEquals(12, dll.size());
		
		dll.add(8, "huh");
		liststring = "[fiddle, A, stick, really, small, big, foo, deep, huh, bar, pepper, giga, byte]";
		assertEquals(liststring, dll.toString());
		assertEquals(13, dll.size());
		
		dll.add(9, new Node<String>("oh"));
		liststring = "[fiddle, A, stick, really, small, big, foo, deep, huh, oh, bar, pepper, giga, byte]";
		assertEquals(liststring, dll.toString());
		assertEquals(14, dll.size());
		
		try {dll.add(20, "nope"); fail();} catch(IndexOutOfBoundsException e){}
		try {dll.add(-1, "nope"); fail();} catch(IndexOutOfBoundsException e){}
		try {dll.add(20, new Node<String>("nope")); fail();} catch(IndexOutOfBoundsException e){}
		try {dll.add(-1, new Node<String>("nope")); fail();} catch(IndexOutOfBoundsException e){}
		
		assertEquals("byte", dll.getTail().getValue());
		
		assertEquals(1, dll.indexOf("A"));
		assertEquals(-1, dll.indexOf(null));
		assertEquals(-1, dll.lastIndexOf(null));
		n4.setValue(null);
		assertEquals(12, dll.indexOf(null));
		assertEquals(12, dll.lastIndexOf(null));
		assertTrue(dll.remove(null));
		assertFalse(dll.remove("billy"));
		
		ListIterator<String> iter = dll.listIterator();
		
		try {iter.remove(); fail();} catch(IllegalStateException e){}
		try {iter.set("nope"); fail();} catch(IllegalStateException e){}
		try {iter.previous(); fail();} catch(NoSuchElementException e){}
		
		iter.add("new");
		liststring = "[new, fiddle, A, stick, really, small, big, foo, deep, huh, oh, bar, pepper, byte]";
		assertEquals(liststring, dll.toString());
		assertEquals(14, dll.size());
		
		iter.add("what");
		liststring = "[new, what, fiddle, A, stick, really, small, big, foo, deep, huh, oh, bar, pepper, byte]";
		assertEquals(liststring, dll.toString());
		assertEquals(15, dll.size());
		
		iter = dll.listIterator(2);
		assertEquals("fiddle", iter.next());
		iter = dll.listIterator(12);
		assertEquals("bar", iter.next());
		iter.set("bat");
		
		liststring = "[new, what, fiddle, A, stick, really, small, big, foo, deep, huh, oh, bat, pepper, byte]";
		assertEquals(liststring, dll.toString());
		assertEquals(15, dll.size());
		
		while(iter.hasNext()){
			iter.next();
		}
		try {iter.next(); fail();} catch(NoSuchElementException e){}
		
		try{dll.listIterator(20); fail();}catch(IndexOutOfBoundsException e){}
		
		try{dll.remove(-1); fail();}catch(IndexOutOfBoundsException e){}
		
		try{dll.remove(20); fail();}catch(IndexOutOfBoundsException e){}
		
		assertEquals("really", dll.remove(5));
		assertEquals(14, dll.size());
		
		
		
		dll.clear();
		assertEquals(0, dll.size());
		assertEquals(null, dll.getHead());
		assertEquals(null, dll.getTail());
		
		iter = dll.listIterator();
		
		iter.add("crystal");
		
		liststring = "[crystal]";
		assertEquals(liststring, dll.toString());
		
		
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
