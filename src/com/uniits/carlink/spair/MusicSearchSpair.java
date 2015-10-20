package com.uniits.carlink.spair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.nutz.lang.Stopwatch;
import org.nutz.lang.Times;

import com.uniits.carlink.domain.BaiduMusicResultInfo;
import com.uniits.carlink.spair.tasks.SpairSearchTask;
import com.uniits.carlink.spair.threads.SpairSearchThread;

public class MusicSearchSpair {
    
	public static List<BaiduMusicResultInfo> startRun(List<String> engineBeanList) {
        try {
            System.out.println(Times.sDT(new Date()) + " ==>> 实时监控抓取开始  ==>> " + engineBeanList.size());
            CountDownLatch taskSearchCount = new CountDownLatch(engineBeanList.size());
            SpairSearchTask spairSearchTask = new SpairSearchTask(engineBeanList, taskSearchCount);
            
            SpairSearchThread s = new SpairSearchThread(" ==>> 实时监控抓取线程 ：" , spairSearchTask);
            ExecutorService execSearch = Executors.newFixedThreadPool(20);
            for (int i = 0; i < 20; i++)
                execSearch.submit(
                    new Thread(s)
                );
                
            execSearch.shutdown();
            try {
                taskSearchCount.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Times.sDT(new Date()) + " ==>> 实时监控抓取结束  ==>> ");

            return spairSearchTask.getResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
	
}
