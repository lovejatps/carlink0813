/**
 * 
 */
package com.uniits.carlink.test;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Test;
import org.nutz.http.Http;
import org.nutz.http.Response;
import org.nutz.lang.Stopwatch;

/**
 * @author lcw
 *
 */
public class TestThread {
    
    public void t() {
        Stopwatch sw = Stopwatch.begin();
        String url = "http://api.36wu.com/Weather/GetWeather?district=北京";
        url = "http://ting.baidu.com/data/music/links?qq-pf-to=pcqq.temporaryc2c&songIds=33984572,1102942,7316462,1000860";
//      url = "http://www.xici.net.co/";
//      Http.setHttpProxy("222.87.129.218", 80);
//      Http.setHttpProxy("36.250.74.88", 80);
        
//      Http.setHttpProxy("183.203.208.167", 8118);
        
        Http.setHttpProxy("163.177.79.5", 80);
        
//      Http.setHttpProxy("182.118.23.7", 8081);
//      Http.setHttpProxy("182.254.129.68", 80);
        
        
            
        
        
        Response rs = Http.get(url);
        System.out.println(rs.getStatus());
        System.out.println(rs.getDetail());
        System.out.println(rs.getContent());
        sw.stop();
        System.out.println(">>>>> dur:" + sw.getDuration());
        
    }
    
    private final class StringTask implements Callable<String> {
        

        @Override
        public String call() throws Exception {
            for(int i = 0;i<10;i++)
            t();
            
            return null;
        }
     }

    @Test
    public void test() throws InterruptedException, ExecutionException {
        ExecutorService threadPool = Executors.newFixedThreadPool(4);
        CompletionService<String> pool = new ExecutorCompletionService<String>(threadPool);
         
        for(int i = 0; i < 5; i++){
           pool.submit(new StringTask());
        }
         
        for(int i = 0; i < 10; i++){
           String result = pool.take().get();
           System.out.println(result);
           //Compute the result
        }
         
        threadPool.shutdown();
    }
}
