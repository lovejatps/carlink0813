/**
 * 
 */
package com.uniits.carlink.test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author lcw
 *
 */
public class CountDownLatchTest {

    /** 
     * 初始化组件的线程 
     */  
    class ComponentThread implements Runnable {  
        // 计数器  
        CountDownLatch latch;  
        // 组件ID  
        int ID;  
        
        Object read = new Object();
        Object write = new Object();
  
        // 构造方法  
        public ComponentThread(CountDownLatch latch, int ID) {  
            this.latch = latch;  
            this.ID = ID;  
        }  
        
        public synchronized void reduce() {
            {
                latch.countDown(); 
            }
        }
        
        public synchronized boolean isEmpty() {
             {
                 reduce();
             return !(latch.getCount() > 0); 
            }
        }
        
        public void run() {
            while(!isEmpty()) {
                // 初始化组件  
                System.out.println("Initializing component " + ID);  
                try {  
                    Thread.sleep(500 * ID);  
                } catch (InterruptedException e) {  
                }  
                System.out.println("Component " + ID + " initialized!");  
                //将计数器减一  
                reduce();
            }
        }  
    }  
  
    /** 
     * 启动服务器 
     */  
    public void startServer() throws Exception { 
        System.out.println("Server is starting.");  
        //初始化一个初始值为3的CountDownLatch  
        CountDownLatch latch = new CountDownLatch(2);  
        //起3个线程分别去启动3个组件  
        ExecutorService service = Executors.newCachedThreadPool();  
        service.submit(new ComponentThread(latch, 1));  
//        service.submit(new ComponentThread(latch, 2));  
//        service.submit(new ComponentThread(latch, 3));  
  
        //等待3个组件的初始化工作都完成  
        latch.await();  
        service.shutdownNow();  
  
        //当所需的三个组件都完成时，Server就可继续了  
        System.out.println("Server is up!");  
    }  
  
    public static void main(String[] args) throws Exception {
        new CountDownLatchTest().startServer();  
    }  
}
