package structures;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

public class QuickQueue<E> implements Queue<E> {

	DoubleLinkedList<E> list;
	HashMap<E, Node<E>> map;
	boolean hascapacity;
	int capacity;
	

	public QuickQueue() {
		this.list = new DoubleLinkedList<E>();
		this.map = new HashMap<E, Node<E>>();
		this.hascapacity = false;
		this.capacity = -1;		
	}
	
	public QuickQueue(int capacity){
		this.list = new DoubleLinkedList<E>();
		this.map = new HashMap<E, Node<E>>();
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
		while(iter.hasNext()){
			curr = iter.next();
			if (!c.contains(curr)){
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
	 * Updates the object in the queue by removing if it exists
	 * and appending it to the end of the queue
	 * 
	 * @param o	The object to update in the queue
	 * @throws	NoSuchElementException	if the object does not exist in the queue
	 * @throws	ClassCastException		if the class of the specified element prevents it from being added to this queue
	 * @throws 
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
			if (this.hascapacity && this.size() == this.capacity)
				return false; 
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

}
