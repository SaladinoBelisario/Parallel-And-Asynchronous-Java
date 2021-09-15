package com.example.forkjoin;

import com.example.util.DataSet;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

import static com.example.util.CommonUtil.delay;
import static com.example.util.CommonUtil.stopWatch;
import static com.example.util.LoggerUtil.log;

public class ForkJoinRecursion extends RecursiveTask<List<String>> {

    private List<String> inputList;

    public ForkJoinRecursion(List<String> inputList) {
        this.inputList = inputList;
    }

    public static void main(String[] args) {

        stopWatch.start();

        List<String> resultList;
        List<String> names = DataSet.namesList();
        ForkJoinPool pool = new ForkJoinPool();
        ForkJoinRecursion recursionExample = new ForkJoinRecursion(names);
        resultList = pool.invoke(recursionExample);

        stopWatch.stop();
        log("Final Result : "+ resultList);
        log("Total Time Taken : "+ stopWatch.getTime());
    }


    private static String addNameLengthTransform(String name) {
        delay(500);
        return name.length()+" - "+name ;
    }

    @Override
    protected List<String> compute() {
        if (inputList.size() <= 1){
            List<String> resultList = new ArrayList<>();
            inputList.forEach(name -> resultList.add(addNameLengthTransform(name)));
            return resultList;
        }

        int midpoint = inputList.size()/2;
        ForkJoinTask<List<String>> leftInputList = new ForkJoinRecursion(inputList.subList(0, midpoint)).fork();
        inputList = inputList.subList(midpoint, inputList.size());
        List<String> rightResult = compute(); // recursion happens
        List<String> leftResult = leftInputList.join();
        leftResult.addAll(rightResult);
        return leftResult;
    }
}
