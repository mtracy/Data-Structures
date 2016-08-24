package structures;

import java.util.AbstractSequentialList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class DoubleLinkedList<E> extends AbstractSequentialList<E> implements List<E> {

	private Node<E> head;
	private Node<E> tail;
	private int size;

	public DoubleLinkedList() {
		this.head = null;
	}

	public Node<E> getHead() {
		return head;
	}

	public void setHead(Node<E> head) {
		this.head = head;
	}

	public Node<E> getTail() {
		return tail;
	}

	public void setTail(Node<E> tail) {
		this.tail = tail;
	}

	@Override
	public boolean add(E elt) {
		if (this.head == null) {
			this.head = new Node<E>(elt);
			this.tail = this.head;
		} else {
			this.tail.next = new Node<E>(elt);
			this.tail.next.prev = this.tail;
			this.tail = this.tail.next;
		}
		this.size++;
		return true;
	}

	public boolean add(Node<E> n) {
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
	}

	@Override
	public void add(int index, E elt) {

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
	}

	public void add(int index, Node<E> n) {

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
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		for (E elt : c) {
			this.add(index, elt);
			index++;
		}
		return true;
	}

	@Override
	public void clear() {
		this.size = 0;
		this.head = this.tail = null;
	}

	@Override
	public boolean contains(Object o) {
		return this.indexOf(o) >= 0;
	}

	@Override
	public E get(int index) {

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
		return curr.value;
	}

	@Override
	public int indexOf(Object o) {
		int index = 0;
		if (o == null) {
			for (Node<E> e = head; e != null; e = e.next) {
				if (e.value == null)
					return index;
				index++;
			}
		} else {
			for (Node<E> e = head; e != null; e = e.next) {
				if (o.equals(e.value))
					return index;
				index++;
			}
		}
		return -1;
	}

	@Override
	public boolean isEmpty() {

		return size == 0;
	}

	@Override
	public int lastIndexOf(Object o) {
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
	}

	@Override
	public boolean remove(Object o) {
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
	}

	@Override
	public E remove(int index) {
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
	}

	@Override
	public int size() {
		return size;
	}

	private class MyListIterator implements ListIterator<E> {

		private Node<E> current = head;
		private Node<E> previous;
		private Node<E> lastreturned;
		int currindex = 0;

		public MyListIterator(int index) {
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
				previous = current.prev;
				current = curr;

			}
		}

		@Override
		public void add(E e) {
			if (previous == null) {
				previous = new Node<E>(e);
				previous.next = current;
			} else {
				previous.next = new Node<E>(e);
				previous.next.next = current;
				previous.next.prev = previous;
				previous = previous.next;
			}
			if (current != null) {
				current.prev = previous;
			}
			size++;
			currindex++;
		}

		@Override
		public boolean hasNext() {
			return current != null;
		}

		@Override
		public boolean hasPrevious() {
			return previous != null;
		}

		@Override
		public E next() {
			if (current == null)
				throw new NoSuchElementException();

			E elt = current.value;
			previous = current;
			current = current.next;
			currindex++;
			lastreturned = previous;
			return elt;
		}

		@Override
		public int nextIndex() {
			return currindex;
		}

		@Override
		public E previous() {
			if (previous == null)
				throw new NoSuchElementException();

			current = previous;
			previous = previous.prev;
			currindex--;
			lastreturned = current;
			return current.value;
		}

		@Override
		public int previousIndex() {
			return currindex - 1;
		}

		@Override
		public void remove() {
			if (lastreturned == null)
				throw new IllegalStateException();
			removeNode(lastreturned);
		}

		@Override
		public void set(E e) {
			if (lastreturned == null)
				throw new IllegalStateException();
			lastreturned.value = e;

		}

	}

}
