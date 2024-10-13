package org.lesson04;


import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class SortService {
    private static final Logger LOG = Logger.getLogger("SortService");

    private final List<SortInterface> sortTypes;

    public SortService(List<SortInterface> sorts) {
        this.sortTypes = sorts;
    }

    public List<Integer> sort(List<Integer> list, SortType type) throws SortListMaxSizeException {
        if (list == null) {
            throw new IllegalArgumentException("Invalid list value");
        }
        if (type == null) {
            throw new IllegalArgumentException("Invalid type value");
        }
        List<Integer> listCopy = new ArrayList<>(list);
        boolean foundSort = false;
        for (SortInterface sortType : sortTypes) {
            if (sortType.type().equals(type)) {
                try {
                    sortType.sortList(listCopy);
                    foundSort = true;
                    break;
                } catch (SortListMaxSizeException exception) {
                    LOG.warning("Превышена длина массива.");
                }
            }
        }
        if (!foundSort) {
            throw new SortListMaxSizeException("Сортировка для данных длины и типа массива не найдена.");
        }

        return listCopy;
    }
}
