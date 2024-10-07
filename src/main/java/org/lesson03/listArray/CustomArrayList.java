package org.lesson03.listArray;

public class CustomArrayList<T> implements CustomArrayListInterface<T> {
  private final int SIZE = 10;
  private final int GROW_SIZE = 10;
  private Object[] array = new Object[SIZE];
  private int currentSize = 0;

  @Override
  public String toString() {
    StringBuilder resultString = new StringBuilder("[");
    if (currentSize > 0) {
      resultString.append(array[0].toString());
      for (int i = 1; i < currentSize; i++) {
        resultString.append(", ").append(array[i].toString());
      }
    }

    return resultString + "]";
  }

  private void checkNull(Object object) {
    if (object == null ) {
      throw new IllegalArgumentException();
    }
  }

  private void resize(int length) {
    Object[] newArray = new Object[length];
    System.arraycopy(array, 0, newArray, 0, currentSize);
    array = newArray;
  }

  private boolean isEmpty() {
    return currentSize == 0;
  }

  private void shiftLeft(int index) {
    if (!isEmpty()) {
      System.arraycopy(array, index + 1, array, index, currentSize - index);
    }
  }

  @Override
  public void add(T item) {
    checkNull(item);

    if(currentSize == array.length) {
      resize(currentSize + GROW_SIZE);
    }
    array[currentSize] = item;
    currentSize++;
  }

  @Override
  public T get(int index) {
    if (!isEmpty()) {
      return (T) array[currentSize - 1];
    }

    throw new IndexOutOfBoundsException();
  }

  @Override
  public void remove(int index) {
    if (index < currentSize - 1) {
      shiftLeft(index);
    }
    currentSize--;
  }
}
