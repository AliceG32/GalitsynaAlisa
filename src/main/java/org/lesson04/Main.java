package org.lesson04;

import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws SortListMaxSizeException {
        SortService sortService = new SortService(
            List.of(
                new BubbleSort(),
                new MergeSort()
            )
        );

        List<Integer> origList = Arrays.asList(2, 1, 3, 5, 2, 3, 6, 7, 9);

        System.out.print("Исходный массив: ");
        System.out.println(Arrays.toString(origList.toArray()));
        System.out.print("Сортировка слиянием: ");
        System.out.println(Arrays.toString(sortService.sort(origList, SortType.MERGE_SORT).toArray()));

        System.out.print("Исходный массив: ");
        System.out.println(Arrays.toString(origList.toArray()));
        System.out.print("Сортировка пузырьком: ");
        System.out.println(Arrays.toString(sortService.sort(origList, SortType.BUBBLE_SORT).toArray()));
    }
}
