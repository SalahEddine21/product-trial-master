package com.alten.back.utils;

import com.alten.back.entities.Product;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.security.InvalidParameterException;
import java.util.Arrays;

public class Utils {


    public static Pageable getPageable(Integer page, Integer size, String sortField, String sortDirection) {
        if(size > Constant.PAGE_SIZE){
            throw new InvalidParameterException("size must be less than "+Constant.PAGE_SIZE);
        }
        if(page < 0){
            throw new InvalidParameterException("page must be greater than 0");
        }
        if(!isFieldInClass(Product.class, sortField)){
            throw new InvalidParameterException("sort field must be in the class Product");
        }
        if(!Constant.SORT_DIRECTIONS.contains(sortDirection)){
            throw new InvalidParameterException("sort direction must be one of "+Constant.SORT_DIRECTIONS);
        }

        Sort sort = sortDirection.equalsIgnoreCase("DESC")
                ? Sort.by(sortField).descending()
                : Sort.by(sortField).ascending();
        return PageRequest.of(page, size, sort);
    }

    public static boolean isFieldInClass(Class<?> c, String fieldName){
        return Arrays.stream(c.getDeclaredFields()).anyMatch(field -> field.getName().equalsIgnoreCase(fieldName));
    }
}
