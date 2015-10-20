package com.uniits.carlink.spair.threads;

import java.util.ArrayList;
import java.util.List;

import org.nutz.json.Json;
import com.uniits.carlink.domain.BaiduMusicResultInfo;
import com.uniits.carlink.domain.vo.baidu.BaiduMusicSecondVo;
import com.uniits.carlink.spair.tasks.SpairSearchTask;
import com.uniits.carlink.utils.Config;
import com.uniits.carlink.utils.HttpConnAssist;
import com.uniits.carlink.utils.MusicAssist;

public class SpairSearchThread implements Runnable{

	private SpairSearchTask spairSearchTask;
	String threadName;

	public SpairSearchThread(String threadName, SpairSearchTask spairSearchTask) {
		this.spairSearchTask = spairSearchTask;
		this.threadName = threadName;
	}

	@Override
	public void run() {
		while (!spairSearchTask.isEmpty()) {
			String engineBean = spairSearchTask.getEngineBean();
			System.out.println(">>>>>>>> run ..." + engineBean);
			if (engineBean == null)
				return;
			
            try {
                
                List<BaiduMusicResultInfo> list = new ArrayList<BaiduMusicResultInfo>();
                boolean b = false;
                String json = "";
                for (int i = 0; i < 3; i++) {//发请求
                    json = HttpConnAssist.getJsonInfoFromNet(Config.MusicURL.MUSICURL_TWO + engineBean, Config.MusicURL.CHARSET);
                    if (json != null && !json.trim().equals("[]")) {
                        b = true;
                        break;
                    }
                }
                if (b) {
                    BaiduMusicSecondVo bms = Json.fromJson(BaiduMusicSecondVo.class, json);
                    if (bms != null && bms.getData() != null && bms.getData().getSongList() != null) {
                        list.addAll(MusicAssist.secondResultFilter(bms.getData().getSongList()));
                    }
                }
                if(list != null && list.size() > 0)
                    spairSearchTask.addResult(list);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                System.out.println(">>>>> -1");
                spairSearchTask.reduceTask();
            }
		}
	}

}
