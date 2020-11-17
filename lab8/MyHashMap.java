import java.util.*;

public class MyHashMap<K, V> implements Map61B<K, V> {
    private static final int INIT_CAPACITY = 16;
    private static final int MULTIPLE = 2;
    private static final double LOAD_FACTOR = 0.75;

    private List<Entry<K,V>>[] buckets;
    private int size;
    private double loadFactor;

    MyHashMap() {
        this(INIT_CAPACITY, LOAD_FACTOR);
    }

    MyHashMap(int initialSize) {
        this(initialSize, LOAD_FACTOR);
    }

    MyHashMap(int initialSize, double loadFactor) {
        buckets = new List[initialSize];
        for(int i = 0; i < buckets.length; i++) {
            buckets[i] = new LinkedList<>();
        }
        this.loadFactor = loadFactor;
    }

    @Override
    public void clear() {
        buckets = new List[buckets.length];
        for(int i = 0; i < buckets.length; i++) {
            buckets[i] = new LinkedList<>();
        }
        size = 0;
    }

    @Override
    public boolean containsKey(K key) {
        int index = generateHash(key, buckets.length);
        List<Entry<K,V>> list = buckets[index];
        for(Entry<K,V> entry : list) {
            if(entry.key.equals(key)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public V get(K key) {
        int index = generateHash(key, buckets.length);
        List<Entry<K,V>> entries = buckets[index];
        for(Entry<K,V> entry : entries) {
            if(entry.key.equals(key)) {
                return entry.value;
            }
        }
        return null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void put(K key, V value) {
        if(size >= buckets.length * loadFactor) {
            resize(buckets.length * MULTIPLE);
        }
        int index = generateHash(key, buckets.length);
        List<Entry<K,V>> entries = buckets[index];
        for(Entry<K,V> entry : entries) {
            if(entry.key.equals(key)) {
                entry.value = value;
                return;
            }
        }
        entries.add(new Entry<>(key, value));
        size++;
    }

    @Override
    public Set<K> keySet() {
        Set<K> res = new HashSet<>();
        for (List<Entry<K, V>> entries : buckets) {
            for (Entry<K, V> entry : entries) {
                res.add(entry.key);
            }
        }
        return res;
    }

    @Override
    public V remove(K key) {
        int index = generateHash(key, buckets.length);
        List<Entry<K,V>> entries = buckets[index];
        for(Entry<K,V> entry : entries) {
            if(entry.key.equals(key)) {
                entries.remove(entry);
                return entry.value;
            }
        }
        return null;
    }

    @Override
    public V remove(K key, V value) {
        int index = generateHash(key, buckets.length);
        List<Entry<K,V>> entries = buckets[index];
        for(Entry<K,V> entry : entries) {
            if(entry.key.equals(key) && entry.value.equals(value)) {
                entries.remove(entry);
                return entry.value;
            }
        }
        return null;
    }

    @Override
    public Iterator<K> iterator() {
        return keySet().iterator();
    }

    private void resize(int capacity) {
        List<Entry<K,V>>[] new_buckets = new List[capacity];
        for(int i = 0; i < capacity; i++) {
            new_buckets[i] = new LinkedList<>();
        }
        for (List<Entry<K, V>> entries : buckets) {
            for (Entry<K, V> entry : entries) {
                int index = generateHash(entry.key, capacity);
                new_buckets[index].add(entry);
            }
        }
        buckets = new_buckets;
    }

    private int generateHash(K key, int module) {
        return (key.hashCode() & 0x7fffffff) % module;
    }

    private static class Entry<K,V> {
        K key;
        V value;

        Entry(K k, V v) {
            key = k;
            value = v;
        }
    }
}
