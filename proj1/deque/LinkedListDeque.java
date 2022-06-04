package deque;

public class LinkedListDeque<T>{
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

    public boolean isEmpty(){
        return size == 0;
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


    public static void main(String[] args){
        LinkedListDeque<Integer> L = new LinkedListDeque<>();
        L.addLast(5); L.addFirst(6); L.addLast(7);
        System.out.println(L.getRecursive(0));
    }


}