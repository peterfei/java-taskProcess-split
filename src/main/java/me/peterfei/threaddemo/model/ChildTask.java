package me.peterfei.threaddemo.model;

import com.google.common.collect.Lists;
import me.peterfei.threaddemo.utils.TaskProcessUtil;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

/**
 * @program: thread-demo
 * @description:
 * @author: peterfei
 * @create: 2022-09-27 09:37
 **/
public class ChildTask {
    private final  int POOL_SIZE =3;//线程池大小
    private final int SPLIT_SIZE =4;//数据拆分大小
    private String taskName;

    //jvm 信号
    protected volatile boolean terminal = false;

    public ChildTask(String taskName){
        this.taskName = taskName;
    }

    //程序执行入口
    public void doExecute(){
        int i = 0;
        while(true){
            System.out.println(taskName +":Cycle-"+i+"-Begin");
            //获取数据
            List<Cat> datas = queryData();
            //处理数据
            taskExecute(datas);
            System.out.println(taskName + ":Cycle-" + i + "-End");
            if (terminal) {
                // 只有应用关闭，才会走到这里，用于实现优雅的下线
                break;
            }
            i++;
        }
        //回收线程池资源
        TaskProcessUtil.releaseExecutors(taskName);
    }

    private void taskExecute(List<Cat> datas) {
        if (CollectionUtils.isEmpty(datas)){
            return ;
        }
        //拆分数据
        List<List<Cat>> splitDatas  = Lists.partition(datas,SPLIT_SIZE);
        final CountDownLatch latch = new CountDownLatch(splitDatas.size());
        //并发处理数据，共用一个线程池
        for (final List<Cat> _datas: splitDatas) {
            ExecutorService executorService = TaskProcessUtil.getOrInitExecutors(taskName,POOL_SIZE);
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    doProcessData(_datas,latch);
                }

            });
        }
        try{
            latch.await();
        }catch (Exception e){
            System.out.println(e.getStackTrace());
        }

    }

    private void doProcessData(List<Cat> datas, CountDownLatch latch) {

        try{
            for (Cat cat : datas
            ) {
                System.out.println(taskName +"："+cat.toString()+",ThreadName:"+Thread.currentThread().getName());
                Thread.sleep(1000L);

            }
        }catch (Exception e){
            System.out.println(e.getStackTrace());
        }finally {
            if (latch != null){
                latch.countDown();
            }
        }

    }

    private List<Cat> queryData() {
        List<Cat> datas = new ArrayList<>();
        for (int i = 0; i < 5 ; i++) {
            datas.add(new Cat().setCatName("peterfei"+i));

        }
        return datas;
    }

    public void terminal() {
        terminal = true;
        System.out.println(taskName + " shut down");
    }
}
