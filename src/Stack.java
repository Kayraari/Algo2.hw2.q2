//-----------------------------------------------------
// Title: Stack class
// Author: Kayra Arı, Elçin Karagül
// ID:
// Section: 4
// Assignment: 2
// Description: This class is a LIFO (last in, first out)
// stack implemented as a linked list of Nodes. It is used by
// DirectedDFS to keep the current trail of intersections and
// to store the finished route in the correct order.
//-----------------------------------------------------

import java.util.Iterator;

public class Stack<Item> implements Iterable<Item> {
    private Node<Item> first;   // top of the stack
    private int n;              // number of items

    public boolean isEmpty() {
        //--------------------------------------------------------
        // Summary: Checks whether the stack is empty.
        // Precondition: None.
        // Postcondition: Returns true if there are no items.
        //--------------------------------------------------------
        return first == null;
    }

    public int size() {
        //--------------------------------------------------------
        // Summary: Returns the number of items in the stack.
        // Precondition: None.
        // Postcondition: n is returned.
        //--------------------------------------------------------
        return n;
    }

    public void push(Item item) {
        //--------------------------------------------------------
        // Summary: Adds an item to the top of the stack.
        // Precondition: None.
        // Postcondition: The item is on top; n is increased.
        //--------------------------------------------------------
        first = new Node<Item>(item, first, null);
        n++;
    }

    public Item pop() {
        //--------------------------------------------------------
        // Summary: Removes and returns the item on top.
        // Precondition: The stack is not empty.
        // Postcondition: The top item is removed; n is decreased.
        //--------------------------------------------------------
        Item item = first.getData();
        first = first.getNext();
        n--;
        return item;
    }

    public Item peek() {
        //--------------------------------------------------------
        // Summary: Returns the item on top without removing it.
        // Precondition: The stack is not empty.
        // Postcondition: The top item is returned; the stack is
        // not changed.
        //--------------------------------------------------------
        return first.getData();
    }

    public Iterator<Item> iterator() {
        //--------------------------------------------------------
        // Summary: Returns an iterator from top to bottom so the
        // stack can be used in a for-each loop.
        // Precondition: None.
        // Postcondition: An iterator starting at the top is returned.
        //--------------------------------------------------------
        return new Iterator<Item>() {
            private Node<Item> current = first;

            public boolean hasNext() {
                return current != null;
            }

            public Item next() {
                Item item = current.getData();
                current = current.getNext();
                return item;
            }
        };
    }
}
