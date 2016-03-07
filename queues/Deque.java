import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdIn;

public class Deque<Item> implements Iterable<Item> {
    private class Element {
        private Item item;
        private Element prev, next;

        public Element(Item item) {
            this.item = item;
            prev = null;
            next = null;
        }
    }

    private Element first, last;
    private int size;

    public Deque() {
        first = null;
        last = null;
        size = 0;
    }

    public boolean isEmpty() {
        return first == null;
    }

    public int size() {
        return size;
    }

    public void addFirst(Item item) {
        if (item == null) {
            throw new NullPointerException();
        }
        Element newFirst = new Element(item);
        newFirst.next = first;
        if (first != null) {
            first.prev = newFirst;
        }
        first = newFirst;
        size++;

        if (last == null) {
            last = newFirst;
        }
    }

    public void addLast(Item item) {
        if (item == null) {
            throw new NullPointerException();
        }
        Element newLast = new Element(item);
        if (last == null) {
            first = newLast;
            last = newLast;
        } else {
            last.next = newLast;
            newLast.prev = last;
            last = newLast;
        }

        size++;
    }

    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        Item result = first.item;

        if (first.next != null) {
            first.next.prev = null;
            first = first.next;
        } else {
            first = null;
            last = null;
        }

        size--;

        return result;
    }

    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        Item result = last.item;

        if (last.prev != null) {
            last.prev.next = null;
            last = last.prev;
        } else {
            first = null;
            last = null;
        }

        size--;

        return result;
    }

    public Iterator<Item> iterator() {
        return new Iterator<Item>() {
            private Element curElement;
            {
                curElement = first;
            }
            
            public boolean hasNext() {
                return curElement != null;
            }
            
            public Item next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                Item result = curElement.item;
                curElement = curElement.next;
                return result;
            }
        };
    }

    public static void main(String[] args) {
        Deque<Integer> dequeue = new Deque<Integer>();

        while (true) {
            try {
                String operation = StdIn.readString();

                if (operation.equals("end")) {
                    break;
                } else if (operation.equals("af")) {
                    int op = StdIn.readInt();

                    dequeue.addFirst(op);
                } else if (operation.equals("rf")) {
                    dequeue.removeFirst();
                } else if (operation.equals("al")) {
                    int op = StdIn.readInt();

                    dequeue.addLast(op);
                } else if (operation.equals("rl")) {
                    dequeue.removeLast();
                } else if (operation.equals("disp")) {
                    Iterator<Integer> iter = dequeue.iterator();

                    System.out.println("Begin iterating:");
                    while (iter.hasNext()) {
                        System.out.println(iter.next());
                    }
                    System.out.println("End.");
                } else {
                    System.out.println("Unsupported operation");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
