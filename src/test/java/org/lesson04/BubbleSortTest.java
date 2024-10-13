package org.lesson04;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BubbleSortTest {
    @Test
    void testSort() throws SortListMaxSizeException {
        BubbleSort bubbleSort = new BubbleSort();
        List<Integer> origList = Arrays.asList(2, 1, 3);
        bubbleSort.sortList(origList);
        assertEquals(origList, Arrays.asList(1, 2, 3));
    }

    @Test
    void testMaxSize() {
        BubbleSort bubbleSort = new BubbleSort();
        List<Integer> origList = Arrays.asList(2, 1, 3, 3, 4, 5, 6, 7, 8, 9, 1, 2);
        assertThrows(SortListMaxSizeException.class, () -> bubbleSort.sortList(origList));
    }

    @Test
    void testNull() {
        BubbleSort bubbleSort = new BubbleSort();
        assertThrows(IllegalArgumentException.class, () -> bubbleSort.sortList(null));
    }

    @Test
    void testType() {
        BubbleSort bubbleSort = new BubbleSort();
        assertEquals(SortType.BUBBLE_SORT, bubbleSort.type());
    }
}