package cache;

public class DoublyLinkedList<K, V> {

    private final LFUNode<K, V> head;
    private final LFUNode<K, V> tail;

    private int size;

    public DoublyLinkedList() {

        head = new LFUNode<>(null, null);
        tail = new LFUNode<>(null, null);

        head.next = tail;
        tail.prev = head;
    }

    public void addFirst(LFUNode<K, V> node) {

        node.next = head.next;
        node.prev = head;

        head.next.prev = node;
        head.next = node;

        size++;
    }

    public void remove(LFUNode<K, V> node) {

        node.prev.next = node.next;
        node.next.prev = node.prev;

        size--;
    }

    public LFUNode<K, V> removeLast() {

        if (size == 0) {
            return null;
        }

        LFUNode<K, V> node = tail.prev;

        remove(node);

        return node;
    }

    public boolean isEmpty() {
        return size == 0;
    }
}