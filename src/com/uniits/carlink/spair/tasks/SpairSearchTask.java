package com.uniits.carlink.spair.tasks;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import com.uniits.carlink.domain.BaiduMusicResultInfo;

public class SpairSearchTask {
	
	private List<String> engineBeanList;
	private CountDownLatch taskCount;
	private List<BaiduMusicResultInfo> resultList;
	
	public SpairSearchTask(List<String> engineBeanList, CountDownLatch taskCount) {
		this.engineBeanList = engineBeanList;
		this.taskCount = taskCount;
		this.resultList = new ArrayList<BaiduMusicResultInfo>();
	}

	public synchronized String getEngineBean() {
		if (engineBeanList.size() == 0) {
			return null;
		}
		return engineBeanList.remove(engineBeanList.size() - 1);
	}
	
	public synchronized void reduceTask() {
		taskCount.countDown();
		System.out.println("==>> 还剩余的任务数为 : " + taskCount.getCount());
	}

	public boolean isEmpty() {
		boolean isEmpty = false;
		if (engineBeanList.size() == 0) {
			isEmpty = true;
		}
		return isEmpty;
	}
	
	public synchronized void addResult(BaiduMusicResultInfo resultTo) {
		this.resultList.add(resultTo);
	}
	
	public synchronized void addResult(List<BaiduMusicResultInfo> list) {
        this.resultList.addAll(list);
    }
	
	public List<BaiduMusicResultInfo> getResult() {
		return this.resultList;
	}
}
