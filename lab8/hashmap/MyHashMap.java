package hashmap;

import java.util.*;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author YOUR NAME HERE
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    private Collection<Node>[] buckets;
    private int size;
    private double loadFactor;
    private double itemCount;
    private HashSet<K> keys;

    /** Constructors */
    public MyHashMap() {
        buckets = new Collection[16];
        size = 16;
        loadFactor = 0.75;
        itemCount = 0;
        keys = new HashSet<>();
        for (int i = 0; i < size; i++) {
            buckets[i] = createBucket();
        }
    }

    public MyHashMap(int initialSize) {
        buckets = new Collection[initialSize];
        size = initialSize;
        loadFactor = 0.75;
        itemCount = 0;
        keys = new HashSet<>();
        for (int i = 0; i < size; i++) {
            buckets[i] = createBucket();
        }
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        buckets = new Collection[initialSize];
        size = initialSize;
        loadFactor = maxLoad;
        itemCount = 0;
        keys = new HashSet<>();
        for (int i = 0; i < size; i++) {
            buckets[i] = createBucket();
        }
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return null;
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new ArrayList<>();
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     *
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {
        return null;
    }

    public void clear() {
        for (int i = 0; i < size; i++) {
            buckets[i] = createBucket();
        }
        keys.clear();
        itemCount = 0;
    }

    @Override
    public boolean containsKey(K key) {
        return keys.contains(key);
    }

    private Node getNode(K key) {
        for (int i = 0; i < size; i++) {
            Iterator<Node> iterator = buckets[i].iterator();
            while (iterator.hasNext()) {
                Node n = iterator.next();
                if (n.key.equals(key)) {
                    return n;
                }
            }
        }
        return null;
    }

    public V get(K key) {
        if (getNode(key) != null) {
            return getNode(key).value;
        }
        return null;
    }

    public int size() {
        return (int) itemCount;
    }

    private int hash(Node n) {
        return Math.floorMod(n.hashCode(), size);
    }

    private void resize() {
        Collection<Node>[] newBuckets = new Collection[size];
        for (int i = 0; i < size; i++) {
            newBuckets[i] = createBucket();
        }
        for (int i = 0; i < size / 2; i++) {
            if (buckets[i] == null) {
                continue;
            }
            Iterator<Node> iterator = buckets[i].iterator();
            while (iterator.hasNext()) {
                Node item = iterator.next();
                newBuckets[hash(item)].add(item);
            }
        }
        buckets = newBuckets;
    }

    public void put(K key, V value) {
        itemCount++;
        if (itemCount / size >= loadFactor) {
            size *= 2;
            resize();
        }
        Node n = new Node(key, value);
        boolean remove = false;
        Node removeNode = null;
        if (containsKey(key)) {
            for (Node node : buckets[hash(getNode(key))]) {
                if (node.key.equals(key)) {
                    remove = true;
                    removeNode = node;
                    break;
                }
            }
        }
        if (remove) {
            buckets[hash(getNode(key))].remove(removeNode);
            itemCount--;
        }
        buckets[hash(n)].add(n);
        keys.add(key);
    }

    public Set<K> keySet() {
        return keys;
    }

    public V remove (K key) {
        for (int i = 0; i < size; i++) {
            Iterator<Node> iterator = buckets[i].iterator();
            while (iterator.hasNext()) {
                Node n = iterator.next();
                if (n.key.equals(key)) {
                    buckets[hash(n)].remove(n);
                    keys.remove(key);
                    return n.value;
                }
            }
        }
        return null;
    }

    public V remove(K key, V value) {
        for (int i = 0; i < size; i++) {
            Iterator<Node> iterator = buckets[i].iterator();
            while (iterator.hasNext()) {
                Node n = iterator.next();
                if (n.key.equals(key) && n.value.equals(value)) {
                    buckets[hash(n)].remove(n);
                    keys.remove(key);
                    return n.value;
                }
            }
        }
        return null;
    }

    public Iterator<K> iterator() {
        return keys.iterator();
    }
}
