package util;

import java.util.Iterator;

public class DLList<E> implements Iterable<E>, java.io.Serializable {

    private static final long serialVersionUID = 12039812301L;
    private final Node dummy = new Node(null);
    private int size = 0;

    public DLList() {
        dummy.next = dummy.prev = dummy;
    }

    public void add(E item) {
        Node node = new Node(item);
        if (size == 0) {
            node.next = dummy;
            node.prev = dummy;
            dummy.next = node;
            dummy.prev = node;
        } else {
            node.next = dummy;
            node.prev = dummy.prev;
            dummy.prev.next = node;
            dummy.prev = node;
        }
        size++;
    }

    public void add(E item, int index) {
        if (size == 0) {
            add(item);
            return;
        }

        Node node = new Node(item);
        Node point = getNode(index);

        node.prev = point.prev;
        node.next = point;
        point.prev = node;

        size++;
    }

    public void remove(E item) {
        remove(indexOf(item));
    }

    public E remove(int index) {
        Node node = getNode(index);
        node.prev.next = node.next;
        node.next.prev = node.prev;
        size--;
        return node.item;
    }

    public void clear() {
        dummy.next = dummy.prev = dummy;
        size = 0;
    }

    public boolean contains(E item) {
        return indexOf(item) >= 0;
    }

    public int indexOf(E item) {
        int index = 0;
        Node node = dummy.next;
        while (node != dummy) {
            if (node.item.equals(item))
                return index;
            node = node.next;
            index++;
        }
        return -1;
    }

    public E get(int index) {
        return getNode(index).item;
    }

    public void set(int index, E item) {
        getNode(index).item = item;
    }

    private Node getNode(int index) {
        Node node;
        if (index > size / 2) {
            node = dummy.prev;
            for (int i = size - 1; i > index; i--)
                node = node.prev;
        } else {
            node = dummy.next;
            for (int i = 0; i < index; i++)
                node = node.next;
        }
        return node;
    }

    public void shuffle() {
        Node node = dummy.next;
        while (node != dummy) {
            Node swap = getNode((int) (Math.random() * size));
            E item = node.item;
            node.item = swap.item;
            swap.item = item;
            node = node.next;
        }
    }

    public int size() {
        return size;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        Node current = dummy.next;
        while (current != dummy) {
            result.append(current.item.toString()).append(" ");
            current = current.next;
        }
        /*for (E e : this)
            result.append(e.toString()).append(" ");*/
        return result.toString();
    }

    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private Node node = dummy;

            public boolean hasNext() {
                return node.next != dummy;
            }

            public E next() {
                return (node = node.next).item;
            }
        };
    }

    private class Node implements java.io.Serializable {
        E item;
        Node prev, next;

        Node(E item) {
            this.item = item;
            prev = null;
            next = null;
        }
    }
}
