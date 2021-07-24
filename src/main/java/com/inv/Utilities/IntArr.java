package com.inv.Utilities;

import java.util.ArrayList;

public class IntArr {

    public static int[] removeItem(int[] arr, int item) {
        int[] newArr = new int[arr.length - 1];
        int index = 0;
        for (int v : arr) {
            if (v == item) continue;
            newArr[index++] = v;
        }
        return newArr;
    }

    public static int[] addItem(int[] arr, int item) {
        int[] newArr = new int[arr.length + 1];
        for (int i = 0; i < arr.length; i++) {
            newArr[i] = arr[i];
        }
        newArr[arr.length] = item;
        return newArr;
    }

    public static ArrayList<Integer> toArrayList(int[] arr) {
        ArrayList<Integer> list = new ArrayList<>();
        for (int v : arr) {
            list.add(v);
        }
        return list;
    }

    public static int[] toArray(ArrayList<Integer> list) {
        int[] arr = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            arr[i] = list.get(i);
        }
        return arr;
    }
}
