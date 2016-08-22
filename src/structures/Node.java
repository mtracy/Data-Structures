package structures;
public class Node<V> {
	V value;
	Node<V> next;
	Node<V> prev;

	public Node(V value) {
		this.value = value;
		this.next = null;
		this.prev = null;
	}

	public void setNext(Node<V> next) {
		this.next = next;
	}

	public Node<V> getNext() {
		return this.next;
	}

	public void setValue(V value) {
		this.value = value;
	}

	public V getValue() {
		return this.value;
	}

	public Node<V> getPrev() {
		return prev;
	}

	public void setPrev(Node<V> prev) {
		this.prev = prev;
	}
}