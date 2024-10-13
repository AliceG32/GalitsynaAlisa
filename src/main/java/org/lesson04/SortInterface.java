package org.lesson04;

import java.util.List;

public interface SortInterface {
    SortType type();
    void sortList(List<Integer> list) throws SortListMaxSizeException;
}
