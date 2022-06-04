package deque;

public class ArrayDeque<T> {
    private T[] array;
    private int nextFirst;
    private int nextLast;
    private int size;
    private int capacity;

    private void resize(int n){
        T[] temp = (T[]) new Object[n];
        int arrayIndex = (nextFirst == capacity - 1) ? 0 : nextFirst + 1;
        nextFirst = n/4 - 1;
        nextLast = nextFirst + size + 1;
        int tempIndex = (nextFirst + 1 < n) ? nextFirst + 1 : 0;
        for (int i = 0; i < size; i += 1, arrayIndex += 1, tempIndex += 1){
            if (tempIndex >= n) tempIndex = 0;
            if (arrayIndex >= capacity) arrayIndex = 0;
            temp[tempIndex] = array[arrayIndex];
        }
        array = temp;
        capacity = n;
    }

    public ArrayDeque() {
        array = (T []) new Object[8];
        nextFirst = 3;
        nextLast = 4;
        size = 0;
        capacity = 8;
    }

    public void addFirst(T item){
        if (size == capacity){
            resize(2 * capacity);
        }
        array[nextFirst] = item;
        nextFirst = (nextFirst == 0) ? capacity - 1 : nextFirst - 1;
        size += 1;
    }

    public void addLast(T item){
        if (size == capacity){
            resize(2 * capacity);
        }
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
        if (size == capacity/4 && capacity > 8){
            resize(capacity/2);
        }
        nextFirst = (nextFirst == capacity - 1) ? 0 : nextFirst + 1;
        T out = array[nextFirst];
        array[nextFirst] = null;
        size = (size == 0) ? 0 : size - 1;
        return out;
    }

    public T removeLast(){
        if (size == capacity/4 && capacity > 8){
            resize(capacity/2);
        }
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

    public static void main(String[] args){
        ArrayDeque<Integer> A = new ArrayDeque<>();
        for (int i = 0; i < 17; i++){
            A.addLast(i);
        }
        for (int i = 0; i < 14; i++){
            A.removeFirst();
        }
    }
}