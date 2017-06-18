package otus.hw3;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public class MyArrayList<T> implements List<T> {
    private T[] array;
    private int currentIndex = -1;
    private Class<T> genericClass;

    private final int startSize = 3;
    private final float multiplier = 1.5f;

    public MyArrayList(Class<T> c) {
        this.genericClass = c;
        this.array = (T[]) Array.newInstance(c, startSize);
    }

    private MyArrayList(T[] array, int currentIndex) {
        this.array = array;
        this.currentIndex = currentIndex;
    }

    private void resize() {
        int newSize = Math.round(array.length * multiplier);
        T[] newArray = (T[]) Array.newInstance(this.genericClass, newSize);

        for (int i = 0; i <= currentIndex; i++) newArray[i] = array[i];

        array = newArray;
    }

    private void resize(int size) {
        int newSize = Math.round(size * multiplier);
        T[] newArray = (T[]) Array.newInstance(this.genericClass, newSize);

        for (int i = 0; i <= currentIndex; i++) newArray[i] = array[i];

        array = newArray;
    }

    @Override
    public String toString() {
        if (isEmpty()) return "[]";

        String result = "[";
        for (int i = 0; i <= currentIndex; i++) result += array[i] + ", ";
        return result.substring(0, result.length() - 2) + "]";
    }

    @Override
    public void replaceAll(UnaryOperator<T> unaryOperator) {
        for (int i = 0; i <= currentIndex; i++) {
            array[i] = unaryOperator.apply(array[i]);
        }
    }

    @Override
    public void sort(Comparator<? super T> comparator) {
        // I know there are nlogn methods...
        for (int i = 0; i <= currentIndex; i++) {
            for (int j = 0; j <= currentIndex; j++) {
                if (comparator.compare(array[i], array[j]) == -1) {
                    T temp = array[i];
                    array[i] = array[j];
                    array[j] = array[i];
                }
            }
        }
    }

    @Override
    public Spliterator<T> spliterator() {
        throw new NotImplementedException();
    }

    @Override
    public boolean removeIf(Predicate<? super T> predicate) {
        boolean result = false;

        for (int i = 0; i <= currentIndex; i++) {
            if (predicate.test(array[i])) {
                remove(i);
                result = true;
            }
        }

        return result;
    }

    @Override
    public Stream<T> stream() {
        throw new NotImplementedException();
    }

    @Override
    public Stream<T> parallelStream() {
        throw new NotImplementedException();
    }

    @Override
    public void forEach(Consumer<? super T> consumer) {
        for (int i = 0; i <= currentIndex; i++) consumer.accept(array[i]);
    }

    @Override
    public int size() {
        return currentIndex + 1;
    }

    @Override
    public boolean isEmpty() {
        return currentIndex == -1;
    }

    @Override
    public boolean contains(Object o) {
        for (int i = 0; i <= currentIndex; i++) {
            if (o.equals(array[i])) return true;
        }
        return false;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index <= currentIndex;
            }

            @Override
            public T next() {
                return array[index++];
            }
        };
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOfRange(array, 0, currentIndex + 1);
    }

    @Override
    public <T1> T1[] toArray(T1[] t1s) {
        throw new NotImplementedException();
    }

    @Override
    public boolean add(T t) {
        if ((currentIndex+1) == array.length) resize();

        array[++currentIndex] = t;

        return true;
    }

    @Override
    public boolean remove(Object o) {
        for (int i = 0; i <= currentIndex; i++) {
            if (o.equals(array[i])) {
                remove(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        for (Object o : collection) if(!contains(o)) return false;

        return true;
    }

    @Override
    public boolean addAll(Collection<? extends T> collection) {
        for (T o : collection) add(o);

        return true;
    }

    @Override
    public boolean addAll(int i, Collection<? extends T> collection) {
        T[] temp = (T[]) Array.newInstance(genericClass, currentIndex - i);
        System.arraycopy(array, i+1, temp, 0, currentIndex - i);

        currentIndex = i;
        for(T o : collection) add(o);
        for (T t : temp) add(t);

        return true;
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        boolean result = false;
        for (Object o : collection) {
            for (int i = 0; i <= currentIndex; i++) {
                if (o.equals(array[i])) {
                    remove(i);
                    result = true;
                }
            }
        }

        return result;
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        for (Object o : collection) {
            for (int i = 0; i <= currentIndex; i++) {
                if (!o.equals(array[i])) {
                    remove(i);
                }
            }
        }

        return !isEmpty();
    }

    @Override
    public void clear() {
        currentIndex = -1;
    }

    @Override
    public T get(int i) {
        return array[i];
    }

    @Override
    public T set(int i, T t) {
        if (i > currentIndex) throw new IndexOutOfBoundsException();

        array[i] = t;

        return t;
    }

    @Override
    public void add(int i, T t) {
        set(i, t);
    }

    @Override
    public T remove(int i) {
        if (i > currentIndex) throw new IndexOutOfBoundsException();

        T result = array[i];
        for (int ii = (i+1); ii <= currentIndex; ii++) {
            array[ii-1] = array[ii];
        }
        currentIndex--;

        return result;
    }

    @Override
    public int indexOf(Object o) {
        for (int i = 0; i <= currentIndex; i++) if(o.equals(array[i])) return i;
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        for (int i = currentIndex; i > 0; i--) if(o.equals(array[i])) return i;
        return 0;
    }

    @Override
    public ListIterator<T> listIterator() {
        return new ListIterator<T>() {
            int index = 0;

            @Override
            public boolean hasNext() {
                return index < currentIndex;
            }

            @Override
            public T next() {
                return array[++index];
            }

            @Override
            public boolean hasPrevious() {
                return index >= 0;
            }

            @Override
            public T previous() {
                return array[--index];
            }

            @Override
            public int nextIndex() {
                return index + 1;
            }

            @Override
            public int previousIndex() {
                return index - 1;
            }

            @Override
            public void remove() {
                MyArrayList.this.remove(index);
            }

            @Override
            public void set(T t) {
                MyArrayList.this.set(index, t);
            }

            @Override
            public void add(T t) {
                MyArrayList.this.add(t);
            }
        };
    }

    @Override
    public ListIterator<T> listIterator(int i) {
        return new ListIterator<T>() {
            int index = i;

            @Override
            public boolean hasNext() {
                return index < currentIndex;
            }

            @Override
            public T next() {
                return array[++index];
            }

            @Override
            public boolean hasPrevious() {
                return index >= i;
            }

            @Override
            public T previous() {
                return array[--index];
            }

            @Override
            public int nextIndex() {
                return index + 1;
            }

            @Override
            public int previousIndex() {
                return index - 1;
            }

            @Override
            public void remove() {
                MyArrayList.this.remove(index);
            }

            @Override
            public void set(T t) {
                MyArrayList.this.set(index, t);
            }

            @Override
            public void add(T t) {
                MyArrayList.this.add(t);
            }
        };
    }

    @Override
    public List<T> subList(int i, int i1) {
        if (i1 <= i) return new MyArrayList<T>(this.genericClass);

        int newSize = Math.round((i1 - i) * multiplier);
        T[] newArray = (T[]) new Object[newSize];

        int j = 0;
        for (int ii = i; ii < i1; ii++) {
            newArray[j] = array[ii];
            j++;
        }

        return new MyArrayList<T>(newArray, j-1);
    }
}
