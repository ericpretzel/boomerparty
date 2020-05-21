package util;

public class HashMap<K, V> implements java.io.Serializable {

    private static final long serialVersionUID = 696969696969696969L;

    private final DLList<V>[] table;

    private final DLList<K> keys;

    @SuppressWarnings("unchecked")
    public HashMap(int size) {
        table = new DLList[size];
        keys = new DLList<>();
    }

    public HashMap() {
        this(10000);
    }

    public void put(K key, V value) {
        int i = index(key);
        if (table[i] == null)
            table[i] = new DLList<>();
        table[i].add(value);
        if (!keys.contains(key)) keys.add(key);
    }

    public DLList<V> get(K key) {
        return table[index(key)];
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
