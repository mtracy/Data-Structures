package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import structures.ConcurrentDoubleLinkedList;
import structures.DoubleLinkedList;
import structures.Node;
import java.util.concurrent.*;

public class ConcurrentDoubleLinkedListTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void testAdd() {
		ConcurrentDoubleLinkedList<String> dll = new ConcurrentDoubleLinkedList<String>();
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

		try {
			dll.add(20, "nope");
			fail();
		} catch (IndexOutOfBoundsException e) {
		}
		try {
			dll.add(-1, "nope");
			fail();
		} catch (IndexOutOfBoundsException e) {
		}
		try {
			dll.add(20, new Node<String>("nope"));
			fail();
		} catch (IndexOutOfBoundsException e) {
		}
		try {
			dll.add(-1, new Node<String>("nope"));
			fail();
		} catch (IndexOutOfBoundsException e) {
		}

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

		try {
			iter.remove();
			fail();
		} catch (UnsupportedOperationException e) {
		}
		try {
			iter.set("nope");
			fail();
		} catch (UnsupportedOperationException e) {
		}
		try {
			iter.add("nope");
			fail();
		} catch (UnsupportedOperationException e) {
		}
		try {
			iter.previous();
			fail();
		} catch (NoSuchElementException e) {
		}

	}

	@Test
	public void testIter() {
		ConcurrentDoubleLinkedList<String> dll = new ConcurrentDoubleLinkedList<String>();
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
		ConcurrentDoubleLinkedList<String> dll = new ConcurrentDoubleLinkedList<String>();

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
		ConcurrentDoubleLinkedList<String> dll = new ConcurrentDoubleLinkedList<String>();

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

		ConcurrentDoubleLinkedList<String> other = new ConcurrentDoubleLinkedList<String>();

		other.add("goof");
		other.add("bar");
		other.add("rather");
		other.add("big");
		other.add("A");

	}

	@Test
	public void testConcurrentAdd() {
		ConcurrentDoubleLinkedList<String> dll = new ConcurrentDoubleLinkedList<String>();
		ArrayList<ThreadOpp> threads = new ArrayList<ThreadOpp>();
		threads.add(new ReadThread(dll, "A"));
		threads.add(new AddThread(dll, "A"));
		threads.add(new ReadThread(dll, "really"));
		threads.add(new RemoveThread(dll, "A"));
		threads.add(new AddThread(dll, "really"));
		threads.add(new AddThread(dll, "big"));
		threads.add(new AddThread(dll, "foo"));
		threads.add(new ReadThread(dll, "big"));
		threads.add(new ReadThread(dll, "foo"));
		threads.add(new RemoveThread(dll, "big"));
		threads.add(new AddThread(dll, "bar"));
		threads.add(new ReadThread(dll, "bar"));

		ExecutorService addexec = Executors.newFixedThreadPool(4);

		try {
			List<Future<Boolean>> results = addexec.invokeAll(threads);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		assertTrue(dll.contains("really"));
		assertTrue(dll.contains("foo"));
		assertTrue(dll.contains("bar"));
		System.out.println(dll);

	}

	abstract class ThreadOpp implements Callable<Boolean> {
		ConcurrentDoubleLinkedList<String> dll;
		String s;

		public ThreadOpp(ConcurrentDoubleLinkedList<String> dll, String s) {
			this.dll = dll;
			this.s = s;
		}

		abstract public Boolean call() throws Exception;
	}

	class AddThread extends ThreadOpp {

		public AddThread(ConcurrentDoubleLinkedList<String> dll, String s) {
			super(dll, s);
		}

		@Override
		public Boolean call() throws Exception {
			System.out.println("Add " + s);
			return dll.add(s);
		}
	}

	class ReadThread extends ThreadOpp {

		public ReadThread(ConcurrentDoubleLinkedList<String> dll, String s) {
			super(dll, s);
		}

		@Override
		public Boolean call() throws Exception {
			System.out.println("Read " + s);
			return dll.contains(s);
		}
	}

	class RemoveThread extends ThreadOpp {

		public RemoveThread(ConcurrentDoubleLinkedList<String> dll, String s) {
			super(dll, s);
		}

		@Override
		public Boolean call() throws Exception {
			System.out.println("Remove " + s);
			return dll.remove(s);
		}
	}

}
