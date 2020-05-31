package util;

public class HashMap<K, V> implements java.io.Serializable {

    private static final long serialVersionUID = 696969696969696969L;

    private static final float loadFactor = 0.75F;

    private Object[] table;

    private final DLList<K> keys;

    @SuppressWarnings("unchecked")
    private final Node nullNode = new Node((K) new Object(), null);

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
        //don't rly know why this works but that's how they do it in java.util.HashMap :)
        int n = -1 >>> Integer.numberOfLeadingZeros(size - 1);
        table = new Object[n + 1];
        keys = new DLList<>();
    }

    public HashMap() {
        this(8);
    }

    @SuppressWarnings("unchecked")
    public void put(K key, V val) {
        if (!keys.contains(key)) keys.add(key);

        int i = indexFor(key);
        if (table[i] == null) {
            table[i] = new Node(key, val);
        } else {
            Node current = (Node) table[i];
            boolean put = false;
            while (current.next != null) {
                if (key.equals(current.key)) {
                    current.val = val;
                    put = true;
                }
                current = current.next;
            }
            if (!put)
                current.next = new Node(key, val);
        }

        if ((double)size() / table.length > loadFactor) {
            rehash();
        }
    }


    public V get(K key) {
        return getNode(key).val;
    }
    @SuppressWarnings("unchecked")
    private Node getNode(K key) {
        Node current = (Node) table[indexFor(key)];
        while (current != null) {
            if (current.key.equals(key))
                return current;
            current = current.next;
        }
        return nullNode;
    }

    @SuppressWarnings("unchecked")
    private void rehash() {
        Object[] newTable = new Object[table.length << 1];

        for (K key : this.getKeys()) {
            Node node = getNode(key);

            int i = indexFor(node, newTable.length);

            if (newTable[i] == null) {
                newTable[i] = node;
            } else {
                Node current = (Node) newTable[i];
                while (current.next != null) {
                    current = current.next;
                }
                current.next = node;
            }
        }
        this.table = newTable;
    }

    public DLList<K> getKeys() {
        return keys;
    }

    public int size() {
        return keys.size();
    }

    private int indexFor(K key) {
        return indexFor(key, table.length);
    }
    private int indexFor(K key, int length) {
        return Math.abs(key.hashCode() % length);
    }
    private int indexFor(Node node) {
        return indexFor(node, table.length);
    }
    private int indexFor(Node node, int length) {
        return Math.abs(node.hash % length);
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < keys.size(); i++) {
            result.append(keys.get(i).toString()).append(" : ").append(get(keys.get(i)).toString()).append("\n");
        }
        return result.toString();
    }
}
