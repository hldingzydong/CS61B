public class ArrayDeque<T> {
    private T[] items;
    private int nextFirst;
    private int nextLast;
    private int size;

    ArrayDeque() {
        this.items = (T[]) new Object[8];
        this.nextFirst = 0;
        this.nextLast = 1;
        this.size = 0;
    }

    ArrayDeque(ArrayDeque<T> other) {
        this.items = (T[]) new Object[other.items.length];
        this.nextFirst = other.nextFirst;
        this.nextLast = other.nextLast;
        this.size = other.size;

        System.arraycopy(other.items, 0, this.items, 0, other.items.length);
    }

    private void resize(int capacity) {
        T[] a = (T[]) new Object[capacity];
        int i = this.nextFirst + 1;
        for(; i < this.nextFirst + this.items.length; i++) {
            a[i] = this.items[i % this.items.length];
        }
        this.nextLast = i;
        this.items = a;
    }

    private void shrink(int capacity) {
        T[] a = (T[]) new Object[capacity];
        int i = this.nextFirst + 1;
        int j = 1;
        if(this.nextLast > this.nextFirst) {
            for(; i < this.nextLast + this.items.length; i++) {
                a[j] = this.items[i % this.items.length];
                j++;
            }
        } else {
            for(; i < this.nextLast; i++) {
                a[j] = this.items[i];
                j++;
            }
        }

        this.nextFirst = 0;
        this.nextLast = j;
        this.items = a;
    }

    public void addFirst(T item) {
        if(this.nextFirst == this.nextLast) {
            resize(this.items.length * 2);
        }
        this.items[this.nextFirst] = item;
        this.nextFirst = (this.nextFirst + this.items.length - 1) % this.items.length;
        this.size += 1;
    }

    public T removeFirst() {
        if(this.size == this.items.length * 0.25) {
            shrink(this.items.length / 2);
        }
        this.nextFirst = (this.nextFirst + 1) % this.items.length;
        this.size -= 1;
        T retVal = this.items[this.nextFirst];
        // for GC
        this.items[this.nextFirst] = null;
        return retVal;
    }

    public void addLast(T item) {
        if(this.nextFirst == this.nextLast) {
            resize(this.size * 2);
        }
        this.items[this.nextLast] = item;
        this.nextLast = (this.nextLast + 1) % this.items.length;
        this.size += 1;
    }

    public T removeLast() {
        if(this.size == this.items.length * 0.25) {
            shrink(this.items.length / 2);
        }
        this.nextLast = (this.nextLast + this.items.length - 1) % this.items.length;
        T retVal = this.items[this.nextLast];
        // for GC
        this.items[this.nextLast] = null;
        this.size -= 1;
        return retVal;
    }

    public T get(int index) {
        return this.items[(this.nextFirst + 1 + index) % this.items.length];
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public int size() {
        return this.size;
    }

    public void printDeque() {
        int startIndex = (this.nextFirst + 1) % this.items.length;
        while (startIndex != this.nextLast) {
            System.out.print(this.items[startIndex] + " ");
            startIndex = (startIndex + 1) % this.items.length;
        }
        System.out.print("\n");
    }

    public static void main(String[] args) {
        System.out.println("****** test basic operation ******");
        ArrayDeque<String> stringDeque = new ArrayDeque<>();
        stringDeque.addFirst("hello");
        stringDeque.addLast(",");
        stringDeque.addLast("world");
        stringDeque.printDeque();

        stringDeque.addFirst("test-first-node");
        stringDeque.addLast("test-last-node");
        stringDeque.printDeque();

        System.out.println("****** test get by iteration ******");
        System.out.println("first node = " + stringDeque.get(0));
        System.out.println("last node = " + stringDeque.get(stringDeque.size() - 1));

        ArrayDeque<String> stringDequeCopy = new ArrayDeque<>(stringDeque);
        stringDeque.removeFirst();
        stringDeque.removeLast();

        System.out.println("****** test deep copy ******");
        stringDeque.printDeque();
        stringDequeCopy.printDeque();

        System.out.println("****** test resize ******");
        ArrayDeque<Integer> resizeArrayDeque = new ArrayDeque<>();
        int N = 9;
        for(int i = 0; i < N; i++) {
            resizeArrayDeque.addLast(i);
        }
        resizeArrayDeque.printDeque();
        for(int i = 0; i < N - 1; i++) {
            resizeArrayDeque.removeLast();
        }
        resizeArrayDeque.printDeque();
    }
}
