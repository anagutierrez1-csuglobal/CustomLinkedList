import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * CustomLinkedList
 * A singly linked list ADT that supports insertion at the tail, deletion of
 * the first matching node, and traversal through a custom iterator.
 */
public class CustomLinkedList {

    private Node head;
    // head is the entry point to the list; null when the list is empty

    /**
     * insert
     * Inserts a new node holding data at the end of the list.
     * @param data the integer value to store in the new node
     */
    public void insert(int data) {
        Node newNode = new Node(data);
        // wrap the value in a new node before linking it in

        if (head == null) {
            head = newNode;
            // empty list, new node becomes the head
        } else {
            Node current = head;
            while (current.next != null) {
                current = current.next;
                // walk to the last node in the list
            }
            current.next = newNode;
            // attach the new node after the last node
        }
    }

    /**
     * delete
     * Removes the first node found holding the given data.
     * Does nothing if the value is not present in the list.
     * @param data the integer value to remove
     */
    public void delete(int data) {
        if (head == null) {
            return;
            // nothing to delete from an empty list
        }

        if (head.data == data) {
            head = head.next;
            // match is the head, move head forward to drop it
            return;
        }

        Node current = head;
        while (current.next != null && current.next.data != data) {
            current = current.next;
            // scan while looking one node ahead for the match
        }

        if (current.next != null) {
            current.next = current.next.next;
            // match found, skip over it to unlink the node
        }
        // if current.next is null here, the value was never in the list
    }

    /**
     * iterator
     * Returns a fresh iterator positioned at the head of the list.
     * @return an Iterator over the Integer values in the list
     */
    public Iterator<Integer> iterator() {
        return new LinkedListIterator();
    }

    /**
     * loadFromFile
     * Reads whitespace or newline separated integers from a text file
     * and inserts each one into the list in the order they appear.
     * @param filePath path to the text file containing integer data
     */
    public void loadFromFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                // skip blank lines so they do not break parsing
                if (line.isEmpty()) {
                    continue;
                }
                String[] tokens = line.split("\\s+");
                // a line can hold one value or several separated by spaces
                for (String token : tokens) {
                    insert(Integer.parseInt(token));
                    // convert token to int and insert it into the list
                }
            }
        } catch (IOException e) {
            System.out.println("Unable to read file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("File contains a non integer value: " + e.getMessage());
        }
    }

    /**
     * Node
     * Private inner class representing a single element in the list.
     */
    private class Node {
        int data;
        Node next;

        Node(int data) {
            this.data = data;
            this.next = null;
            // new nodes always start unlinked
        }
    }

    /**
     * LinkedListIterator
     * Private inner class that walks the list one node at a time
     * without exposing the underlying Node structure.
     */
    private class LinkedListIterator implements Iterator<Integer> {
        private Node current = head;
        // starts at head so the first call to next() returns the first element

        @Override
        public boolean hasNext() {
            return current != null;
            // true as long as there is an unvisited node remaining
        }

        @Override
        public Integer next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
                // guard against calling next() past the end of the list
            }
            int data = current.data;
            current = current.next;
            // capture the value first, then advance the cursor
            return data;
        }
    }
}

/**
 * Main
 * Demonstrates CustomLinkedList functionality: insertion, deletion,
 * iterator-based traversal, and loading integer data from a text file.
 */
class Main {
    public static void main(String[] args) {

        // ---------------------------------------------------------
        // STEP 1: Insert elements and traverse with the iterator
        // ---------------------------------------------------------
        CustomLinkedList linkedList = new CustomLinkedList();
        linkedList.insert(1);
        linkedList.insert(2);
        linkedList.insert(3);

        System.out.print("After insertion (1, 2, 3): ");
        Iterator<Integer> iterator = linkedList.iterator();
        while (iterator.hasNext()) {
            System.out.print(iterator.next() + " ");
        }
        System.out.println();

        // ---------------------------------------------------------
        // STEP 2: Delete an element and traverse again
        // ---------------------------------------------------------
        linkedList.delete(2);
        // removes the middle node to prove delete() re-links the list correctly

        System.out.print("After deleting 2: ");
        iterator = linkedList.iterator();
        while (iterator.hasNext()) {
            System.out.print(iterator.next() + " ");
        }
        System.out.println();

        // ---------------------------------------------------------
        // STEP 3: Delete a value that is not in the list
        // ---------------------------------------------------------
        linkedList.delete(99);
        // confirms delete() fails safely when the value is not found

        System.out.print("After deleting 99 (not present): ");
        iterator = linkedList.iterator();
        while (iterator.hasNext()) {
            System.out.print(iterator.next() + " ");
        }
        System.out.println();

        // ---------------------------------------------------------
        // STEP 4: Load integers from a text file and traverse
        // ---------------------------------------------------------
        CustomLinkedList fileList = new CustomLinkedList();
        fileList.loadFromFile("data.txt");
        // data.txt must sit in the working directory, one or more ints per line

        System.out.print("Elements loaded from data.txt: ");
        iterator = fileList.iterator();
        while (iterator.hasNext()) {
            System.out.print(iterator.next() + " ");
        }
        System.out.println();
    }
}