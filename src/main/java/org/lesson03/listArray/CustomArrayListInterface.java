package org.lesson03.listArray;

/**
 * @param <T>
 */
public interface CustomArrayListInterface<T> {
  /**
   * Add item to array
   *
   * @param item T
   */
  void add(T item);

  /**
   * Get item by index from array
   *
   * @param index integer
   * @return T
   */
  T get(int index);

  /**
   * Remove item by index from array
   *
   * @param index integer
   */
  void remove(int index);

  /**
   * Convert list to string
   * @return string
   */
  String toString();
}
