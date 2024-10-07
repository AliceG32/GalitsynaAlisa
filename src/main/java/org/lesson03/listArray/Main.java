package org.lesson03.listArray;

public class Main {
  public static void main(String[] args) {
    CustomArrayList<Integer> array = new CustomArrayList<>();
    array.add(1);
    array.add(2);
    array.add(3);
    System.out.println(array);
    array.remove(1);
    System.out.println(array);
    System.out.println(array.get(1));
  }
}
