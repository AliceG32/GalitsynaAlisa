package org.lesson04;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MergeSortTest {
    @Test
    void testSort() {
        MergeSort mergeSort = new MergeSort();
        List<Integer> origList = Arrays.asList(2, 1, 3);
        mergeSort.sortList(origList);
        assertEquals(origList, Arrays.asList(1, 2, 3));
    }

    @Test
    void testNull() {
        MergeSort mergeSort = new MergeSort();
        assertThrows(IllegalArgumentException.class, () -> mergeSort.sortList(null));
    }

    @Test
    void testType() {
        MergeSort mergeSort = new MergeSort();
        assertEquals(SortType.MERGE_SORT, mergeSort.type());
    }
}