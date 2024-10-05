package org.lesson03.listArray;

public class CustomArrayList<T> implements CustomArrayListInterface<T> {
  private final int SIZE = 10;
  private final int GROW_SIZE = 10;
  private Object[] array = new Object[SIZE];
  private int current_size = 0;

  @Override
  public String toString() {
    StringBuilder resultString = new StringBuilder("[");
    if (current_size > 0) {
      resultString.append(array[0].toString());
      for (int i = 1; i < current_size; i++) {
        resultString.append(", ").append(array[i].toString());
      }
    }

    return resultString + "]";
  }

  private void checkNull(Object object) {
    if (object == null ) {
      throw new NullPointerException();
    }
  }

  private void resize(int length) {
    Object[] newArray = new Object[length];
    System.arraycopy(array, 0, newArray, 0, current_size);
    array = newArray;
  }

  private boolean isEmpty() {
    return current_size == 0;
  }

  private void shiftLeft(int index) {
    if (!isEmpty()) {
      System.arraycopy(array, index + 1, array, index, current_size - index);
    }
  }

  @Override
  public void add(T item) {
    checkNull(item);

    if(current_size == array.length) {
      resize(current_size + GROW_SIZE);
    }
    array[current_size] = item;
    current_size++;
  }

  @Override
  public T get(int index) {
    if (!isEmpty()) {
      return (T) array[current_size - 1];
    }

    throw new IndexOutOfBoundsException();
  }

  @Override
  public void remove(int index) {
    if (index < current_size - 1) {
      shiftLeft(index);
    }

    current_size--;
  }
}
