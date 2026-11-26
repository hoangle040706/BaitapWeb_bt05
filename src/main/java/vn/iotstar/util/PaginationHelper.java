package vn.iotstar.util;

import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

public final class PaginationHelper {

    private static final int DEFAULT_WINDOW_SIZE = 5;

    private PaginationHelper() {
    }

    public static List<Integer> buildPageNumbers(int currentPage, int totalPages) {
        if (totalPages <= 0) {
            return Collections.emptyList();
        }
        int safeCurrent = Math.min(Math.max(1, currentPage), totalPages);
        int start = Math.max(1, safeCurrent - 2);
        int end = Math.min(start + DEFAULT_WINDOW_SIZE - 1, totalPages);
        if (end - start + 1 < DEFAULT_WINDOW_SIZE) {
            start = Math.max(1, end - DEFAULT_WINDOW_SIZE + 1);
        }
        return IntStream.rangeClosed(start, end).boxed().toList();
    }
}
