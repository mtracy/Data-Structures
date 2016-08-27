package structures;

import java.io.Serializable;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ConcurrentDoubleLinkedList<E> extends DoubleLinkedList<E> implements List<E>, Serializable {

	static final long serialVersionUID = 1;

	private Node<E> head;
	private Node<E> tail;
	private int size;
	ReadWriteLock rwLock = new ReentrantReadWriteLock(false);

	private int writecount = 0;

	public ConcurrentDoubleLinkedList() {
		this.head = null;
		this.tail = null;
		this.size = 0;

	}

	public Node<E> getHead() {
		try {
			rwLock.readLock().lock();
			Node<E> head = this.head;
			return head;
		} finally {
			rwLock.readLock().unlock();
		}
	}

	public void setHead(Node<E> head) {
		try {
			rwLock.writeLock().lock();

			this.head = head;
		} finally {
			writecount++;
			rwLock.writeLock().unlock();
		}
	}

	public Node<E> getTail() {
		try {
			rwLock.readLock().lock();
			Node<E> tail = this.tail;
			return tail;
		} finally {
			rwLock.readLock().unlock();
		}
	}

	public void setTail(Node<E> tail) {
		try {
			rwLock.writeLock().lock();
			this.tail = tail;
		} finally {
			writecount++;
			rwLock.writeLock().unlock();
		}
	}

	@Override
	public boolean add(E elt) {
		try {
			rwLock.writeLock().lock();
			if (this.head == null) {
				setHead(new Node<E>(elt));
				setTail(this.head);
			} else {
				this.tail.next = new Node<E>(elt);
				this.tail.next.prev = this.tail;
				this.tail = this.tail.next;
			}
			this.size++;
			return true;
		} finally {
			writecount++;
			rwLock.writeLock().unlock();
		}
	}

	public boolean add(Node<E> n) {
		try {
			rwLock.writeLock().lock();
			if (this.head == null) {
				this.head = n;
				this.tail = this.head;
			} else {
				this.tail.next = n;
				n.prev = this.tail;
				n.next = null;
				this.tail = n;

			}
			this.size++;
			return true;
		} finally {
			writecount++;
			rwLock.writeLock().unlock();
		}

	}

	@Override
	public void add(int index, E elt) {
		try {
			rwLock.writeLock().lock();

			if (index < 0 || index > this.size) {
				throw new IndexOutOfBoundsException("Specified index " + index + " is greater than size " + size);
			}

			if (index == 0) {
				Node<E> newhead = new Node<E>(elt);
				newhead.next = this.head;
				this.head.prev = newhead;
				this.head = newhead;
			} else if (index == this.size) {
				Node<E> newtail = new Node<E>(elt);
				newtail.prev = this.tail;
				this.tail.next = newtail;
				this.tail = newtail;
			} else {
				Node<E> curr;
				if (index < (size >> 1)) {
					for (curr = this.head; index > 1; index--) {
						curr = curr.next;
					}
				} else {
					index = this.size - 1 - index;
					for (curr = this.tail; index >= 0; index--) {
						curr = curr.prev;
					}
				}

				Node<E> temp = curr.next;
				curr.next = new Node<E>(elt);
				curr.next.next = temp;
				curr.next.prev = curr;
				temp.prev = curr.next;
			}
			this.size++;
		} finally {
			writecount++;
			rwLock.writeLock().unlock();
		}
	}

	public void add(int index, Node<E> n) {
		try {
			rwLock.writeLock().lock();
			if (index < 0 || index > this.size) {
				throw new IndexOutOfBoundsException("Specified index " + index + " is greater than size " + size);
			}

			if (index == 0) {
				Node<E> newhead = n;
				newhead.next = this.head;
				this.head.prev = newhead;
				this.head = newhead;
			} else if (index == this.size) {
				Node<E> newtail = n;
				newtail.prev = this.tail;
				this.tail.next = newtail;
				this.tail = newtail;
			} else {
				Node<E> curr;
				if (index < (size >> 1)) {
					for (curr = this.head; index > 1; index--) {
						curr = curr.next;
					}
				} else {
					index = this.size - 1 - index;
					for (curr = this.tail; index >= 0; index--) {
						curr = curr.prev;
					}
				}

				Node<E> temp = curr.next;
				curr.next = n;
				curr.next.next = temp;
				curr.next.prev = curr;
				temp.prev = curr.next;
			}
			this.size++;
		} finally {
			writecount++;
			rwLock.writeLock().unlock();
		}
	}

	@Override
	/***
	 * Currently no modifications are allowed through the iterator on the
	 * ConcurrentDoubleLinkedList. Perhaps eventually I will change it so that
	 * this is supported, but it would likely have a negative impact on
	 * performance since it would require a long running write lock.
	 */
	public boolean addAll(int index, Collection<? extends E> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear() {
		try {
			rwLock.writeLock().lock();
			this.size = 0;
			this.head = this.tail = null;
		} finally {
			writecount++;
			rwLock.writeLock().unlock();
		}
	}

	@Override
	public boolean contains(Object o) {
		try {
			rwLock.readLock().lock();
			boolean toreturn = (this.indexOf(o) >= 0);
			return toreturn;
		} finally {
			rwLock.readLock().unlock();
		}

	}

	@Override
	public E get(int index) {
		try {
			rwLock.readLock().lock();

			if (index < 0 || index >= this.size) {
				throw new IndexOutOfBoundsException("Specified index " + index + " is greater than size " + size);
			}

			Node<E> curr;
			if (index < (size >> 1)) {
				for (curr = this.head; index > 0; index--) {
					curr = curr.next;
				}

			} else {
				index = this.size - 1 - index;
				for (curr = this.tail; index > 0; index--) {
					curr = curr.prev;
				}
			}
			E toreturn = curr.value;
			return toreturn;
		} finally {
			rwLock.readLock().unlock();
		}

	}

	@Override
	public int indexOf(Object o) {
		try {
			rwLock.readLock().lock();
			int index = 0;
			if (o == null) {
				for (Node<E> e = head; e != null; e = e.next) {
					if (e.value == null) {
						return index;
					}
					index++;
				}
			} else {
				for (Node<E> e = head; e != null; e = e.next) {
					if (o.equals(e.value)) {
						return index;
					}
					index++;
				}
			}
			return -1;
		} finally {
			rwLock.readLock().unlock();
		}

	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	@Override
	public int lastIndexOf(Object o) {
		try {
			rwLock.readLock().lock();
			int index = size - 1;
			if (o == null) {
				for (Node<E> e = tail; e != null; e = e.prev) {
					if (e.value == null)
						return index;
					index--;
				}
			} else {
				for (Node<E> e = tail; e != null; e = e.prev) {
					if (o.equals(e.value))
						return index;
					index--;
				}
			}
			return -1;
		} finally {
			rwLock.readLock().unlock();
		}
	}

	@Override
	public ListIterator<E> listIterator() {
		return new MyListIterator(0);
	}

	@Override
	public ListIterator<E> listIterator(int index) {
		return new MyListIterator(index);
	}

	public E removeNode(Node<E> node) {
		try {
			rwLock.writeLock().lock();
			if (node.prev != null)
				node.prev.next = node.next;
			if (node.next != null)
				node.next.prev = node.prev;
			if (node == head)
				head = head.next;
			if (node == tail)
				tail = tail.prev;
			size--;
			return node.value;
		} finally {
			rwLock.writeLock().unlock();
		}
	}

	@Override
	public boolean remove(Object o) {
		try {
			rwLock.writeLock().lock();
			if (o == null) {
				for (Node<E> e = head; e != null; e = e.next) {
					if (e.value == null) {
						removeNode(e);
						return true;
					}
				}
			} else {
				for (Node<E> e = head; e != null; e = e.next) {
					if (o.equals(e.value)) {
						removeNode(e);
						return true;
					}
				}
			}
			return false;
		} finally {
			rwLock.writeLock().unlock();
		}

	}

	@Override
	public E remove(int index) {
		try {
			rwLock.writeLock().lock();
			if (index < 0 || index >= this.size) {
				throw new IndexOutOfBoundsException("Specified index " + index + " is greater than size " + size);
			}

			Node<E> curr;

			if (index < (size >> 1)) {
				for (curr = this.head; index > 0; index--) {
					curr = curr.next;
				}

			} else {
				index = this.size - 1 - index;
				for (curr = this.tail; index > 0; index--) {
					curr = curr.prev;
				}
			}
			return removeNode(curr);
		} finally {
			rwLock.writeLock().unlock();
		}
	}
	
	@Override
	/***
	 * Currently no modifications are allowed through the iterator on the
	 * ConcurrentDoubleLinkedList. Perhaps eventually I will change it so that
	 * this is supported, but it would likely have a negative impact on
	 * performance since it would require a long running write lock.
	 */
	public boolean removeAll(Collection c){
		throw new UnsupportedOperationException();
	}
	
	@Override
	/***
	 * Currently no modifications are allowed through the iterator on the
	 * ConcurrentDoubleLinkedList. Perhaps eventually I will change it so that
	 * this is supported, but it would likely have a negative impact on
	 * performance since it would require a long running write lock.
	 */
	public boolean retainAll(Collection c){
		throw new UnsupportedOperationException();
	}

	@Override
	public int size() {
		try {
			rwLock.readLock().lock();
			return size;
		} finally{
			rwLock.readLock().unlock();
		}
	}

	private class MyListIterator implements ListIterator<E> {

		private Node<E> current;
		private Node<E> previous;
		private Node<E> lastreturned;
		int currindex;
		private final int iterwritecount;

		public MyListIterator(int index) {
			try {
				rwLock.writeLock().lock();
				lastreturned = null;
				iterwritecount = writecount;
				if (index < 0 || index > size) {
					throw new IndexOutOfBoundsException("Specified index " + index + " is greater than size " + size);
				}
				currindex = index;

				if (index == size) {
					current = null;
					previous = tail;
				} else {

					Node<E> curr;
					if (index < (size >> 1)) {
						for (curr = head; index > 0; index--) {
							curr = curr.next;
						}

					} else {
						index = size - 1 - index;
						for (curr = tail; index > 0; index--) {
							curr = curr.prev;
						}
					}
					previous = curr.prev;
					current = curr;

				}
			} finally {
				rwLock.writeLock().unlock();
			}
		}

		@Override
		/***
		 * Currently no modifications are allowed through the iterator on the
		 * ConcurrentDoubleLinkedList. Perhaps eventually I will change it so
		 * that this is supported, but it would likely have a negative impact on
		 * performance since it would require a long running write lock.
		 */
		public void add(E e) {
			throw new UnsupportedOperationException("Cannot add with the iterator in ConcurrentDoubleLinkedList");
		}

		@Override
		public boolean hasNext() {
			try {
				rwLock.readLock().lock();
				if (iterwritecount != writecount)
					throw new ConcurrentModificationException();
				return current != null;
			} finally {
				rwLock.readLock().unlock();
			}

		}

		@Override
		public boolean hasPrevious() {
			try {
				rwLock.readLock().lock();
				if (iterwritecount != writecount)
					throw new ConcurrentModificationException();
				return previous != null;
			} finally {
				rwLock.readLock().unlock();
			}
		}

		@Override
		public E next() {
			try {
				rwLock.readLock().lock();
				if (iterwritecount != writecount)
					throw new ConcurrentModificationException();
				if (current == null)
					throw new NoSuchElementException();
				E elt = current.value;
				previous = current;
				current = current.next;
				currindex++;
				lastreturned = previous;
				return elt;
			} finally {
				rwLock.readLock().unlock();
			}

		}

		@Override
		public int nextIndex() {
			try {
				rwLock.readLock().lock();
				if (iterwritecount != writecount)
					throw new ConcurrentModificationException();
				return currindex;
			} finally {
				rwLock.readLock().unlock();
			}
		}

		@Override
		public E previous() {
			try {
				rwLock.readLock().lock();
				if (iterwritecount != writecount)
					throw new ConcurrentModificationException();
				if (previous == null)
					throw new NoSuchElementException();

				current = previous;
				previous = previous.prev;
				currindex--;
				lastreturned = current;
				return current.value;
			} finally {
				rwLock.readLock().unlock();
			}
		}

		@Override
		public int previousIndex() {
			try {
				rwLock.readLock().lock();
				if (iterwritecount != writecount)
					throw new ConcurrentModificationException();
				return currindex - 1;
			} finally {
				rwLock.readLock().unlock();
			}
		}

		@Override
		/***
		 * Currently no modifications are allowed through the iterator on the
		 * ConcurrentDoubleLinkedList. Perhaps eventually I will change it so
		 * that this is supported, but it would likely have a negative impact on
		 * performance since it would require a long running write lock.
		 */
		public void remove() {
			throw new UnsupportedOperationException("Cannot remove with the iterator in ConcurrentDoubleLinkedList");
		}

		@Override
		/***
		 * Currently no modifications are allowed through the iterator on the
		 * ConcurrentDoubleLinkedList. Perhaps eventually I will change it so
		 * that this is supported, but it would likely have a negative impact on
		 * performance since it would require a long running write lock.
		 */
		public void set(E e) {
			throw new UnsupportedOperationException("Cannot set with the iterator in ConcurrentDoubleLinkedList");

		}

	}

}
