package me.peterfei.threaddemo.model;

import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: thread-demo
 * @description:
 * @author: peterfei
 * @create: 2022-09-27 10:03
 **/
public class LoopTask {
    private List<ChildTask> childTasks;
    public void initLoopTask(){

        childTasks = new ArrayList<>();
        childTasks.add(new ChildTask("childTask1"));
        childTasks.add(new ChildTask("childTask2"));
        for (final ChildTask childTask: childTasks
             ) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    childTask.doExecute();
                }
            }).start();

        }
    }

    public void shutdownLoopTask(){
        if (!CollectionUtils.isEmpty(childTasks)){
            for (ChildTask childTask:childTasks
                 ) {
                childTask.terminal();

            }
        }
    }

}
