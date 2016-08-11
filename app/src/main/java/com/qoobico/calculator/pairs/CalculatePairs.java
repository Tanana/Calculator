package com.qoobico.calculator.pairs;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ta on 11.08.2016.
 */
public class CalculatePairs {

    private static List<Pair> parseInputString(String input) {
        String[] arr = input.split(" ");
        System.out.println("ARRAY LENGTH: "+arr.length);
        List<Pair> list = new ArrayList<>();
    try {

        for (int i = 0; i < arr.length; i += 2) {
            try {
                Pair p = new Pair();
                if (arr[i] != null) {
                    p.setFirstNumber(Integer.valueOf(arr[i]));

                } else {
                    break;
                }
                if (arr[i + 1] != null) {
                    p.setSecondNumber(Integer.valueOf(arr[i + 1]));

                } else {
                    break;
                }
                list.add(p);
            }catch (IndexOutOfBoundsException e){

            }

        }
    } catch(NumberFormatException e){
        return null;
    }
        return list;

    }

    private static List<Pair> findPairs(String input) {
    try {


        List<Pair> list = new ArrayList<>(parseInputString(input));
        System.out.println("Size: "+list.size());
        List<Pair> tempList = new ArrayList<>();
        List<Pair> maxSubqueryList = new ArrayList<>();

        if (list.isEmpty()) {
            return null;
        }

    for (int i = 0; i < list.size(); i += 2) {
        try {
        if (list.get(i).getFirstNumber() < list.get(i).getSecondNumber()
                && list.get(i + 1).getFirstNumber() > list.get(i + 1).getSecondNumber()) {
            tempList.add(list.get(i));
            tempList.add(list.get(i + 1));
        } else {
            if (tempList.size() >= maxSubqueryList.size()) {
                maxSubqueryList.clear();
                maxSubqueryList.addAll(tempList);
                tempList.clear();
            }
        }
    }catch (IndexOutOfBoundsException e1){
            System.out.println("Exception in algorithm");
        continue;
    }
    }

    return maxSubqueryList;

    } catch(NullPointerException e){
        return null;
    }

    }

    public static String getPairs(String input){
        try {


            List<Pair> list = new ArrayList<>(findPairs(input));
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < list.size(); i++) {
                sb.append("[" + list.get(i).getFirstNumber() + " " + list.get(i).getSecondNumber() + "],");
            }
            return sb.toString();
        } catch(NullPointerException e){
            return "Enter correct data in format : \"15 35 5 6 78 98\", etc. ";
        }

    }



}
