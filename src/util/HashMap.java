package util;

public class HashMap<K, V> implements java.io.Serializable {

    private static final long serialVersionUID = 696969696969696969L;

    private static final float loadFactor = 0.75F;

    private final Object[] table;

    private final DLList<K> keys;

    class Node {
        Node(K key, V val) {
            this(key, val, null);
        }
        Node(K key, V val, Node next) {
            hash = key.hashCode();
            this.key = key;
            this.val = val;
            this.next = next;
        }
        final int hash;
        final K key;
        V val;
        Node next;
    }

    public HashMap(int size) {
        table = new Object[size];
        keys = new DLList<>();
    }

    public HashMap() {
        this(8);
    }

    @SuppressWarnings("unchecked")
    public void put(K key, V val) {
        if (!keys.contains(key)) keys.add(key);


        int i = index(key);
        if (table[i] == null) {
            table[i] = new Node(key, val);
            return;
        }

        Node current = (Node) table[i];
        while (current != null) {
            if (key.equals(current.key)) {
                current.val = val;
                return;
            }
            current = current.next;
        }
        current.next = new Node(key, val);

    }

    @SuppressWarnings("unchecked")
    public V get(K key) {
        Node current = (Node) table[index(key)];
        while (current != null) {
            if (current.key.equals(key))
                return current.val;
            current = current.next;
        }
        return null;
    }

    public DLList<K> getKeys() {
        return keys;
    }

    public int size() {
        return keys.size();
    }

    private int index(K key) {
        return Math.abs(key.hashCode() % table.length);
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < keys.size(); i++) {
            result.append(keys.get(i).toString()).append(" : ").append(get(keys.get(i)).toString());
        }
        return result.toString();
    }
}
