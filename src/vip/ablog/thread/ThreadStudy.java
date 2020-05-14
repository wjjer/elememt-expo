package vip.ablog.thread;

import java.util.concurrent.Callable;

/***
 * @author: jsp
 * @datetime: 2019/9/10 001021:11
 * @description: TODO
 * @version :1.0
 ***/
public class ThreadStudy {

    private static int count = 0;

    /*public static void main(String[] args) {
        //创建线程
        ExecutorService executorService =
                Executors.newFixedThreadPool(1);
        //获取值
        Future<Boolean> result = executorService.submit(new Call());
        try {
            Boolean success = result.get();
            System.out.println("结果:"+success);
            executorService.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }*/

    public static void adCount(){
        count++;
    }

}

class AThread implements Runnable{

    @Override
    public void run() {
        for (int i = 0; i < 1000; i++) {
            System.out.println("数据："+i);
            //ThreadStudy.adCount();
        }
    }
}

class Call implements Callable<Boolean>{
    @Override
    public Boolean call() throws Exception {
        for (int i = 0; i < 1000; i++) {
            System.out.println("数据："+i);
        }
        return true;
    }
}


class Test {
    public static void main(String[] args) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    if (i == 5){
                        Thread.yield();
                    }
                    System.out.println("线程2:"+i);
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    System.out.println("线程1:"+i);
                }
            }
        }).start();

    }
}
