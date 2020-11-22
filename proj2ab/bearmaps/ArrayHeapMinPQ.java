package bearmaps;

import java.util.*;

public class ArrayHeapMinPQ<T> implements ExtrinsicMinPQ<T>{
    private final List<Node> heap;
    private final Map<T, Integer> items;

    ArrayHeapMinPQ() {
        heap = new ArrayList<>();
        heap.add(new Node(null, -1)); // dummy root

        items = new HashMap<>();
    }

    @Override
    public void add(T item, double priority) {
        if(!contains(item)) {
            heap.add(new Node(item, priority));
            items.put(item, heap.size() - 1);
            swim(heap.size() - 1);
        }
    }

    @Override
    public boolean contains(T item) {
        return items.containsKey(item);
    }

    @Override
    public T getSmallest() {
        return heap.get(1).item;
    }

    @Override
    public T removeSmallest() {
        if(size() > 1) {
            Node smallest = heap.get(1);
            T item = smallest.item;
            swap(smallest, heap.get(size()));

            heap.remove(size());
            items.remove(item);

            items.put(heap.get(1).item, 1);
            sink(1);
            return item;
        } else if(size() == 1) {
            T item = heap.remove(1).item;
            items.remove(item);
            return item;
        } else {
            return null;
        }


    }

    @Override
    public int size() {
        return this.items.size();
    }

    @Override
    public void changePriority(T item, double priority) {
        if(!contains(item)) {
            throw new IllegalArgumentException(item + " doesn't exist");
        } else {
            int index = items.get(item);
            Node curr = heap.get(index);
            if(curr.priority > priority) {
                curr.priority = priority;
                swim(index);
            } else if (curr.priority < priority) {
                curr.priority = priority;
                sink(index);
            }
        }
    }

    private void swim(int index) {
        Node curr = heap.get(index);
        int parentIndex = parent(index);
        Node parent = heap.get(parentIndex);
        while (parent != null && parent.priority > curr.priority) {
            items.put(curr.item, parentIndex);
            items.put(parent.item, index);
            swap(parent, curr);

            index = parentIndex;
            curr = parent;
            parentIndex = parent(index);
            parent = heap.get(parentIndex);
        }
    }

    private void sink(int index) {
        Node curr = heap.get(index);

        int leftChildIndex = leftChild(index);
        int rightChildIndex = rightChild(index);

        int childIndex = -1;
        if(leftChildIndex <= size() && rightChildIndex <= size()) {
            Node leftChild = heap.get(leftChildIndex);
            Node rightChild = heap.get(rightChildIndex);
            childIndex = leftChild.priority < rightChild.priority ? leftChildIndex : rightChildIndex;
        } else if(leftChildIndex <= size()) {
            childIndex = leftChildIndex;
        }

        if(childIndex > 0) {
            Node child = heap.get(childIndex);
            if(child.priority < curr.priority) {
                items.put(curr.item, childIndex);
                items.put(child.item, index);
                swap(curr, child);
                sink(childIndex);
            }
        }
    }

    private void swap(Node left, Node right) {
        if(left != null && right != null) {
            Node tmp = new Node(left);
            left.item = right.item;
            left.priority = right.priority;

            right.item = tmp.item;
            right.priority = tmp.priority;
        } else {
            throw new UnsupportedOperationException("cannot swap null node");
        }
    }

    private int parent(int index) {
        return index / 2;
    }

    private int leftChild(int index) {
        return index * 2;
    }

    private int rightChild(int index) {
        return index * 2 + 1;
    }

    private class Node implements Comparable<Node> {
        T item;
        double priority;

        Node(T item, double priority) {
            this.item = item;
            this.priority = priority;
        }

        Node(Node other) {
            this.item = other.item;
            this.priority = other.priority;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return Double.compare(node.priority, priority) == 0 &&
                    Objects.equals(item, node.item);
        }

        @Override
        public int hashCode() {
            return Objects.hash(item, priority);
        }

        @Override
        public int compareTo(Node o) {
            if(o == null) {
                return -1;
            }
            return Double.compare(this.priority, o.priority);
        }
    }
}
