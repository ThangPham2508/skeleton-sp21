package bstmap;

import edu.princeton.cs.algs4.BST;

import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable<K>,V> implements Map61B<K,V>{
    private Node root;

    @Override
    public Iterator<K> iterator() {
        throw new UnsupportedOperationException();
    }

    private class Node {
        K key;
        V value;
        int size;
        Node left;
        Node right;
        private Node (K key, V value, int size) {
            this.key = key;
            this.value = value;
            this.size = size;
        }
    }

    public BSTMap () {
        root = null;
    }

    @Override
    public void clear() {
        root = null;
    }

    @Override
    public boolean containsKey(K key) {
        return get(key) != null || get(key) == null && size(getNode(root, key)) != 0;
    }

    public V get(K key) {
        return get(root, key);
    }

    private V get(Node node, K key) {
        if (key == null) throw new IllegalArgumentException("calls get() with a null key");
        if (node == null) {
            return null;
        }
        if (key.compareTo(node.key) < 0) {
            return get(node.left, key);
        }
        else if (key.compareTo(node.key) > 0) {
            return get(node.right, key);
        }
        else {
            return node.value;
        }
    }

    private Node getNode(Node node, K key) {
        if (key == null) throw new IllegalArgumentException("calls get() with a null key");
        if (node == null) {
            return null;
        }
        if (key.compareTo(node.key) < 0) {
            return getNode(node.left, key);
        }
        else if (key.compareTo(node.key) > 0) {
            return getNode(node.right, key);
        }
        else {
            return node;
        }
    }

    public void print () {
        print(root);
    }

    private void print(Node node) {
        if (node.left != null) {
            print(node.left);
        }
        System.out.println(node.value);
        if (node.right != null) {
            print(node.right);
        }
    }

    @Override
    public int size() {
        if (root == null) return 0;
        return root.size;
    }

    public void put (K key, V value) {
        root = put(root, key, value);
    }

    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    private Node put(Node x, K key, V val) {
        if (x == null) return new Node(key, val, 1);
        int cmp = key.compareTo(x.key);
        if      (cmp < 0) x.left  = put(x.left,  key, val);
        else if (cmp > 0) x.right = put(x.right, key, val);
        else              x.value   = val;
        x.size = 1 + size(x.left) + size(x.right);
        return x;
    }

    private int size (Node node) {
        if (node == null) return 0;
        return node.size;
    }
}
