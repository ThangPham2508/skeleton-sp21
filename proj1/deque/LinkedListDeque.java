package deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkedListDeque<T> implements Iterable<T>, Deque<T> {
    public class Node{
        public T data;
        public Node prev;
        public Node next;
        public Node(T x, Node p, Node n){
            data = x;
            prev = p;
            next = n;
        }
    }

    private int size;
    private Node sentinel;

    public LinkedListDeque(){
        size = 0;
        sentinel = new Node(null, null, null);
        sentinel.prev = sentinel; sentinel.next = sentinel;
    }

    public void addFirst(T item){
        sentinel.next.prev = new Node(item, sentinel, sentinel.next);
        sentinel.next = sentinel.next.prev;
        size += 1;
    }

    public void addLast(T item){
        sentinel.prev.next = new Node(item, sentinel.prev, sentinel);
        sentinel.prev = sentinel.prev.next;
        size += 1;
    }

    public int size(){
        return size;
    }

    public void printDeque(){
        Node p = sentinel.next;
        while (p != sentinel) {
            System.out.print(p.data + " ");
            p = p.next;
        }
        System.out.println();
    }

    public T removeFirst(){
        Node p = sentinel.next;
        sentinel.next = sentinel.next.next;
        sentinel.next.prev = sentinel;
        size = (size == 0) ? 0 : size - 1;
        return p.data;
    }

    public T removeLast(){
        Node p = sentinel.prev;
        sentinel.prev = sentinel.prev.prev;
        sentinel.prev.next = sentinel;
        size = (size == 0) ? 0 : size - 1;
        return p.data;
    }

    public T get(int index){
        if (index >= size) return null;
        Node p = sentinel.next;
        for (int i = 0; i < index; i++){
            p = p.next;
        }
        return p.data;
    }

    public T getRecursive(int index){
        if (index >= size) return null;
        if (index == 0) {
            return sentinel.next.data;
        } else {
            LinkedListDeque<T> L = new LinkedListDeque<>();
            L.sentinel = this.sentinel;
            L.removeFirst();
            return getRecursive(index - 1);
        }
    }

    public Iterator<T> iterator(){
        return new LinkedListIterator();
    }

    private class LinkedListIterator implements Iterator<T> {
        private Node pos;

        public LinkedListIterator(){
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
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        LinkedListDeque<T> LLD = (LinkedListDeque<T>) obj;
        if (this.size() != LLD.size()) {
            return false;
        }
        int index = 0;
        for (T item : this){
            if (LLD.get(index) != item) return false;
            index += 1;
        }
        return true;
    }

    public static void main(String[] args){
        LinkedListDeque<Integer> L = new LinkedListDeque<>();
        L.addLast(5);
        LinkedListDeque<Integer> l = new LinkedListDeque<>();
        l.addLast(5);
        System.out.println(L.equals(l));
    }
}