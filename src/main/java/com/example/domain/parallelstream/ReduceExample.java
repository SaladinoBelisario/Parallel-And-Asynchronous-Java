package com.example.domain.parallelstream;

import java.util.List;

public class ReduceExample {

    public  int reduce_sum_ParallelStream(List<Integer> inputList){

        return inputList
                .parallelStream()
                //.reduce(1, (x,y)->x+y);
                .reduce(0, (x,y)->x+y);
    }

    public  int reduce_multiply_parallelStream(List<Integer> inputList){
        return inputList
                .parallelStream()
                .reduce(1, (x,y)->x*y);
    }

}
