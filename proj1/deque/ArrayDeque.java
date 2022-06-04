package deque;

public class ArrayDeque<T> {
    private T[] array;
    private int nextFirst;
    private int nextLast;
    private int size;
    private int capacity;

    /*private resize(int n){
        T[] temp = (T[]) new Object[n];

    }*/

    public ArrayDeque() {
        array = (T []) new Object[8];
        nextFirst = 3;
        nextLast = 4;
        size = 0;
        capacity = 8;
    }

    public void addFirst(T item){
        array[nextFirst] = item;
        nextFirst = (nextFirst == 0) ? capacity - 1 : nextFirst - 1;
        size += 1;
    }

    public void addLast(T item){
        array[nextLast] = item;
        nextLast = (nextLast == capacity - 1) ? 0 : nextLast + 1;
        size += 1;
    }

    public boolean isEmpty(){
        return size == 0;
    }

    public int size(){
        return size;
    }

    public void printDeque(){
        for (int i = nextFirst + 1; i != nextLast; i++){
            if (i == capacity){
                i = -1;
                continue;
            }
            System.out.print(array[i] + " ");
        }
    }

    public T removeFirst(){
        nextFirst = (nextFirst == capacity - 1) ? 0 : nextFirst + 1;
        T out = array[nextFirst];
        array[nextFirst] = null;
        size = (size == 0) ? 0 : size - 1;
        return out;
    }

    public T removeLast(){
        nextLast = (nextLast == 0) ? capacity - 1 : nextLast - 1;
        T out = array[nextLast];
        array[nextLast] = null;
        size = (size == 0) ? 0 : size - 1;
        return out;
    }

    public T get(int index){
        if (index >= size) return null;
        return array[(nextFirst + 1 + index) % capacity];
    }

    public static void main(String args[]){
        ArrayDeque<Integer> A = new ArrayDeque<>();
        A.addFirst(2);
        A.addFirst(1);
        A.addLast(3);
        A.addLast(4);
        A.addLast(5);
        A.addLast(6);
        A.addLast(7);
        A.addLast(8);
        A.removeLast();
    }
}