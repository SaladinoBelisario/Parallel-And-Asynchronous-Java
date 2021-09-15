package com.example.domain.parallelstream;

import com.example.util.DataSet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static com.example.util.CommonUtil.*;
import static org.junit.jupiter.api.Assertions.*;

class ParallelStreamsExampleTest {

    ParallelStreamsExample parallelismExample = new ParallelStreamsExample();

    @Test
    void stringTransform() {
        startTimer();
        List<String> stringList = parallelismExample.stringTransform(DataSet.namesList());
        timeTaken();
        assertEquals(4, stringList.size());
        stringList.forEach((name) -> {
            assertTrue(name.contains("-"));
        });
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    void stringTransform_1(boolean isParallel) {

        //given
        startTimer();
        List<String> inputList = DataSet.namesList();

        //when
        List<String> stringList = parallelismExample.stringTransform_1(inputList,isParallel);
        timeTaken();
        stopWatchReset();

        //then
        assertEquals(4, stringList.size());
        stringList.forEach((name) -> {
            assertTrue(name.contains("-"));
        });
    }
}