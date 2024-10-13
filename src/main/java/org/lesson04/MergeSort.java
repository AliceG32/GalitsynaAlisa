package org.lesson04;

import java.util.Collections;
import java.util.List;

public class MergeSort implements SortInterface {
    @Override
    public SortType type() {
        return SortType.MERGE_SORT;
    }

    @Override
    public void sortList(List<Integer> list) throws IllegalArgumentException {
        if (list == null) {
            throw new IllegalArgumentException();
        }

        Collections.sort(list);
    }
}
