package dsgp6.fakebook.chat;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class HashSet<E> implements Set<E> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private Node<E>[] table;
    private int size;

    public HashSet() {
        this(DEFAULT_CAPACITY);
    }

    public HashSet(int capacity) {
        table = new Node[capacity];
        size = 0;
    }
    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }
    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        int hash = hash(o);
        Node<E> node = table[hash];
        while (node != null) {
            if (node.value.equals(o)) {
                return true;
            }
            node = node.next;
        }
        return false;
    }

    @Override
    public boolean add(E e) {
        if (contains(e)) {
            return false;
        }

        int hash = hash(e);
        Node<E> newNode = new Node<>(e);
        if (table[hash] == null) {
            table[hash] = newNode;
        } else {
            Node<E> node = table[hash];
            while (node.next != null) {
                node = node.next;
            }
            node.next = newNode;
        }
        size++;
        if (size > table.length * DEFAULT_LOAD_FACTOR) {
            resizeTable();
        }
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int hash = hash(o);
        Node<E> node = table[hash];
        Node<E> prev = null;
        while (node != null) {
            if (node.value.equals(o)) {
                if (prev == null) {
                    table[hash] = node.next;
                } else {
                    prev.next = node.next;
                }
                size--;
                return true;
            }
            prev = node;
            node = node.next;
        }
        return false;
    }

    @Override
    public void clear() {
        table = new Node[table.length];
        size = 0;
    }

    private int hash(Object o) {
        return Math.abs(o.hashCode()) % table.length;
    }

    private void resizeTable() {
        int newCapacity = table.length * 2;
        Node<E>[] newTable = new Node[newCapacity];
        for (Node<E> node : table) {
            while (node != null) {
                Node<E> next = node.next;
                int hash = hash(node.value);
                if (newTable[hash] == null) {
                    newTable[hash] = node;
                    node.next = null;
                } else {
                    node.next = newTable[hash];
                    newTable[hash] = node;
                }
                node = next;
            }
        }
        table = newTable;
    }

    @Override
    public Iterator<E> iterator() {
        return new HashSetIterator();
    }

    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];
        int index = 0;
        for (Node<E> node : table) {
            while (node != null) {
                array[index++] = node.value;
                node = node.next;
            }
        }
        return array;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        }

        Object[] result = a;
        int index = 0;
        for (Node<E> node : table) {
            while (node != null) {
                result[index++] = node.value;
                node = node.next;
            }
        }

        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }

    private static class Node<E> {
        E value;
        Node<E> next;

        Node(E value) {
            this.value = value;
        }
    }

    private class HashSetIterator implements Iterator<E> {
        private int currentBucket;
        private Node<E> currentNode;
        private Node<E> nextNode;

        HashSetIterator() {
            currentBucket = 0;
            currentNode = null;
            nextNode = null;
            findNextNode();
        }

        private void findNextNode() {
            if (nextNode != null && nextNode.next != null) {
                nextNode = nextNode.next;
            } else {
                while (currentBucket < table.length) {
                    if (table[currentBucket] != null) {
                        nextNode = table[currentBucket];
                        break;
                    }
                    currentBucket++;
                }
            }
        }

        @Override
        public boolean hasNext() {
            return nextNode != null;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new java.util.NoSuchElementException();
            }
            currentNode = nextNode;
            findNextNode();
            return currentNode.value;
        }

        @Override
        public void remove() {
            if (currentNode == null) {
                throw new IllegalStateException();
            }
            HashSet.this.remove(currentNode.value);
            currentNode = null;
        }
    }
}
