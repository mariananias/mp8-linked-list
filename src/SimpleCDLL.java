import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Circularly-linked, doubly-linked lists with a dummy node that supports the Fail Fast policy.
 *
 * @author Samuel A. Rebelsky
 * @author Marina Ananias
 * @param <T>
 */
@SuppressWarnings("rawtypes")
public class SimpleCDLL<T> implements SimpleList {
  // +--------+------------------------------------------------------------
  // | Fields |
  // +--------+

  /**
   * The dummy of the list
   */
  Node<T> dummy;

  /**
   * The number of values in the list.
   */
  int size;

  /**
   * 
   */
  int listCounter = 0;

  // +--------------+------------------------------------------------------
  // | Constructors |
  // +--------------+

  /**
   * Create an empty list.
   */
  public SimpleCDLL() {
    this.dummy = new Node<T>(null, null, null);
    this.size = 0;
  } // SimpleCDLL

  // +-----------+---------------------------------------------------------
  // | Iterators |
  // +-----------+

  public Iterator<T> iterator() {
    return listIterator();
  } // iterator()

  public ListIterator<T> listIterator() {
    return new ListIterator<T>() {
      // +--------+--------------------------------------------------------
      // | Fields |
      // +--------+

      /**
       * The position in the list of the next value to be returned.
       * Included because ListIterators must provide nextIndex and
       * prevIndex.
       */
      int pos = 0;

      /**
       * Counter to keep track of the number of changes to the list. 
       * When you create an iterator, you save that number.
       */
      int itCounter = listCounter;

      /**
       * The cursor is between neighboring values, so we start links
       * to the previous and next value..
       */
      Node<T> prev = SimpleCDLL.this.dummy.prev;
      Node<T> next = SimpleCDLL.this.dummy.next;

      /**
       * The node to be updated by remove or set. Has a value of
       * null when there is no such value.
       */
      Node<T> update = null;

      // +---------+-------------------------------------------------------
      // | Methods |
      // +---------+

      public void failFast() {
        if (itCounter != listCounter) {
          throw new ConcurrentModificationException();
        }
      } // failFast()

      public void add(T val) throws UnsupportedOperationException {        
        // Fail-fast
        failFast();

        // Special case: The list is empty)
        if (SimpleCDLL.this.dummy.next == null) {
          SimpleCDLL.this.dummy.next = new Node<T>(dummy, val, dummy);
          SimpleCDLL.this.dummy.prev = SimpleCDLL.this.dummy.next;
          this.prev = SimpleCDLL.this.dummy.next;
          this.next = this.prev;
        } // empty list
        
        // Normal case
        else {
          this.prev = this.prev.insertAfter(val);
        } // normal case

        // Note that we cannot update
        this.update = null;

        // Increase the size
        ++SimpleCDLL.this.size;

        // Update the position. (See SimpleArrayList.java for more of
        // an explanation.)
        ++this.pos;

        // Update Counters
        ++itCounter;
        ++listCounter;
      } // add(T)

      public boolean hasNext() {
        // Fail-fast
        failFast();

        return (this.pos < SimpleCDLL.this.size);
      } // hasNext()

      public boolean hasPrevious() {
        // Fail-fast
        if (itCounter != listCounter) {
          throw new ConcurrentModificationException();
        }
        
        return (this.pos > 0);
      } // hasPrevious()

      public T next() {
        // Fail-fast
        failFast();

        if (!this.hasNext()) {
         throw new NoSuchElementException();
        } // if
        // Identify the node to update
        this.update = this.next;
        // Advance the cursor
        this.prev = this.next;
        this.next = this.next.next;
        // Note the movement
        ++this.pos;

        // And return the value
        return this.update.value;
      } // next()

      public int nextIndex() {
        // Fail-fast
        failFast();

        return this.pos;
      } // nextIndex()

      public int previousIndex() {
        // Fail-fast
        failFast();

        return this.pos - 1;
      } // prevIndex

      public T previous() throws NoSuchElementException {
        // Fail-fast
        failFast();

        if (!this.hasPrevious()) {
          throw new NoSuchElementException();
        }
        // Identify the node to update
        this.update = this.prev;
        // Advance the cursor
        this.next = this.prev;
        this.prev = this.prev.prev;
        // Note the movement
        --this.pos;

        // And return the value
        return this.update.value;
      } // previous()

      public void remove() {
        // Fail-fast
        failFast();

        // Sanity check
        if (this.update == null) {
          throw new IllegalStateException();
        } // if

        // Update the cursor
        if (this.next == this.update) {
          this.next = this.update.next;
        } // if
        if (this.prev == this.update) {
          this.prev = this.update.prev;
          --this.pos;
        } // if

        // Update the front
        if (SimpleCDLL.this.dummy == this.update) {
          SimpleCDLL.this.dummy = this.update.next;
        } // if

        // Do the real work
        this.update.remove();
        --SimpleCDLL.this.size;

        // Note that no more updates are possible
        this.update = null;
                
        // Update Counters
        ++itCounter;
        ++listCounter;

      } // remove()

      public void set(T val) {
        // Fail-fast
        failFast();

        // Sanity check
        if (this.update == null) {
          throw new IllegalStateException();
        } // if
        // Do the real work
        this.update.value = val;

        // Update Counters
        ++itCounter;
        ++listCounter;

      } // set(T)
    };
  } // listIterator()
}
