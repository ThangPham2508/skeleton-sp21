package deque;
import java.util.Iterator;

public class LinkedListDeque<T> implements Iterable<T>, Deque<T> {
    private class Node {
        private T data;
        private Node prev;
        private Node next;
        Node(T x, Node p, Node n) {
            data = x;
            prev = p;
            next = n;
        }
    }

    private int size;
    private Node sentinel;

    public LinkedListDeque() {
        size = 0;
        sentinel = new Node(null, null, null);
        sentinel.prev = sentinel; sentinel.next = sentinel;
    }

    public void addFirst(T item) {
        sentinel.next.prev = new Node(item, sentinel, sentinel.next);
        sentinel.next = sentinel.next.prev;
        size += 1;
    }

    public void addLast(T item) {
        sentinel.prev.next = new Node(item, sentinel.prev, sentinel);
        sentinel.prev = sentinel.prev.next;
        size += 1;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        Node p = sentinel.next;
        while (p != sentinel) {
            System.out.print(p.data + " ");
            p = p.next;
        }
        System.out.println();
    }

    public T removeFirst() {
        Node p = sentinel.next;
        sentinel.next = sentinel.next.next;
        sentinel.next.prev = sentinel;
        size = (size == 0) ? 0 : size - 1;
        return p.data;
    }

    public T removeLast() {
        Node p = sentinel.prev;
        sentinel.prev = sentinel.prev.prev;
        sentinel.prev.next = sentinel;
        size = (size == 0) ? 0 : size - 1;
        return p.data;
    }

    public T get(int index) {
        if (index >= size) {
            return null;
        }
        Node p = sentinel.next;
        for (int i = 0; i < index; i++) {
            p = p.next;
        }
        return p.data;
    }

    public T getRecursive(int index) {
        if (index >= size) {
            return null;
        }
        if (index == 0) {
            return sentinel.next.data;
        } else {
            LinkedListDeque<T> L = new LinkedListDeque<>();
            L.sentinel = this.sentinel; L.size = size;
            T n = L.removeFirst();
            T out = L.getRecursive(index - 1);
            L.addFirst(n);
            return out;
        }
    }

    public Iterator<T> iterator(){
        return new LinkedListIterator();
    }

    private class LinkedListIterator implements Iterator<T> {
        private Node pos;

        public LinkedListIterator() {
            pos = sentinel.next;
        }

        @Override
        public boolean hasNext() {
            return pos != sentinel;
        }

        public T next() {
            Node p = pos;
            pos = pos.next;
            return p.data;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Deque)) {
            return false;
        }
        Deque<T> lld = (Deque<T>) obj;
        if (this.size() != lld.size()) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            if (lld.get(i) != this.get(i)) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        LinkedListDeque<String> lld = new LinkedListDeque<>();
        lld.addFirst("H");
        lld.addFirst("e");
        lld.addFirst("l");
        LinkedListDeque<String> lldd = new LinkedListDeque<>();
        lldd.addFirst("H");
        lldd.addFirst("e");
        lldd.addFirst("ld");
        System.out.println(lld.equals(lldd));
    }
}
