package structures;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Similar to the QuickQueue class but handles concurrent access to the data structure
 */
public class ConcurrentQuickQueue<E> extends QuickQueue<E> implements Serializable {

	static final long serialVersionUID = 1;

	ConcurrentDoubleLinkedList<E> list;
	ConcurrentHashMap<E, Node<E>> map;
	boolean hascapacity;
	int capacity;

	/***
	 * Default constructor for the ConcurrentQuickQueue that provides a queue without a capacity
	 */
	public ConcurrentQuickQueue() {
		this.list = new ConcurrentDoubleLinkedList<E>();
		this.map = new ConcurrentHashMap<E, Node<E>>();
		this.hascapacity = false;
		this.capacity = -1;
	}

	/***
	 * Constructor for the ConcurrentQuickQueue. Capacity sets the capacity of the queue.
	 * 
	 * @param capacity
	 */
	public ConcurrentQuickQueue(int capacity) {
		this.list = new ConcurrentDoubleLinkedList<E>();
		this.map = new ConcurrentHashMap<E, Node<E>>();
		this.hascapacity = true;
		this.capacity = capacity;
	}

	@Override
	public boolean addAll(Collection c) {
		Node<E> n;
		E e;
		for (Object o : c) {
			add(o);
		}
		return true;
	}

	@Override
	public void clear() {
		this.map.clear();
		this.list.clear();

	}

	@Override
	public boolean contains(Object o) {
		return map.containsKey(o);
	}

	@Override
	public boolean containsAll(Collection c) {
		E e;
		for (Object o : c) {
			e = (E) o;
			if (!map.containsKey(e))
				return false;
		}
		return true;
	}

	@Override
	public boolean isEmpty() {
		return map.isEmpty();
	}

	@Override
	public Iterator iterator() {
		return list.iterator();
	}

	@Override
	public boolean remove(Object o) {
		E e = (E) o;
		Node<E> n = map.remove(e);
		if (n != null) {
			list.removeNode(n);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean removeAll(Collection c) {
		boolean ret = false;
		for (Object o : c) {
			ret = remove(o) || ret;
		}
		return ret;
	}

	@Override
	public boolean retainAll(Collection c) {
		boolean modified = false;
		Iterator<E> iter = iterator();
		E curr;
		while (iter.hasNext()) {
			curr = iter.next();
			if (!c.contains(curr)) {
				iter.remove();
				map.remove(curr);
				modified = true;
			}
		}
		return modified;

	}

	@Override
	public int size() {
		return map.size();
	}

	@Override
	public Object[] toArray() {
		return list.toArray();
	}

	@Override
	public Object[] toArray(Object[] o) {
		return list.toArray(o);
	}

	@Override
	public boolean add(Object o) {

		if (map.get(o) == null) {
			if (this.hascapacity && this.size() == this.capacity)
				throw new IllegalStateException();
			E e = (E) o;
			Node<E> n = new Node<E>(e);
			list.add(n);
			map.put(e, n);
		} else {
			update(o);
		}
		return true;
	}

	/***
	 * Updates the object in the queue by removing if it exists and appending it
	 * to the end of the queue
	 * 
	 * @param o
	 *            The object to update in the queue @throws
	 *            NoSuchElementException if the object does not exist in the
	 *            queue @throws ClassCastException if the class of the specified
	 *            element prevents it from being added to this queue
	 * @throws NoSuchElementException
	 *             if the queue does not have the specified object
	 */
	public void update(Object o) {
		Node<E> n = map.get(o);
		if (n == null)
			throw new NoSuchElementException();
		list.removeNode(n);
		list.add(n);
	}

	@Override
	public E element() {
		if (list.size() == 0)
			throw new NoSuchElementException();

		return list.getHead().value;
	}

	@Override
	public boolean offer(Object o) {
		if (map.get(o) == null) {
			if (this.hascapacity && this.size() == this.capacity) {
				return false;
			}

			E e = (E) o;
			Node<E> n = new Node<E>(e);
			list.add(n);
			map.put(e, n);
		} else {
			update(o);
		}
		return true;
	}

	/**
	 * Has the same properties as the standard offer method when evict is false,
	 * but when evict is true, the head of the queue (the least recently used
	 * element) is removed to make room for the offered object.
	 * 
	 * @param o
	 *            the object to be added to the queue
	 * @param evict
	 *            specifies whether or not to remove the LRU element if the
	 *            queue is full
	 * @return true if the queue was altered as a result of this method call.
	 *         false otherwise.
	 */
	public boolean offer(Object o, boolean evict) {
		if (map.get(o) == null) {
			if (this.hascapacity && this.size() == this.capacity) {
				if (evict) {
					remove();
				} else {
					return false;
				}
			}

			E e = (E) o;
			Node<E> n = new Node<E>(e);
			list.add(n);
			map.put(e, n);
		} else {
			update(o);
		}
		return true;
	}

	@Override
	public E peek() {
		return (list.getHead() != null ? list.getHead().value : null);
	}

	@Override
	public E poll() {
		if (list.size() == 0)
			return null;

		E toremove = list.remove(0);
		map.remove(toremove);
		return toremove;
	}

	@Override
	public E remove() {
		if (list.size() == 0)
			throw new NoSuchElementException();

		E toremove = list.remove(0);
		map.remove(toremove);
		return toremove;
	}

	public String toString() {
		return list.toString();
	}

	/*
	 * 
	 * public static void main(String[] args){ Runnable runnable = new
	 * Runnable() { public void run() {
	 * System.out.println("Hello in the future - " + new Date()); } }; //create
	 * anonymous inner class that implements Runnable
	 * 
	 * //create a threadpool with 5 available threads ScheduledExecutorService
	 * futurethreads = Executors.newScheduledThreadPool(5); //Launch the thread
	 * at 10 seconds in the future futurethreads.schedule( runnable, 10,
	 * TimeUnit.SECONDS ); System.out.println("Hello from now - " + new Date());
	 * 
	 * 
	 * }
	 */

}
