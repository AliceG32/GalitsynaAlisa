package org.lesson04;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SortServiceTest {
    @Test
    void testNotExistsSortType() {
        List<Integer> origList = Arrays.asList(2, 1, 3);
        SortService sortService = new SortService(
                List.of(
                        new BubbleSort()
                )
        );
        assertThrows(SortListMaxSizeException.class, () -> sortService.sort(origList, SortType.MERGE_SORT));
    }

    @Test
    void testListNull() {
        SortService sortService = new SortService(
                List.of(
                        new BubbleSort()
                )
        );
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class, () -> sortService.sort(null, SortType.MERGE_SORT)
        );
        assertEquals("Invalid list value", exception.getMessage());
    }

    @Test
    void testTypeNull() {
        List<Integer> origList = Arrays.asList(2, 1, 3);
        SortService sortService = new SortService(
                List.of(
                        new BubbleSort()
                )
        );
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class, () -> sortService.sort(origList, null)
        );
        assertEquals("Invalid type value", exception.getMessage());
    }

    @Test
    void testMaxListSize() {
        List<Integer> origList = Arrays.asList(2, 1, 3, 3, 4, 5, 3, 2, 1, 2, 3, 4);
        SortService sortService = new SortService(
                List.of(
                        new BubbleSort()
                )
        );
        assertThrows(SortListMaxSizeException.class, () -> sortService.sort(origList, SortType.BUBBLE_SORT));
    }
    @Test
    void testSort() throws SortListMaxSizeException {
        List<Integer> origList = Arrays.asList(2, 1, 3);
        SortService sortService = new SortService(
                List.of(
                        new BubbleSort()
                )
        );
        List<Integer> result = sortService.sort(origList, SortType.BUBBLE_SORT);
        assertEquals(result, Arrays.asList(1, 2, 3));
        assertEquals(origList, Arrays.asList(2, 1, 3));
    }
}