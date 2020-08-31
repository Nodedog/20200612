import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
/*
*
*
*                   线程池
*
*
* */
public class Test1 {


    //使用Worker这个类来描述当前的工作线程是什么样的
    static  class Worker extends Thread{
        private int id = 0;
        //每个Worker线程都需要从任务队列中取任务
        //需要能够获取到任务队列的实例
        private BlockingQueue<Runnable> queue = null;
        public  Worker(BlockingQueue<Runnable> queue,int id){
            this.queue = queue;
            this.id = id;
        }

        @Override
        public void run() {
            try {
                //注意此处的try把while包裹进去
                //目的是只要线程收到异常,就会立刻结束run方法(即结束线程)
                while (!Thread.currentThread().isInterrupted()){
                    Runnable command = queue.take();//出阻塞队列
                    System.out.println("thread" + id + "running.....");
                    command.run();
                }
            } catch (InterruptedException e) {
                //线程被结束
                System.out.println("线程被结束");
            }
        }
    }


    //本质上就是一个生产者消费者模式
    //调用execute的代码就是生产者,生产了任务(Runnable对象)
    //worker线程就是消费者,消费了队列中的任务
    //交易场所就是BlckingQueue
    static class MyThreadPool{

        //这个阻塞队列用来组织若干个任务
        private BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>();

        //这个List用来组织若干个工作线程
        private List<Worker> list = new ArrayList<>();

        //一个线程池中有多少个线程,应根据实际情况确定
        //这里定的"10" 只是随机写的
        private static final int maxWorkerCount = 10;

        //实现execute方法和shutdown方法
        public  void execute(Runnable command) throws InterruptedException {
            //使用延时加载的方式来创建线程
            //当线程池中的线程数目比较少的时候,新创建线程来当工作线程
            //如果线程数目已经比较多了(达到设定的final值),就不用创建新的线程了
            if (list.size() < maxWorkerCount){
                Worker worker = new Worker(queue,list.size());
                worker.start();
                list.add(worker);
            }

            queue.put(command);//入阻塞队列
        }

        //当shutdown方法结束之后所有线程一定结束
        public void shutdown() throws InterruptedException {
            //foreatch循环 for(数据类型 变量/实例化名称: 所在的类型)
            for (Worker worker: list ) {
                worker.interrupt();
            }

            //还需要等待所有线程结束
            for (Worker worker: list ) {
                worker.join();
            }
        }

    }


    public static void main(String[] args) throws InterruptedException {
        MyThreadPool myThreadPool = new MyThreadPool();
        for (int i = 0; i < 100; i++) {
            myThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println("正在执行任务");
                }
            });
        }
        Thread.sleep(2000);
        myThreadPool.shutdown();
        System.out.println("线程池已经被销毁");

    }
    
}
