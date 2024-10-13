package org.lesson04;

import java.util.List;

public class BubbleSort implements SortInterface {
    final private int MAX_SIZE = 10;

    @Override
    public SortType type() {
        return SortType.BUBBLE_SORT;
    }

    @Override
    public void sortList(List<Integer> list) throws SortListMaxSizeException {
        if (list == null) {
            throw new IllegalArgumentException();
        }
        if (list.size() > MAX_SIZE) {
            throw new SortListMaxSizeException("Превышен размер массива для сортировки.");
        }
        for (int i = 0; i < list.size(); i++) {
            for (int j = i + 1; j < list.size(); j++) {
                if (list.get(i) > list.get(j)) {
                    int temp = list.get(i);
                    list.set(i, list.get(j));
                    list.set(j, temp);
                }
            }
        }

    }
}
