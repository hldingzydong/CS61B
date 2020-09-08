public class ArrayDeque<T> {
    private T[] items;
    private int front;
    private int tail;
    private int size;

    ArrayDeque() {
        this.items = (T[]) new Object[8];
        this.front = 0;
        this.tail = 0;
        this.size = 0;
    }

    ArrayDeque(ArrayDeque<T> other) {
        this.items = (T[]) new Object[8];
        this.front = other.front;
        this.tail = other.tail;
        this.size = other.size;

        System.arraycopy(other.items, 0, this.items, 0, other.items.length);
    }

    private void resize(int capacity) {
        T[] a = (T[]) new Object[capacity];
        System.arraycopy(this.items, 0, a, 0, this.items.length);
        if(this.front > this.tail) {
            int startIndex = (this.front + 1) % this.items.length;
            while (startIndex > 0) {
                a[startIndex + capacity - this.items.length] = a[startIndex];
                a[startIndex] = null;
                startIndex = (startIndex + 1) % this.items.length;
            }
            this.front = this.front + (capacity - this.items.length);
        } else if(this.front == this.tail) {
            this.tail = this.tail + this.items.length - 1;
        }
        this.items = a;

    }

    public void addFirst(T item) {
        if(this.size == this.items.length) {
            resize(this.items.length * 2);
        }
        this.items[this.front] = item;
        this.front = (this.front + this.items.length - 1) % this.items.length;
        this.size += 1;
    }

    public T removeFirst() {
        this.front = (this.front + 1) % this.items.length;
        this.size -= 1;
        T retVal = this.items[this.front];
        // for GC
        this.items[this.front] = null;
        return retVal;
    }

    public void addLast(T item) {
        if(this.size == this.items.length) {
            resize(this.size * 2);
        }
        this.tail = (this.tail + 1) % this.items.length;
        this.items[this.tail] = item;
        this.size += 1;
    }

    public T removeLast() {
        T retVal = this.items[this.tail];
        this.items[this.tail] = null;
        this.tail = (this.tail + this.items.length - 1) % this.items.length;
        this.size -= 1;
        return retVal;
    }

    public T get(int index) {
        return this.items[(this.front + 1 + index) % this.items.length];
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public int size() {
        return this.size;
    }

    public void printDeque() {
        int startIndex = (this.front + 1) % this.items.length;
        while (startIndex != this.tail) {
            System.out.print(this.items[startIndex] + " ");
            startIndex = (startIndex + 1) % this.items.length;
        }
        System.out.print(this.items[startIndex] + " ");
        System.out.print("\n");
    }

    public static void main(String[] args) {
        ArrayDeque<String> stringDeque = new ArrayDeque<>();
        stringDeque.addFirst("hello");
        stringDeque.addLast(",");
        stringDeque.addLast("world");
        stringDeque.printDeque();

        stringDeque.addFirst("test-first-node");
        stringDeque.addLast("test-last-node");
        stringDeque.printDeque();

        System.out.println("get by iteration:");
        System.out.println("first node = " + stringDeque.get(0));
        System.out.println("last node = " + stringDeque.get(stringDeque.size() - 1));

        ArrayDeque<String> stringDequeCopy = new ArrayDeque<>(stringDeque);
        stringDeque.removeFirst();
        stringDeque.removeLast();

        System.out.println("test deep copy:");
        stringDeque.printDeque();
        stringDequeCopy.printDeque();

        System.out.println("test resize");
        ArrayDeque<Integer> resizeArrayDeque = new ArrayDeque<>();
        int N = 9;
        for(int i = 0; i < N; i++) {
            resizeArrayDeque.addLast(i);
        }
        resizeArrayDeque.printDeque();
    }
}
