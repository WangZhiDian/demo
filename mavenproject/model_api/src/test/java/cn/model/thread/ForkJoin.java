package cn.model.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;

/**
 * Created by wangdian05 on 2018/7/9.
 */
@Slf4j
public class ForkJoin {

    public static void main(String[] args) {
        testAcation();
    }

    private static void testAcation() {
        ForkJoinPool pool = new ForkJoinPool();
        ForkJoinTask<Integer> future = pool.submit(new ResultTask(100));//共100棵苹果树
        try {
            future.get();
            pool.awaitTermination(1000, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        pool.shutdown();
    }
}


@Slf4j
class ResultTask extends RecursiveTask<Integer> { //也可继承自RecursiveAction抽象类，区别在于compute方法没有返回值，如果只需要执行动作则可以使用该接口
    private int treeNum;

    public ResultTask(int num) {
        this.treeNum = num;
    }

    @Override
    protected Integer compute() {
        if (treeNum < 10) {//每个线程最多只能摘10棵苹果树
            return getAppleNum(treeNum);
        } else {

            //对任务进行拆分，注意这里不仅仅可以一分为二进行拆分，也可以拆为多个子任务
            int temp = treeNum / 2;
            ResultTask left = new ResultTask(temp);
            ResultTask right = new ResultTask(treeNum - temp);
            left.fork();
            right.fork();
            //对子任务处理的结果进行合并
            int result = left.join() + right.join();

            log.info("========" + Thread.currentThread().getName() + "=========" + result);
            return result;
        }
    }

    public Integer getAppleNum(int treeNum) {
        log.info(treeNum * 10 + "");
        return treeNum * 10;//每棵树上10个苹果
    }
}
