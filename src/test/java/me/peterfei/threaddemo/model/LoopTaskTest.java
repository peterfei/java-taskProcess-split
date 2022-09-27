package me.peterfei.threaddemo.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoopTaskTest {

    @Test
    void initLoopTask() throws Exception{
        LoopTask loopTask = new LoopTask();
        loopTask.initLoopTask();
        Thread.sleep(150000L);
        loopTask.shutdownLoopTask();
    }
}