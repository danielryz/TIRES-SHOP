package org.tireshop.tiresshopapp.util;

import org.springframework.data.domain.Sort;

import java.util.Arrays;

public class SortUtils {

    public static Sort parseSort(String[] sortParams) {
        return Sort.by(Arrays.stream(sortParams)
                .map(s -> {
                    String[] parts = s.split(",");
                    String property = parts[0];
                    String direction = parts.length > 1 ? parts[1] : "asc";
                    return new Sort.Order(Sort.Direction.valueOf(direction), property);
                }).toList());
    }
}
