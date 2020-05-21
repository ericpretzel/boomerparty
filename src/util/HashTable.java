package util;

import java.util.Iterator;

public class HashTable<E> implements Iterable<E> {

    private static final double loadFactor = 0.75;

    private final DLList<E>[] table;

    @SuppressWarnings("unchecked")
    public HashTable(int size) {
        this.table = new DLList[size];
    }

    public HashTable() {
        this(10000);
    }

    public void add(E e) {
        int i = get(e);
        if (table[i] == null)
            table[i] = new DLList<>();
        table[i].add(e);
    }

    public int get(Object key) {
        return Math.abs(key.hashCode()) % table.length;
    }

    public boolean contains(E e) {
        int i = get(e);
        if (table[i] == null)
            return false;
        return table[i].contains(e);
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < table.length; i++) {
            DLList<E> list = table[i];
            if (list != null && list.size() > 0)
                result.append("\nBucket ").append(i).append(list.toString());
        }
        return result.toString();
    }

    public void remove(E e) {
        int i = get(e);
        if (table[i] == null) return;
        if (table[i].contains(e))
            table[i].remove(e);
    }

    public Iterator<E> iterator() {
        return new Iterator<>() {

            int t = 0;
            Iterator<E> l = table[t] == null ? null : table[t].iterator();

            public E next() {
                if (l == null || !l.hasNext()) {
                    do {
                        t++;
                    } while (t < table.length && table[t] == null);
                    l = table[t].iterator();
                }
                return l.next();
            }

            public boolean hasNext() {
                if (l != null && l.hasNext())
                    return true;
                int i = t + 1;
                while (i < table.length) {
                    if (table[i++] != null) {
                        return true;
                    }
                }
                return false;
            }
        };
    }
}