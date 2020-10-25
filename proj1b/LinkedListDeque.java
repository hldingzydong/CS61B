public class LinkedListDeque<T> implements Deque<T> {
    public class Node {
        T item;
        Node prev;
        Node next;

        Node(T value, Node prev, Node next) {
            this.item = value;
            this.prev = prev;
            this.next = next;
        }

        void setPrev(Node prev) {
            this.prev = prev;
        }

        void setNext(Node next) {
            this.next = next;
        }

        T getIndex(int index) {
            if(index == 0) {
                return this.item;
            }
            return this.next.getIndex(index - 1);
        }


        @Override
        public String toString() {
            return "Node[item=" + this.item + "]";
        }
    }

    private final Node sentinel;
    private int size;

    public LinkedListDeque() {
        this.sentinel = new Node(null, null, null);
        this.sentinel.setPrev(this.sentinel);
        this.sentinel.setNext(this.sentinel);
        this.size = 0;
    }

    public LinkedListDeque(LinkedListDeque<T> other) {
        this();
        int srcSize = other.size();
        Node srcNode = other.sentinel.next;
        while (srcSize > 0) {
            this.addLast(srcNode.item);
            srcNode = srcNode.next;
            srcSize -= 1;
        }
    }

    @Override
    public void addFirst(T item) {
        Node firstItem = new Node(item, this.sentinel, this.sentinel.next);
        this.sentinel.next.setPrev(firstItem);
        this.sentinel.setNext(firstItem);
        this.size += 1;
    }

    @Override
    public T removeFirst() {
        if(this.size < 1) {
            throw new UnsupportedOperationException("cannot remove first element from a empty list");
        }
        this.sentinel.next.next.setPrev(this.sentinel);
        T retVal = this.sentinel.next.item;
        this.sentinel.setNext(this.sentinel.next.next);
        this.size -= 1;
        return retVal;
    }

    @Override
    public void addLast(T item) {
        Node lastItem = new Node(item, this.sentinel.prev, this.sentinel);
        this.sentinel.prev.setNext(lastItem);
        this.sentinel.setPrev(lastItem);
        this.size += 1;
    }

    @Override
    public T removeLast() {
        if(this.size < 1) {
            throw new UnsupportedOperationException("cannot remove last element from a empty list");
        }
        this.sentinel.prev.prev.setNext(this.sentinel);
        T retval = this.sentinel.prev.item;
        this.sentinel.setPrev(this.sentinel.prev.prev);
        this.size -= 1;
        return retval;
    }

    @Override
    public int size() {
        return this.size;
    }

    public void printDeque() {
        Node printNode = this.sentinel.next;
        while (printNode != this.sentinel) {
            System.out.print(printNode + " ");
            printNode = printNode.next;
        }
        System.out.print("\n");
    }

    @Override
    public T get(int index) {
        Node getNode = this.sentinel.next;
        while (index > 0) {
            getNode = getNode.next;
            index -= 1;
        }
        return getNode.item;
    }

    public T getRecursive(int index) {
        return this.sentinel.next.getIndex(index);
    }

    public static void main(String[] args) {
        LinkedListDeque<String> stringDeque = new LinkedListDeque<>();
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

        System.out.println("get by recursion:");
        System.out.println("first node = " + stringDeque.getRecursive(0));
        System.out.println("last node = " + stringDeque.getRecursive(stringDeque.size() - 1));

        LinkedListDeque<String> stringDequeCopy = new LinkedListDeque<>(stringDeque);
        stringDeque.removeFirst();
        stringDeque.removeLast();

        System.out.println("test deep copy:");
        stringDeque.printDeque();
        stringDequeCopy.printDeque();
    }
}
