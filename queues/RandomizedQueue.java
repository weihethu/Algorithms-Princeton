import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private int arrayLength, size;
    private Item[] array;

    public RandomizedQueue() {
        arrayLength = 1;
        array = (Item[]) (new Object[arrayLength]);
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void enqueue(Item item) {
        if (item == null) {
            throw new NullPointerException();
        }

        extend();
        array[size++] = item;
    }

    private void swap(int index1, int index2) {
        if (index1 != index2) {
            Item item = array[index1];
            array[index1] = array[index2];
            array[index2] = item;
        }
    }

    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        int index = StdRandom.uniform(size);
        Item result = array[index];
        swap(index, size - 1);
        array[size - 1] = null;
        size--;

        shrink();
        return result;
    }

    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        int index = StdRandom.uniform(size);

        return array[index];
    }

    public Iterator<Item> iterator() {
        return new Iterator<Item>() {
            private Item[] randomizedArray;
            private int curIndex;
            
            {
                randomizedArray = (Item[]) (new Object[size]);

                RandomizedQueue<Item> copy = new RandomizedQueue<Item>();
                copy.size = size;
                copy.arrayLength = arrayLength;
                copy.array = array.clone();

                int index = 0;
                while (!copy.isEmpty()) {
                    randomizedArray[index++] = copy.dequeue();
                }
                curIndex = 0;
            }
            
            @Override
            public boolean hasNext() {
                return curIndex < randomizedArray.length;
            }

            @Override
            public Item next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return randomizedArray[curIndex++];
            }
            
        };
    }

    private void extend() {
        if (size == arrayLength) {
            Item[] newArray = (Item[]) (new Object[2 * arrayLength]);
            for (int i = 0; i < size; i++) {
                newArray[i] = array[i];
                array[i] = null;
            }
            arrayLength *= 2;
            array = newArray;
        }
    }

    private void shrink() {
        if (size < arrayLength / 4) {
            Item[] newArray = (Item[]) (new Object[arrayLength / 2]);

            for (int i = 0; i < size; i++) {
                newArray[i] = array[i];
                array[i] = null;
            }
            arrayLength = arrayLength / 2;
            array = newArray;
        }
    }

    public static void main(String[] args) {
        RandomizedQueue<Integer> rq = new RandomizedQueue<Integer>();

        while (true) {
            try {
                String operation = StdIn.readString();

                if (operation.equals("end")) {
                    break;
                } else if (operation.equals("eq")) {
                    int op = StdIn.readInt();

                    rq.enqueue(op);
                } else if (operation.equals("dq")) {
                    System.out.println(rq.dequeue());
                } else if (operation.equals("s")) {
                    System.out.println(rq.sample());
                } else if (operation.equals("disp")) {
                    Iterator<Integer> iter = rq.iterator();

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
