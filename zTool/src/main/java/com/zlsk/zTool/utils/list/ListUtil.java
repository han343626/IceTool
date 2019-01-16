package com.zlsk.zTool.utils.list;

import com.zlsk.zTool.utils.system.ReflectUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by IceWang on 2018/5/25.
 */

public class ListUtil {
    public static boolean isEmpty(List<?> list) {
        return (list == null) || (list.size() == 0);
    }

    public static <T> boolean isEmpty(T[] array) {
        return (array == null) || (array.length == 0);
    }

    public static <T extends Comparable<? super T>> void sort(List<T> list, boolean isAscending) {
        Collections.sort(list);
        if (!isAscending) {
            Collections.reverse(list);
        }
    }

    public static <T> List<T> toList(T[] array) {
        List<T> result = new ArrayList<>();
        Collections.addAll(result,array);
        return result;
    }

    public static <T> List<T> singleToList(T object){
        List<T> result = new ArrayList<>();
        result.add(object);
        return result;
    }

    public static <T> T[] toArray(List<T> list, T[] target) {
        return list.toArray(target);
    }

    public static <T> List<T> deepCopy(List<T> list){
        List<T> result = new ArrayList<>();
        for (T item : list){
            result.add(item);
        }
        return result;
    }

    /**
     * list去重复(浅)
     */
    public static <T> List<T> deduplicate(List<T> list) {
        Set<T> set = new HashSet<>();
        List<T> newList = new ArrayList<>();
        for (T element : list) {
            if (set.add(element)){
                newList.add(element);
            }
        }
        return newList;
    }

    /**
     * array去重(浅)
     */
    public static <T> List<T> deduplicate(T[] array){
        return deduplicate(toList(array));
    }

    /**
     * list去重复(深) 并根据字段进行排序
     */
    public static <T> ArrayList<T> deduplicate(List<T> list, String filedName) {
        Set<T> set = new TreeSet<>(new Comparator<T>() {
            @Override
            public int compare(T o1, T o2) {
                String object1 = ReflectUtils.getValueByFiledName(o1,filedName);
                String object2 = ReflectUtils.getValueByFiledName(o2,filedName);
                return object1.compareTo(object2);
            }
        });
        set.addAll(list);
        return new ArrayList<>(set);
    }

    /**
     * array去重(深)
     */
    public static <T> List<T> deduplicate(T[] array,String name){
        return deduplicate(toList(array),name);
    }
}
