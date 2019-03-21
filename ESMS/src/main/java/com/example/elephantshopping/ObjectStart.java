package com.example.elephantshopping;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.example.elephantshopping.service.operationsManage.ExperienceService;

/**
 * 项目启动时执行
 * @author XB
 *
 */
@Component
public class ObjectStart implements ApplicationRunner{

	@Autowired
	private ExperienceService experienceService;
	
	@Override
	public void run(ApplicationArguments arg0) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//得到所有的定时任务
		List<Map<String,Object>> list = experienceService.getAllSetTimes();
		for(Map<String,Object> map:list){
			String id = (String) map.get("ID");
			Date time = sdf.parse(sdf.format(map.get("TIME")));
			Date now = new Date();
			if(now.after(time)){//判断定时器是否过时
				experienceService.deleteSetTime(id);
			}else{
				new Thread(new ActivityAmountControl(id,experienceService)).start();
			}
		}
	}

}
