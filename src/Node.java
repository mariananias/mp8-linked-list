/**
 * Nodes for doubly-linked structures.
 */
public class Node<T> {
  // +--------+------------------------------------------------------
  // | Fields |
  // +--------+

  /** 
   * The previous node.
   */
  Node<T> prev;

  /**
   * The stored value.
   */
  T value;

  /**
   * The next node.
   */
  Node<T> next;

  // +--------------+------------------------------------------------
  // | Constructors |
  // +--------------+

  /** 
   * Create a new node.
   */
  public Node(Node<T> prev, T value, Node<T> next) {
    this.prev = prev;
    this.value = value;
    this.next = next;
  } // Node(Node<T>, T, Node<T>)

  /**
   * Create a new node with no previous link. (E.g., the front
   * of some kinds of lists.)
   */
  public Node(T value, Node<T> next) {
    this(null, value, next);
  } // Node(T, Node<T>)

  /**
   * Create a new node with no next link. (Included primarily
   * for symmetry.)
   */
  public Node(Node<T> prev, T value) {
    this(prev, value, null);
  } // Node(Node<T>, T)

  /**
   * Create a new node with no links.
   */
   public Node(T value) {
     this(null, value, null);
   } // Node(T)

  // +---------+-----------------------------------------------------
  // | Methods |
  // +---------+

  /**
   * Insert a new value after this node. Returns the new node.
   */
  Node<T> insertAfter(T value) {
    Node<T> tmp = new Node<T>(this, value, this.next);
    if (this.next != null) {
      this.next.prev = tmp;
    } // if
    this.next = tmp;
    return tmp;
  } // insertAfter

  /**
   * Insert a new value before this node. Returns the new node.
   */
  Node<T> insertBefore(T value) {
    Node<T> tmp = new Node<T>(this.prev, value, this);
    if (this.prev != null) {
      this.prev.next = tmp;
    } // if
    this.prev = tmp;
    return tmp;
  } // insertBefore

  /**
   * Remove this node.
   */
  void remove() {
    if (this.prev != null) {
      this.prev.next = this.next;
    }
    if (this.next != null) {
      this.next.prev = this.prev;
    }
    this.prev = null;
    this.next = null;
  } // remove()

} // Node<T>